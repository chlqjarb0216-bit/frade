package com.frade.scheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.frade.service.stock.StockService;
import com.frade.util.MarketUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StockDataScheduler {
	@Autowired
	private StockService stockService;

	//10초마다 버퍼의 큐를 확인해서 데이터가 들어있으면 DB에 저장
	@Scheduled(fixedDelay = 10000)
	public void pollCompletedMinuteBatch() {
		try {
			stockService.flushCompletedMinuteBufferAndSave();
		} catch (Exception e) {
			log.error("분단위 버퍼 저장 에러\n 원인: {}", e.getMessage(), e);
		}
	}

	//1초마다 전체 종목을 매핑하여 경량 StockPreviewDTO 목록으로 정리한 뒤 정렬
	@Scheduled(fixedDelay = 1000)
	public void refreshRealtimeRanking() {
		//장이 닫혀있으면 종료
		if (!MarketUtil.isMarketOpenTime()) {
			return;
		}

		try {
			stockService.refreshAndSwapRealtimeRankingCache();
		} catch (Exception e) {
			log.warn("종목 순위 갱신중 에러\n {}", e.getMessage());
		}
	}

	// 월요일부터 금요일까지(MON-FRI) 매일 새벽 00시 05분 00초에 정기 가동
	@Scheduled(cron = "0 5 0 ? * MON-FRI")
	public void clearOldStockPriceCacheScheduler() {
		try {
			stockService.setNowDateString(LocalDateTime.now().format(MarketUtil.DATE_FORM));

			// 어제날짜
			String yesterdayStr = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

			log.info("어제 자({}) 캐시 데이터 청소를 시작합니다.", yesterdayStr);

			// 서비스 레이어를 통해 그저께 날짜 방을 O(1)로 원샷 폭파 수거
			stockService.clearOldStockPriceCache(yesterdayStr);

		} catch (Exception e) {
			log.error("정기 캐시 청소 중 에러 발생", e);
		}
	}
}
