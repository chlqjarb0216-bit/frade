package com.frade.service.stock.impl;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frade.common.ResultCode;
import com.frade.dto.Response;
import com.frade.dto.event.RealtimeStockEvent;
import com.frade.dto.stock.StockPriceDTO;
import com.frade.service.stock.SseChartPushService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseChartPushServiceImpl implements SseChartPushService {

	private final ObjectMapper objectMapper;

	// 분 변경선 비교를 위한 시간 포맷터 (yyyyMMddHHmm)
	private static final DateTimeFormatter MINUTE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

	// ⏰ 대한민국 표준시(KST) 타임존 정의
	private static final ZoneId SEOUL_ZONE = ZoneId.of("Asia/Seoul");

	// ❶ { 가상 UUID : 진짜 물리 SseEmitter } ➡️ 오직 연결 유지 및 발송용 원장 상자
	private final Map<String, SseEmitter> browserEmitters = new ConcurrentHashMap<>();

	// ❷ { 종목코드 : [현재 이 방을 켜둔 가상 UUID 리스트] } ➡️ 초고속 인덱스 맵
	private final Map<String, List<String>> stockRoomBrowserIds = new ConcurrentHashMap<>();

	// ❸ { 가상 UUID : 현재 들어가있는 종목코드 } ➡️ 방 바꿀 때 이전 방 추적용
	private final Map<String, String> browserCurrentRoom = new ConcurrentHashMap<>();

	// ❹ [트래픽 제어용] 종목코드별 최종 푸시 시간 및 스로틀링 백업 가드
	private final Map<String, Long> lastPushTimeMap = new ConcurrentHashMap<>();
	private final Map<String, StockPriceDTO> lastBlockedDtoMap = new ConcurrentHashMap<>();

	/**
	 * 사용자가 차트 화면을 켰을 때 다중 접속자 리스트에 세션을 등록하는 진입점
	 */
	@Override
	public SseEmitter subscribeStockChart(String stockCode, String browserSessionId) {
		final String finalSessionId;

		if (browserSessionId != null && !browserSessionId.trim().isEmpty()
				&& browserEmitters.containsKey(browserSessionId)) {
			log.info("🎯 [인프라 이사 성공] 살아있는 세션 확인 완료. UUID [{}]의 기존 Emitter를 활용합니다.", browserSessionId);
			finalSessionId = browserSessionId;

			String oldStockCode = browserCurrentRoom.get(finalSessionId);
			if (oldStockCode != null && stockRoomBrowserIds.containsKey(oldStockCode)) {
				stockRoomBrowserIds.get(oldStockCode).remove(finalSessionId);
			}
		} else {
			log.info("🔌 [신규 세션 감지] 번호표가 없거나 만료되었습니다. 새 가상 UUID를 발급합니다.");
			finalSessionId = "CONN_" + UUID.randomUUID().toString();

			SseEmitter newEmitter = new SseEmitter(30 * 60 * 1000L);
			browserEmitters.put(finalSessionId, newEmitter);

			newEmitter.onCompletion(() -> cleanSession(finalSessionId));
			newEmitter.onTimeout(() -> cleanSession(finalSessionId));
			newEmitter.onError((e) -> cleanSession(finalSessionId));

			try {
				newEmitter.send(SseEmitter.event().name("iam").data(finalSessionId));
			} catch (Exception e) {
				cleanSession(finalSessionId);
				return null;
			}
		}

		stockRoomBrowserIds.computeIfAbsent(stockCode, k -> new CopyOnWriteArrayList<>()).add(finalSessionId);
		browserCurrentRoom.put(finalSessionId, stockCode);

		log.info("🚀 [구독 종결] UUID [{}] 유저가 [{}] 차트 방에 수평 바인딩 안착 완료.", finalSessionId, stockCode);

		return browserEmitters.get(finalSessionId);
	}

	/**
	 * 컨트롤러와 규격을 맞춘 초고속 방 스위칭 비즈니스 로직
	 */
	@Override
	public Response<String> switchChartRoom(String browserSessionId, String newStockCode) {
		if (!browserEmitters.containsKey(browserSessionId)) {
			return Response.error(ResultCode.SESSION_NOT_FOUND);
		}

		String oldStockCode = browserCurrentRoom.get(browserSessionId);

		if (newStockCode.equals(oldStockCode)) {
			return Response.error(ResultCode.SAME_STOCK_CODE);
		}

		if (oldStockCode != null && stockRoomBrowserIds.containsKey(oldStockCode)) {
			stockRoomBrowserIds.get(oldStockCode).remove(browserSessionId);
		}

		stockRoomBrowserIds.computeIfAbsent(newStockCode, k -> new CopyOnWriteArrayList<>()).add(browserSessionId);
		browserCurrentRoom.put(browserSessionId, newStockCode);

		return Response.success(oldStockCode);
	}

	@Override
	@Async("sseChartPushExecutor")
	@EventListener
	public void handleRealtimeStockEvent(RealtimeStockEvent event) {
		StockPriceDTO currentDto = event.stockPriceDto();
		if (currentDto == null || currentDto.getStockCode() == null)
			return;

		String stockCode = currentDto.getStockCode();
		List<String> roomUuidList = stockRoomBrowserIds.get(stockCode);
		if (roomUuidList == null || roomUuidList.isEmpty())
			return;

		long currentTime = System.currentTimeMillis();
		long lastPushTime = lastPushTimeMap.getOrDefault(stockCode, 0L);

		String currentMinuteStr = currentDto.getDateTime().format(MINUTE_FORMATTER);
		StockPriceDTO blockedDto = lastBlockedDtoMap.get(stockCode);

		// 🚨 [분 변경 감지 Bypass]: 분이 교체되었는데 과거 낙오 유산이 있다면 최종 마감 종가 강제 푸시!
		if (blockedDto != null) {
			String blockedMinuteStr = blockedDto.getDateTime().format(MINUTE_FORMATTER);
			if (!currentMinuteStr.equals(blockedMinuteStr)) {
				broadcastMessage(roomUuidList, blockedDto);
				lastBlockedDtoMap.remove(stockCode);
				lastPushTime = 0L;
			}
		}

		// 🚨 평상시 500ms 트래픽 제한 가드
		if (currentTime - lastPushTime < 500) {
			lastBlockedDtoMap.put(stockCode, currentDto);
			return;
		}

		lastPushTimeMap.put(stockCode, currentTime);
		lastBlockedDtoMap.remove(stockCode);

		broadcastMessage(roomUuidList, currentDto);
	}

	/**
	 * 🌟 차트 커스텀 포맷 배열화 및 JSON 직렬화 공통 메서드
	 */
	private void broadcastMessage(List<String> roomUuidList, StockPriceDTO dto) {
		try {
			// 1. LocalDateTime을 타임존(아시아/서울) 기준의 밀리초(Timestamp)로 변환
			long epochMilli = dto.getDateTime().atZone(SEOUL_ZONE).toInstant().toEpochMilli();

			// 2. 거래량(Volume)까지 포함하여 배열 생성 [시, 고, 저, 종, 거래량]
			long[] priceAndVolumeArray = new long[] { dto.getPriceOpen(), dto.getPriceHigh(), dto.getPriceLow(),
					dto.getPriceClose(), dto.getVolume() };

			// 3. 차트용 단일 데이터 조립 [밀리초, [시, 고, 저, 종, 거래량]]
			Object[] singleData = new Object[] { epochMilli, priceAndVolumeArray };

			// 💡 데이터 변환 후 무거운 JSON 직렬화 연산은 단 1번만 수행
			String jsonString = objectMapper.writeValueAsString(singleData);

			for (String browserId : roomUuidList) {
				SseEmitter emitter = browserEmitters.get(browserId);
				if (emitter != null) {
					try {
						emitter.send(SseEmitter.event().name("chart-tick").data(jsonString));
					} catch (Exception e) {
						emitter.complete();
						cleanSession(browserId);
					}
				}
			}
		} catch (Exception e) {
			log.error("SSE 직렬화 및 차트 데이터 브로드캐스팅 중 오류 발생", e);
		}
	}

	/**
	 * 유령 세션 청소 방어선
	 */
	private void cleanSession(String browserSessionId) {
		if (browserSessionId == null)
			return;

		browserEmitters.remove(browserSessionId);
		String stockCode = browserCurrentRoom.remove(browserSessionId);

		if (stockCode != null && stockRoomBrowserIds.containsKey(stockCode)) {
			List<String> uuidList = stockRoomBrowserIds.get(stockCode);
			uuidList.remove(browserSessionId);
			if (uuidList.isEmpty()) {
				stockRoomBrowserIds.remove(stockCode);
				lastPushTimeMap.remove(stockCode);
				lastBlockedDtoMap.remove(stockCode);
			}
		}
	}
}
