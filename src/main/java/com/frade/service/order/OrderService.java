package com.frade.service.order;

import java.util.List;

import com.frade.dto.order.HistoryDTO;
import com.frade.dto.order.OrderInfoDTO;
import com.frade.dto.user.UserCashDTO;


public interface OrderService {
	
	//매수 검증
	public boolean processBuy(OrderInfoDTO orderInfo); 
	//매도 검증
	public boolean processSell(OrderInfoDTO orderInfo); 
	//시장가 주문 저장
	public boolean saveMarketPrice(OrderInfoDTO orderInfo);
	
	public UserCashDTO findUserCashByUserNum(int userNum);
	
	public List<HistoryDTO> findTradeHistoryByUserNum(int userNum);
	
}
