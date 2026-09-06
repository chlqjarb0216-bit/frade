package com.frade.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;

import com.frade.service.stock.StockService;
import com.frade.util.MarketUtil;
import com.frade.websocket.KiwoomWebSocketClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiScheduler {

	@Autowired
	StockService stockService;

	@Autowired
	KiwoomWebSocketClient kiwoomWebSocketClient;

	//장 시작전 전체 종목 상태 갱신
	@Scheduled(cron = "0 40 8 * * MON-FRI")
	public void preMarketTask() {
		int result = stockService.updateStockInfoList();
		log.info("preMarketTask 작업 완료 {}건", result);
		if (result < 100) {
			log.warn("작업완료된 건수 미달. 현재 작업 완료된 건수: {}건. 확인요망", result);
		}
	}

	@Scheduled(cron = "0 50 8 * * MON-FRI")
	public void startWebsocket() {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("웹소켓 시동");
		kiwoomWebSocketClient.boot();
	}

	@Scheduled(cron = "0 40 15 * * MON-FRI")
	public void stopWebsocket() {
		log.info("장 마감. 소켓 차단");
		kiwoomWebSocketClient.close();
		log.info("토큰 폐기");
		stockService.revokeApiToken();
	}

	@EventListener
	public void handleContextRefresh(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() != null) {
			return; // 중복 호출 방지
		}
		// 이미 만들어둔 데몬 스레드 스케줄러를 활용해 비동기로 시퀀스를 틀어줍니다.
		kiwoomWebSocketClient.getReconnectScheduler().execute(() -> {
			executeStartupSequence();
		});
	}

	//순서 보장 초기화 시퀀스
	private void executeStartupSequence() {
		try {
			// STEP 1: 메모리 캐시 초기화 (DB 조회 등 무거운 작업)
			log.info("[시퀀스 1/3] 메모리 캐시 로드 시작...");
			stockService.initMemoryCache();

			// 🌟 [추가 STEP]: 오라클 DB에서 오늘 오전 분 봉 이력을 가져와 캐시판 데우기 (Warm-up)
			log.info("[시퀀스 2/3] 오라클 DB 기반 2일 치 장중 시세 웜업 시작...");
			String[] last2Days = MarketUtil.getLast2MarketDayString();
			stockService.warmUpStockPriceMemoryCache(last2Days[0], last2Days[1]);

			//장 시간 이면
			if (MarketUtil.isMarketOpenTime()) {
				// STEP 3: 웹소켓 부팅 (캐시가 완료된 후 안전하게 가동)
				log.info("[시퀀스 2/3] 웹소켓 클라이언트 시동");
				kiwoomWebSocketClient.boot();
			}

		} catch (Exception e) {
			log.error("❌ 가동 시퀀스 실행 중 치명적 에러 발생", e);
		}
	}

}
