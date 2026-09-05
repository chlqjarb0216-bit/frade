package com.frade.service.stock.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.frade.config.KiwoomApiConfig;
import com.frade.dto.stock.StockPriceComputableDTO;
import com.frade.dto.stock.StockPriceDTO;
import com.frade.memcache.StockMemoryCache;
import com.frade.service.stock.SseChartPushService;
import com.frade.service.stock.StockDataBufferService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StockDataBufferServiceImpl implements StockDataBufferService {

	private final ObjectMapper objectMapper;
	private final TaskExecutor stockBufferTaskExecutor;
	private final StockMemoryCache stockMemoryCache; // 이미 RAM에 로드되어 있는 초고속 100종목 캐시
	private final SseChartPushService sseChartPushService;

	private final Queue<String> stockEventQueue = new ConcurrentLinkedQueue<>();
	private final Map<String, StockPriceComputableDTO> globalBufferMap = new ConcurrentHashMap<>();
	private final Queue<String> batchTimeTaskQueue = new ConcurrentLinkedQueue<>();

	// 1분에 단 1번, 5초 지연 예약을 위한 자바 내장 싱글 스레드 알람 구조 (XML 선언 불필요)
	private final ScheduledExecutorService delayExecutor = Executors.newSingleThreadScheduledExecutor();
	private ScheduledFuture<?> autoCloseTaskFuture = null; // 장마감 시 고립 방지 시한폭탄 타이머

	private String todayDateStr;
	private static final DateTimeFormatter MINUTE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
	private volatile String lastRecordedMinuteStr = ""; // 락(Lock) 없는 멀티스레드 캐시 동기화 가드
	private volatile String prevRecordedMinuteStr = "";

	public StockDataBufferServiceImpl(ObjectMapper objectMapper, TaskExecutor stockBufferTaskExecutor,
			StockMemoryCache stockMemoryCache, SseChartPushService sseChartPushService,
			KiwoomApiConfig kiwoomApiConfig) {
		this.objectMapper = objectMapper;
		this.stockBufferTaskExecutor = stockBufferTaskExecutor;
		this.stockMemoryCache = stockMemoryCache;
		this.sseChartPushService = sseChartPushService;
	}

	@Override
	public void enqueueRealtimeData(String data) {
		this.stockEventQueue.offer(data);
	}

	/**
	 * [🎯 인메모리 캐시 결합 상수시간 마감 플러시]:
	 * 10초 주기 스케줄러가 호출하는 정석 창구명 수립.
	 * 맵 전체 순회(contains)를 박멸하고, 메모리 캐시 리스트를 기반으로 1분 전 타겟 주소방을 O(1) 속도로 다이렉트 타격 제거(`remove`)합니다.
	 */
	@Override
	public List<StockPriceDTO> flushCompleteMinuteBuffer() {
		String targetMinuteStr = batchTimeTaskQueue.poll();
		if (targetMinuteStr == null) {
			return new ArrayList<>(); // 큐가 비어있으면 0초만에 빈 리스트 직접 리턴 (isEmpty 효과)
		}

		List<StockPriceDTO> targetList = new ArrayList<>();
		List<String> cachedStockCodes = stockMemoryCache.getStockCodeList(); // RAM에서 0초만에 100종목 가져오기

		for (String stockCode : cachedStockCodes) {
			String fullKey = stockCode + "_" + targetMinuteStr;

			// 💡 제로 카피(Zero-Copy): 맵에서 끈을 완전히 끊음과 동시에 리스트로 주소지 이관
			StockPriceDTO dto = globalBufferMap.remove(fullKey).toFinalDTO();
			if (dto != null) {
				targetList.add(dto);
			}
		}
		return targetList;
	}

	@Override
	public Map<String, Integer> getMinPriceSnapshotMap() {
		// 1. 현재 엔진이 바라보고 있는 최신 분 문자열 확보 (lastRecordedMinuteStr은 고정 유지)
		String targetMinuteStr = this.lastRecordedMinuteStr;
		String backupMinuteStr = this.prevRecordedMinuteStr;
		if (targetMinuteStr == null || targetMinuteStr.isEmpty()) {
			return Collections.emptyMap(); // 아직 데이터가 안 들어왔으면 0초 만에 빈 맵 리턴
		}

		// 2. RAM 메모리 캐시에서 현재 활성화된 종목 리스트 0초 만에 가져오기
		List<String> cachedStockCodes = stockMemoryCache.getStockCodeList();
		if (cachedStockCodes == null || cachedStockCodes.isEmpty()) {
			return Collections.emptyMap();
		}

		// 초기 용량을 종목 개수만큼 지정하여 맵 리사이징(Re-indexing) 오버헤드 박멸
		Map<String, Integer> snapshotMap = new HashMap<>(cachedStockCodes.size());

		// 3. 전체 맵 풀 스캔 없이, 등록된 종목만큼만 다이렉트 복합키 타격
		for (String stockCode : cachedStockCodes) {
			String fullKey = stockCode + "_" + targetMinuteStr;

			// 💡 마감 이관이 아니므로 remove가 아닌 get으로 안전하게 조회만 수행
			StockPriceComputableDTO dto = globalBufferMap.get(fullKey);
			if (dto != null) {
				// Key: 종목코드, Value: 현재 시점의 실시간 체결가(종가)
				snapshotMap.put(stockCode, dto.getPriceClose());
			} else if (!backupMinuteStr.isEmpty()) {
				String prevKey = stockCode + "_" + backupMinuteStr;

				// 💡 마감 이관이 아니므로 remove가 아닌 get으로 안전하게 조회만 수행
				StockPriceComputableDTO prevDto = globalBufferMap.get(prevKey);
				if (prevDto != null) {
					// Key: 종목코드, Value: 현재 시점의 실시간 체결가(종가)
					snapshotMap.put(stockCode, prevDto.getPriceClose());
				}
			}
		}

		return snapshotMap; // 완벽하게 정제된 맵 주소지 그대로 리턴
	}

	@Override
	public int getMinPriceSnapshotByStockCode(String stockCode) {
		String targetMinuteStr = this.lastRecordedMinuteStr;
		String backupMinuteStr = this.prevRecordedMinuteStr;
		if (stockCode == null || stockCode.isEmpty() || this.lastRecordedMinuteStr.isEmpty()) {
			return 0;
		}
		String fullKey = stockCode + "_" + targetMinuteStr;
		StockPriceComputableDTO dto = globalBufferMap.get(fullKey);
		if (dto != null) {
			return dto.getPriceClose();
		} else if (!backupMinuteStr.isEmpty()) {
			String prevKey = stockCode + "_" + backupMinuteStr;

			// 💡 마감 이관이 아니므로 remove가 아닌 get으로 안전하게 조회만 수행
			StockPriceComputableDTO prevDto = globalBufferMap.get(prevKey);
			if (prevDto != null) {
				return prevDto.getPriceClose();
			}
		}
		return 0;
	}

	@PostConstruct
	private void init() {
		// 스프링 부팅 즉시 날짜 캐싱 완료
		this.todayDateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		// 프록시 제약 조건 없이 스레드 풀의 독점 일꾼 1명을 즉시 깨워 무한루프 구동
		stockBufferTaskExecutor.execute(() -> {
			while (true) {
				try {
					if (stockEventQueue.isEmpty()) {
						Thread.sleep(10); // CPU 가드
						continue;
					}
					String rawJson = stockEventQueue.poll();
					if (rawJson != null) {
						processRealtimeData(rawJson); // 같은 클래스 내부 호출 전혀 문제 없음!
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				} catch (Exception e) {
					// 에러가 나도 무한루프가 터지지 않도록 예외 포획 및 로그 처리
					log.error("큐 데이터 처리중 에러발생: {}", e.getMessage());
				}
			}
		});

	}

	//키움 데이터의 진짜 체결 시각(FID 20)을 기준으로 스스로 '분 변경선'과 '장마감 무거래' 상태를 판별합니다.
	private void processRealtimeData(String stockJsonText) {
		try {
			ObjectNode stockData = (ObjectNode) objectMapper.readTree(stockJsonText);
			String stockCode = stockData.path("item").asText("");
			ObjectNode values = (ObjectNode) stockData.path("values");

			if (values != null && !stockCode.isEmpty()) {
				int currentPrice = Math.abs(values.path("10").asInt(0));
				long rawVolume = values.path("15").asLong(0);
				String rawTime = values.path("20").asText("");

				if (rawTime.length() < 4)
					return;
				String currentMinuteStr = this.todayDateStr + rawTime.substring(0, 4); // hhmm 결합

				// 💡 A. 장중 데이터 시각 변경선 감지 (5초 안전 마진 가드 자동 예약)
				if (lastRecordedMinuteStr.isEmpty()) {
					lastRecordedMinuteStr = currentMinuteStr;
				} else if (!currentMinuteStr.equals(lastRecordedMinuteStr)) {
					final String minuteToClose = lastRecordedMinuteStr;

					this.prevRecordedMinuteStr = lastRecordedMinuteStr;
					this.lastRecordedMinuteStr = currentMinuteStr;

					delayExecutor.schedule(() -> {
						batchTimeTaskQueue.add(minuteToClose);
					}, 5, TimeUnit.SECONDS);
				}

				// 💡 B. 장마감 시 시세 정지 상황 가드 (65초 시한폭탄 상시 리셋 갱신 기법)
				if (autoCloseTaskFuture != null)
					autoCloseTaskFuture.cancel(false);
				final String forceMinuteToClose = currentMinuteStr;
				autoCloseTaskFuture = delayExecutor.schedule(() -> {
					batchTimeTaskQueue.add(forceMinuteToClose);
				}, 65, TimeUnit.SECONDS);

				// 💡 C. 글로벌 맵 실시간 다이렉트 적재 연산 (초고속 차트 연동용 '종목코드_분시간' 결합키 사용)
				String compositeKey = stockCode + "_" + currentMinuteStr;
				StockPriceComputableDTO dto = globalBufferMap.compute(compositeKey, (k, d) -> {
					if (d == null)
						return new StockPriceComputableDTO(stockCode,
								LocalDateTime.parse(currentMinuteStr, MINUTE_FORMATTER), currentPrice, rawVolume);
					d.updateRealtimeData(currentPrice, rawVolume);
					return d;
				});

				// 💡 D. SSE 단방향 JSON 스트링 멀티캐스팅 푸시 즉시 연동
				sseChartPushService.pushChartToSse(stockCode, dto.toFinalDTO(), currentMinuteStr);
			}
		} catch (Exception e) {
			log.error("수집 엔진 에러: {}", e.getMessage());
		}
	}

	@PreDestroy
	private void shutdownExecutor() {
		log.info("톰캣 리로드 및 종료 시 좀비 스레드 누수 방지를 위한 알람 실행기 종료 파괴.");
		this.delayExecutor.shutdown();
	}
}
