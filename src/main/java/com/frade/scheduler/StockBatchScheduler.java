package com.frade.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.frade.dto.stock.StockPriceDTO;
import com.frade.service.stock.StockDataBufferService;
import com.frade.service.stock.StockService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StockBatchScheduler {
	@Autowired
	private StockDataBufferService stockDataBufferService;
	@Autowired
	private StockService stockService;

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
}
