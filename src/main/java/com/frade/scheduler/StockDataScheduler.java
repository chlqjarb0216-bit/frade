package com.frade.scheduler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.frade.common.stock.StockSector;
import com.frade.dto.stock.StockPreviewDTO;
import com.frade.dto.stock.StockPriceDTO;
import com.frade.memcache.StockMemoryCache;
import com.frade.memcache.StockRankingCache;
import com.frade.service.stock.StockDataBufferService;
import com.frade.service.stock.StockService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StockDataScheduler {
	@Autowired
	private StockDataBufferService stockDataBufferService;
	@Autowired
	private StockService stockService;
	@Autowired
	private StockMemoryCache stockMemoryCache;
	@Autowired
	private StockRankingCache stockRankingCache;

	//10초마다 버퍼의 큐를 확인해서 데이터가 들어있으면 DB에 저장
	@Scheduled(fixedDelay = 10000)
	public void pollCompletedMinuteBatch() {
		try {
			List<StockPriceDTO> completedList = stockDataBufferService.flushCompleteMinuteBuffer();

			//없으면 종료
			if (completedList.isEmpty()) {
				return;
			}

			// 💡 큐에 신호가 툭 떨어져서 리스트를 낚아챈 바로 그 10초 주기 안에서, 
			// 지연 없이 징검다리 서비스 레이어를 호출해 DB에 벌크로 즉시 적재(Upsert)합니다!
			stockService.saveMinuteStockPrice(completedList);

		} catch (Exception e) {
			log.error("분단위 버퍼 저장 에러\n 원인: {}", e.getMessage(), e);
		}
	}

	//1초마다 전체 종목을 매핑하여 경량 StockPreviewDTO 목록으로 정리한 뒤 정렬
	@Scheduled(fixedDelay = 1000)
	public void refreshRealtimeRanking() {
		//장이 닫혀있으면 종료
		if (!isMarketOpenTime()) {
			return;
		}

		try {
			//현재가를 버퍼에서 맵으로 가져옴
			Map<String, Integer> priceSnapshots = stockDataBufferService.getMinPriceSnapshotMap();
			if (priceSnapshots.isEmpty())
				return;

			//갈아끼울 새 맵 작성
			Map<String, StockPreviewDTO> tempMap = new HashMap<>();
			//정렬
			List<StockPreviewDTO> previewList = stockMemoryCache.getAllStockList().stream().map(info -> {
				Integer currentPrice = priceSnapshots.get(info.getStockCode());
				if (currentPrice == null) {
					StockPreviewDTO prevCache = stockRankingCache.getPreviewByStockCode(info.getStockCode());
					currentPrice = prevCache != null ? prevCache.getPrice() : 0;
					if (currentPrice <= 0) {
						currentPrice = info.getPrevDayClosePrice();
					}
				}

				if (info.getPrevDayClosePrice() <= 0)
					return null;

				// 화면 출력용 객체 조립
				StockPreviewDTO previewDTO = new StockPreviewDTO(info.getStockCode(), info.getStockName(),
						StockSector.getSectorName(info.getSectorNum()), currentPrice, info.getPrevDayClosePrice());

				tempMap.put(info.getStockCode(), previewDTO);
				return previewDTO;
			}).filter(Objects::nonNull)
					.sorted((o1, o2) -> Double.compare(o2.getDailyPriceChangeRate(), o1.getDailyPriceChangeRate())) // dailyChangeRate(당일 등락률) 기준 내림차순 정렬
					.collect(Collectors.toList());

			// 종목 순위 갱신
			stockRankingCache.updateSortedCache(previewList, tempMap);

		} catch (Exception e) {
			log.warn("종목 순위 갱신중 에러\n {}", e.getMessage());
		}
	}

	//평일(월~금)이면서 시각이 09:00:00 ~ 15:30:00 사이인지 판정
	private boolean isMarketOpenTime() {
		LocalDateTime now = LocalDateTime.now();
		java.time.DayOfWeek day = now.getDayOfWeek();

		//토요일이거나 일요일이면 장이 닫혔으므로 탈락
		if (day == java.time.DayOfWeek.SATURDAY || day == java.time.DayOfWeek.SUNDAY) {
			return false;
		}

		//시간과 분을 이어붙여 직관적인 크기 비교
		int hhmm = now.getHour() * 100 + now.getMinute();

		// 09시 00분부터 15시 30분 사이일 때만 True 반환!
		return hhmm >= 900 && hhmm <= 1530;
	}
}
