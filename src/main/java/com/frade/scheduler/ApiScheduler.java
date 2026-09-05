package com.frade.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.frade.service.stock.StockService;
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

}
