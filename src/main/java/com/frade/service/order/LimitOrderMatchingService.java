package com.frade.service.order;

import com.frade.dto.event.RealtimeStockEvent;
import com.frade.dto.order.OrderInfoDTO;

public interface LimitOrderMatchingService {
	// 1. 유저의 지정가 주문을 장부에 대기 등록하는 입구
	void registerOrder(OrderInfoDTO newOrder);

	// 2. 외부 진짜 거래소 웹소켓 시세를 리슨하여 일괄 체결을 트리거하는 입구
	void handleRealtimeStockEvent(RealtimeStockEvent event);
}
