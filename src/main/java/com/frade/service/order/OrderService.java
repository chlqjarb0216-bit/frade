package com.frade.service.order;

import com.frade.dto.order.OrderInfoDTO;


public interface OrderService {
	
	//매수 검증
	public boolean processBuy(OrderInfoDTO orderInfo); 
	//매도 검증
	public boolean processSell(OrderInfoDTO orderInfo); 
	//시장가 주문 저장
	public boolean saveMarketPrice(OrderInfoDTO orderInfo); 
	
}
