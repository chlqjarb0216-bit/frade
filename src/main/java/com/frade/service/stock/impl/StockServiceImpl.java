package com.frade.service.stock.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frade.common.stock.StockCommonFinalString;
import com.frade.common.stock.StockSector;
import com.frade.dao.stock.StockDAO;
import com.frade.dao.stock.StockPriceDAO;
import com.frade.dto.stock.StockInfoDTO;
import com.frade.dto.stock.StockPreviewDTO;
import com.frade.dto.stock.StockPriceDTO;
import com.frade.memcache.StockMemoryCache;
import com.frade.memcache.StockPriceMemoryCache;
import com.frade.memcache.StockRankingCache;
import com.frade.service.api.KiwoomApiService;
import com.frade.service.stock.StockDataBufferService;
import com.frade.service.stock.StockService;
import com.frade.util.MarketUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StockServiceImpl implements StockService {

	@Autowired
	KiwoomApiService kiwoomApiService;
	@Autowired
	StockDataBufferService stockDataBufferService;

	@Autowired
	StockDAO stockDAO;
	@Autowired
	StockPriceDAO stockPriceDAO;

	@Autowired
	StockMemoryCache stockMemoryCache;
	@Autowired
	StockRankingCache stockRankingCache;
	@Autowired
	StockPriceMemoryCache stockPriceMemoryCache;

	@Autowired
	ObjectMapper objectMapper;

	@Override
	public List<StockInfoDTO> searchStockByName(String stockName) {
		return stockMemoryCache.searchByName(stockName);
	}

	@Override
	public List<String> getStockCodeList() {
		return stockMemoryCache.getStockCodeList();
	}

	@Override
	public StockPreviewDTO getStockPreviewByStockCode(String stockCode) {
		return stockRankingCache.getPreviewByStockCode(stockCode);
	}

	@Override
	public List<StockPreviewDTO> getSortedStockRankingListPage(int pageIdx, int pageSize) {
		return stockRankingCache.getSortedCachePage(pageIdx, pageSize);
	}

	@Override
	public void refreshAndSwapRealtimeRankingCache() {
		//현재가를 버퍼에서 맵으로 가져옴
		Map<String, Integer> priceSnapshots = stockDataBufferService.getMinPriceSnapshotMap();
		if (priceSnapshots.isEmpty())
			return;

		//갈아끼울 새 맵 작성
		Map<String, StockPreviewDTO> tempMap = new HashMap<>();
		//정렬
		List<StockPreviewDTO> previewList = stockMemoryCache.getAllStockList().stream().map(info -> {
			if (info.getPrevDayClosePrice() <= 0)
				return null;
			Integer currentPrice = priceSnapshots.get(info.getStockCode());
			if (currentPrice == null) {
				StockPreviewDTO prevCache = stockRankingCache.getPreviewByStockCode(info.getStockCode());
				currentPrice = prevCache != null ? prevCache.getPrice() : 0;
				if (currentPrice <= 0) {
					currentPrice = info.getPrevDayClosePrice();
				}
			}

			// 화면 출력용 객체 조립
			StockPreviewDTO previewDTO = new StockPreviewDTO(info.getStockCode(), info.getStockName(),
					StockSector.getSectorName(info.getSectorNum()), currentPrice, info.getPrevDayClosePrice());

			tempMap.put(info.getStockCode(), previewDTO);
			return previewDTO;
		}).filter(Objects::nonNull)
				.sorted((o1, o2) -> Double.compare(o2.getDailyPriceChangeRate(), o1.getDailyPriceChangeRate())) // dailyChangeRate(당일 등락률) 기준 내림차순 정렬
				.toList();
		stockRankingCache.updateSortedCache(previewList, tempMap);
	}

	@Override
	public void putStockPriceListToStockPriceMemoryCache(String stockCode, String dayString,
			List<StockPriceDTO> priceList) {
		stockPriceMemoryCache.putAll(stockCode, dayString, priceList);
	}

	@Override
	public String getEveryChartDataCached(String stockCode) {

		List<StockPriceDTO> cacheData = stockPriceMemoryCache.getEveryChartDataCached(stockCode);
		List<Object[]> chartDataList = new ArrayList<>();

		for (StockPriceDTO dto : cacheData) {
			// 1. LocalDateTime을 타임존(아시아/서울) 기준의 밀리초(Timestamp)로 변환
			long epochMilli = dto.getDateTime().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();

			// 💡 거래량(Volume)까지 포함하여 배열 생성 [시, 고, 저, 종, 거래량]
			long[] priceAndVolumeArray = new long[] { dto.getPriceOpen(), dto.getPriceHigh(), dto.getPriceLow(),
					dto.getPriceClose(), dto.getVolume() // 거래량 추가
			};

			// 3. 차트용 단일 데이터 조립 [밀리초, [시, 고, 저, 종]]
			Object[] singleData = new Object[] { epochMilli, priceAndVolumeArray };

			chartDataList.add(singleData);
		}
		String jsonString = "[]";
		try {
			jsonString = objectMapper.writeValueAsString(chartDataList);
		} catch (JsonProcessingException e) {
			log.error("json가공중 에러");
		}

		return jsonString;
	}

	@Override
	public void flushCompletedMinuteBufferAndSave() {
		// 1. 인메모리 버퍼 팩토리에서 마감된 데이터 리스트 제로카피로 안전 인양
		List<StockPriceDTO> completedList = stockDataBufferService.flushCompleteMinuteBuffer();

		// 2. [가드] 없으면 서비스 레이어 단에서 조용히 트랜잭션 종료
		if (completedList == null || completedList.isEmpty()) {
			return;
		}

		// 3. 오라클 레포지토리로 토스하여 원샷 벌크 인서트 때려박기
		int result = stockPriceDAO.insertMinuteStockPrice(completedList);

		// 💡 4. [최종 완성] 오라클 적재 성공 직후, 분리 상주 중인 대형 일자별 캐시에 동시 동기화 적재(put)
		String todayStr = LocalDate.now().format(MarketUtil.DATE_FORM);

		for (StockPriceDTO dto : completedList) {
			// 구조: Map<종목코드, Map<yyyyMMdd, List<StockPriceDTO>>> 공간에 차곡차곡 누적
			stockPriceMemoryCache.put(dto.getStockCode(), todayStr, dto);
		}

		log.info("분단위 DB저장 완료. 기준시간: {} ➔ 총 {}건 오라클 적재 및 메모리캐시 갱신 성공", completedList.get(0).getDateTime(), result);
	}

	@Override
	public List<StockPriceDTO> selectMinuteStockPriceListByStockCodeAndDayString(String stockCode, String dayString) {
		return stockPriceDAO.selectMinuteStockPriceListByStockCodeAndDayString(stockCode, dayString);
	}

	@Override
	public void revokeApiToken() {
		kiwoomApiService.revokeToken();
	}

	@Override
	public int updateStockInfoList() {
		List<StockInfoDTO> infoList = kiwoomApiService.getMarketAllStockInfo();

		// 1. XML 문자열 조립
		StringBuilder xmlBuilder = new StringBuilder(infoList.size() * 300);
		xmlBuilder.append("<rows>");
		for (StockInfoDTO dto : infoList) {
			xmlBuilder.append("<row>").append("<stockCode>").append(dto.getStockCode()).append("</stockCode>")
					.append("<stockName><![CDATA[").append(dto.getStockName()).append("]]></stockName>")
					.append("<sectorNum>").append(dto.getSectorNum()).append("</sectorNum>").append("<stockStatus>")
					.append(dto.getStockStatus()).append("</stockStatus>").append("<prevDayClosePrice>")
					.append(dto.getPrevDayClosePrice()).append("</prevDayClosePrice>").append("</row>");
		}
		xmlBuilder.append("</rows>");

		// 2. DB 작업
		List<String> updatedCodes = stockDAO.updateAllStockAndReturnMatchedCodeList(xmlBuilder.toString());

		// 3. 100종목 검열
		if (updatedCodes != null && !updatedCodes.isEmpty()) {
			Set<String> updatedCodeSet = new HashSet<>(updatedCodes);

			List<StockInfoDTO> filteredCacheList = infoList.stream()
					.filter(dto -> updatedCodeSet.contains(dto.getStockCode())).toList();

			stockMemoryCache.refreshCache(filteredCacheList);

			return filteredCacheList.size();
		}
		return 0;
	}

	@Override
	public void updateKOSPIChartData(String nowDateString) {
		LocalDateTime lastData = stockPriceMemoryCache.getLatestDataTime(StockCommonFinalString.KOSPI, nowDateString);
		List<StockPriceDTO> updateChartData = kiwoomApiService.getKOSPIChartDataFromLastData(lastData);
		stockPriceMemoryCache.putOrUpdate(StockCommonFinalString.KOSPI, nowDateString, updateChartData);
	}

	@Override
	public void updateKOSPIChartDataToDB(String nowDateString) {
		List<StockPriceDTO> dailyPriceList = stockPriceMemoryCache.getChartData(StockCommonFinalString.KOSPI,
				nowDateString);
		if (dailyPriceList == null || dailyPriceList.isEmpty())
			return;
		int result = stockPriceDAO.updateOrInsertDailyMinutePrice(dailyPriceList);
		log.info("장마감 오늘({}) 코스피지수 DB업데이트 완료: {}건", nowDateString, result);
	}

	@Override
	public void initMemoryCache() {
		stockMemoryCache.initStockCache();
	}

	@Override
	public void warmUpStockPriceMemoryCache(String todayStr, String yesterdayStr) {
		List<String> activeStockCodes = this.getStockCodeList();
		if (activeStockCodes == null || activeStockCodes.isEmpty()) {
			return;
		}

		int loadedCount = 0;
		boolean isBeforeMarket = LocalDateTime.now().getHour() < 9; // 💡 상단에서 시간 조건 플래그 1방에 추출

		// 🌟 지저분한 이중 루프를 박멸하고 단 하나의 클린한 종목 순회 루프로 대통합
		for (String stockCode : activeStockCodes) {
			try {
				// [공통 인양]: 요일 불문하고 오라클 DB에 상주 중인 이틀 치 데이터를 우선 0초 만에 인양
				List<StockPriceDTO> yesterdayDbBars = stockPriceDAO
						.selectMinuteStockPriceListByStockCodeAndDayString(stockCode, yesterdayStr);
				List<StockPriceDTO> todayDbBars = stockPriceDAO
						.selectMinuteStockPriceListByStockCodeAndDayString(stockCode, todayStr);

				if (!MarketUtil.isMarketDay() || isBeforeMarket) {
					/* ☀️ [시나리오 A]: 장 시작 전 부팅 (00:00 ~ 08:59) ➔ 어제 자 철통 검증 */
					if (!yesterdayDbBars.isEmpty()) {
						stockPriceMemoryCache.putAll(stockCode, yesterdayStr, yesterdayDbBars);
					} else {
						// 🚨 장전 어제 자 DB 0건 유실 참사 가드 작동
						log.warn("[{} 장전 유실 감지] 장 개막 전 부팅되었으나 직전 영업일({}) DB가 0건입니다. API 복구를 트리거합니다.", stockCode,
								yesterdayStr);

						List<StockPriceDTO> apiStockYesterdayBars = kiwoomApiService.getStockChartDataByDay(stockCode,
								yesterdayStr);
						if (apiStockYesterdayBars != null && !apiStockYesterdayBars.isEmpty()) {
							stockPriceMemoryCache.putOrUpdate(stockCode, yesterdayStr, apiStockYesterdayBars);
							log.info("데이터 {}", apiStockYesterdayBars.toString());
							stockPriceDAO.updateOrInsertDailyMinutePrice(apiStockYesterdayBars);
							log.info("[{} 장전 복구 성공] 직전 영업일 이력 복원 및 영속화 종결", stockCode);

							// ⏳ [핵심 트래픽 가드]: 대량 종목 연달아 API 난사 시 키움 게이트웨이 차단(Blocking) 방지
							Thread.sleep(150);
						}
					}
				} else {
					/* 🚀 [시나리오 B]: 장 시작 이후 부팅 (09:00 ~ 23:59) ➔ 오늘 자 철통 검증 */
					if (!yesterdayDbBars.isEmpty()) {
						stockPriceMemoryCache.putAll(stockCode, yesterdayStr, yesterdayDbBars);
					}

					if (!todayDbBars.isEmpty()) {
						stockPriceMemoryCache.putAll(stockCode, todayStr, todayDbBars);
					} else {
						// 🚨 장중 오늘 자 크래시 유실 가드 작동
						log.warn("[{} 장중 유실 감지] 아침 9시 이후 부팅되었으나 오늘 자 DB가 0건입니다. API 복구를 트리거합니다.", stockCode);

						List<StockPriceDTO> apiStockTodayBars = kiwoomApiService.getStockChartDataByDay(stockCode,
								todayStr);
						if (apiStockTodayBars != null && !apiStockTodayBars.isEmpty()) {
							stockPriceMemoryCache.putOrUpdate(stockCode, todayStr, apiStockTodayBars);
							stockPriceDAO.updateOrInsertDailyMinutePrice(apiStockTodayBars);
							log.info("[{} 장중 복구 성공] 오늘 자 실시간 누적 이력 복원 및 영속화 종결", stockCode);

							// ⏳ [핵심 트래픽 가드]: 증권사 방화벽 세션 차단 예방 휴식 추가
							Thread.sleep(150);
						}
					}
				}
				loadedCount++;

			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
				log.error("❌ 웜업 인터럽트 발생으로 복구 시퀀스 강제 중단", ie);
				break;
			} catch (Exception e) {
				// 💡 [실전 인프라 가드]: 특정 한 종목 통신 중 에러가 나도, 전체 복구 루프가 무너지지 않고 다음 종목으로 전진!
				log.error("❌ [{}] 종목 웜업 복구 중 예외 발생 (가동 유지): {}", stockCode, e.getMessage());
			}
		}

		log.info("🏁 [전 종목 웜업 최종 마감] 오늘({}) 및 직전 영업일({}) 이틀 치 오라클-인메모리 파이프라인 정렬 완료 (총 {}개 종목 복원 완료)", todayStr,
				yesterdayStr, loadedCount);
		// =========================================================================

		// [파트 2]: 🌟 KOSPI 지수 전용 장전/장후 타깃 스위칭 복구 엔진 가동

		// 1단계: 오라클 DB에서 어제 자와 오늘 자 지수 데이터를 일단 긁어옵니다.
		List<StockPriceDTO> kospiYesterdayDbBars = stockPriceDAO
				.selectMinuteStockPriceListByStockCodeAndDayString(StockCommonFinalString.KOSPI, yesterdayStr);
		List<StockPriceDTO> kospiTodayDbBars = stockPriceDAO
				.selectMinuteStockPriceListByStockCodeAndDayString(StockCommonFinalString.KOSPI, todayStr);

		// 2단계: 현재 시각에 따라 '진짜 살아있어야 정상인 기준 날짜'를 타깃으로 스위칭 가드 가동
		if (MarketUtil.isMarketDay() || isBeforeMarket) {
			/* ☀️ [시나리오 A]: 장 시작 전 부팅 (00:00 ~ 08:59) */
			if (!kospiYesterdayDbBars.isEmpty()) {
				// 어제 자 데이터가 DB에 잘 들어있다면 정상 웜업 적재
				stockPriceMemoryCache.putAll(StockCommonFinalString.KOSPI, yesterdayStr, kospiYesterdayDbBars);
			} else {
				// 🚨 [장전 비상 구출]: 장 시작 전인데 어제(직전 영업일) 자 데이터마저 DB에 0건으로 비어있다면!
				log.warn("[KOSPI 장전 유실 감지] 장 시작 전 부팅되었으나, 직전 영업일({}) DB 데이터가 0건입니다! API 구출을 기동합니다.", yesterdayStr);

				// 어제 자 아침 00:00:00 기점 마커를 들고 가 키움 API를 때려 어제 하루치 전체 뭉탱이를 강제 인양합니다.
				List<StockPriceDTO> apiKospiYesterdayBars = kiwoomApiService.getKOSPIChartDataByDay(yesterdayStr);

				if (apiKospiYesterdayBars != null && !apiKospiYesterdayBars.isEmpty()) {
					stockPriceMemoryCache.putOrUpdate(StockCommonFinalString.KOSPI, yesterdayStr,
							apiKospiYesterdayBars);
					stockPriceDAO.updateOrInsertDailyMinutePrice(apiKospiYesterdayBars);
					log.info("[KOSPI 장전 복구 성공] 직전 영업일 지수 {}건을 캐시판에 보정 주입 완료", apiKospiYesterdayBars.size());
				}
			}
			// 장전이므로 오늘 자 빈 방은 당연히 0건인 채로 조용히 통과합니다.

		} else {
			/* 🚀 [시나리오 B]: 장 시작 이후 부팅 (09:00 ~ 23:59) */
			// 어제 자는 밀려 들어온 정상 데이터가 있을 확률이 높으니 가볍게 깔아둡니다.
			if (!kospiYesterdayDbBars.isEmpty()) {
				stockPriceMemoryCache.putAll(StockCommonFinalString.KOSPI, yesterdayStr, kospiYesterdayDbBars);
			}

			if (!kospiTodayDbBars.isEmpty()) {
				// 장중 재부팅 시 오늘 자 적재본이 정상 상주 중이라면 그대로 캐시판 스왑
				stockPriceMemoryCache.putAll(StockCommonFinalString.KOSPI, todayStr, kospiTodayDbBars);
			} else {
				// 🚨 [장중 비상 구출]: 장 개막 이후인데 오늘 자 데이터가 DB에 0건으로 통째로 낙오되어 있다면!
				log.warn("[KOSPI 장중 유실 감지] 아침 9시 이후 부팅되었으나 오늘 자 DB 데이터가 0건입니다! API 구출을 기동합니다.");

				// 오늘 자 아침 00:00:00 기점 마커를 들고 가 오늘 아침 9시부터 현재 찰나까지 쌓인 라이브 누적 세트를 인양합니다.
				List<StockPriceDTO> apiKospiTodayBars = kiwoomApiService.getKOSPIChartDataByDay(todayStr);

				if (apiKospiTodayBars != null && !apiKospiTodayBars.isEmpty()) {
					// 역순 스캔 덮어쓰기 엔진으로 최신 정산가 오버라이딩 복구 완료
					stockPriceMemoryCache.putOrUpdate(StockCommonFinalString.KOSPI, todayStr, apiKospiTodayBars);
					stockPriceDAO.updateOrInsertDailyMinutePrice(apiKospiTodayBars);
					log.info("[KOSPI 장중 복구 성공] 오늘 자 누적 지수 {}건을 캐시판에 오버라이딩 강제 적재 완료!", apiKospiTodayBars.size());
				}
			}
		}

		log.info("KOSPI 지수 장전/장후 이중화 스위칭 웜업 파이프라인 최종 마감 완결");
	}

	@Override
	public void clearOldStockPriceCache(String dayString) {
		stockPriceMemoryCache.clearOldDate(dayString);
	}
}
