package com.frade.service.order.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.frade.common.order.TradeOptionCommon;
import com.frade.dto.event.PriceExecutedEvent;
import com.frade.dto.event.RealtimeStockEvent;
import com.frade.dto.order.OrderInfoDTO;
import com.frade.dto.stock.StockPriceDTO;
import com.frade.service.order.LimitOrderMatchingService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service // 💡 구현체에 서비스 간판을 달아줍니다.
public class LimitOrderMatchingServiceImpl implements LimitOrderMatchingService {

	@Autowired
	ApplicationEventPublisher eventPublisher;

	// 종목별 매수/매도 장부 격리
	private final ConcurrentMap<String, ConcurrentNavigableMap<Integer, List<OrderInfoDTO>>> bidBooks = new ConcurrentHashMap<>();
	private final ConcurrentMap<String, ConcurrentNavigableMap<Integer, List<OrderInfoDTO>>> askBooks = new ConcurrentHashMap<>();

	/**
	 * 📥 유저 주문 등록 입구 메서드 (HTTP 스레드가 직접 실행)
	 */
	@Override
	public void registerOrder(OrderInfoDTO newOrder) {
		try {
			String stockCode = newOrder.getStockCode();

			ConcurrentNavigableMap<Integer, List<OrderInfoDTO>> bidBook = bidBooks.computeIfAbsent(stockCode,
					k -> new ConcurrentSkipListMap<>(Collections.reverseOrder()));
			ConcurrentNavigableMap<Integer, List<OrderInfoDTO>> askBook = askBooks.computeIfAbsent(stockCode,
					k -> new ConcurrentSkipListMap<>());

			if (newOrder.getTradeOption() == TradeOptionCommon.BUY) {
				registerToBook(newOrder, bidBook);
			} else {
				registerToBook(newOrder, askBook);
			}
		} catch (Exception e) {
			log.error("❌ [주문 등록 에러] 유저: {} 주문 처리 중 예외 발생: {}", newOrder.getUserNum(), e.getMessage(), e);
		}
	}

	private void registerToBook(OrderInfoDTO order, ConcurrentNavigableMap<Integer, List<OrderInfoDTO>> book) {
		book.computeIfAbsent(order.getOrderPrice(), k -> Collections.synchronizedList(new ArrayList<>())).add(order);
		log.info("[{} 장부 대기 등록] 유저: {} | 가격: {}원 | 수량: {}개", order.getStockCode(), order.getUserNum(),
				order.getOrderPrice(), order.getOrderCount());
	}

	/**
	 * 🌪️ 실시간 시세 이벤트 리스너 (웹소켓 수신 스레드가 발행 -> 비동기 워커 풀이 처리)
	 */
	@Override
	@Async("matchingEngineExecutor")
	@EventListener
	public void handleRealtimeStockEvent(RealtimeStockEvent event) {
		StockPriceDTO dto = event.stockPriceDto();
		String stockCode = dto.getStockCode();
		int eventPrice = dto.getPriceClose();

		try {
			checkAndMatchWaitingOrders(stockCode, eventPrice);
		} catch (Exception e) {
			log.error("❌ [시세 매칭 에러] {} 처리 중 예외 발생: {}", stockCode, e.getMessage(), e);
		}
	}

	private void checkAndMatchWaitingOrders(String stockCode, int currentPrice) {
		ConcurrentNavigableMap<Integer, List<OrderInfoDTO>> bidBook = bidBooks.get(stockCode);
		ConcurrentNavigableMap<Integer, List<OrderInfoDTO>> askBook = askBooks.get(stockCode);

		if (bidBook != null && !bidBook.isEmpty()) {
			NavigableMap<Integer, List<OrderInfoDTO>> matchableBids = bidBook.headMap(currentPrice, true);
			triggerBulkExecution(stockCode, matchableBids, "매수 대기 물량 시세 체결");
		}

		if (askBook != null && !askBook.isEmpty()) {
			NavigableMap<Integer, List<OrderInfoDTO>> matchableAsks = askBook.headMap(currentPrice, true);
			triggerBulkExecution(stockCode, matchableAsks, "매도 대기 물량 시세 체결");
		}
	}

	private void triggerBulkExecution(String stockCode, NavigableMap<Integer, List<OrderInfoDTO>> matchableSubBook,
			String cause) {
		if (matchableSubBook.isEmpty())
			return;

		Iterator<Map.Entry<Integer, List<OrderInfoDTO>>> entryIterator = matchableSubBook.entrySet().iterator();

		while (entryIterator.hasNext()) {
			Map.Entry<Integer, List<OrderInfoDTO>> entry = entryIterator.next();
			List<OrderInfoDTO> makerQueue = entry.getValue();

			synchronized (makerQueue) {
				for (OrderInfoDTO makerOrder : makerQueue) {
					log.info("✨ [{}] 시세 도달 일괄 체결 완료 | 원인: {} | 가격: {}원 | 유저: {} | 수량: {}개", stockCode, cause,
							entry.getKey(), makerOrder.getUserNum(), makerOrder.getOrderCount());
					eventPublisher.publishEvent(new PriceExecutedEvent(makerOrder));
				}
			}
			entryIterator.remove();
		}
	}
}
