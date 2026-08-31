package com.frade.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.frade.dto.stock.StockInfoDTO;
import com.frade.memcache.StockMemoryCache;
import com.frade.service.stock.StockService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiScheduler {

	@Autowired
	StockService stockService;

	//종목검색시 DB검색을 최소화하기 위한 메모리캐시
	@Autowired
	StockMemoryCache stockMemoryCache;


	//장 시작전 전체 종목 상태 갱신
	@Scheduled(cron = "0 40 8 * * *")
	public void preMarketTask() {

		List<StockInfoDTO> result = stockService.updateStockInfoList();
		stockMemoryCache.refreshCache(result);

		log.info("preMarketTask 작업 완료");
		if (result.size() < 100) {
			log.warn("작업완료된 건수 미달. 현재 작업 완료된 건수: {}건. 확인요망", result);
		}

		System.out.println("test");
	}

}
