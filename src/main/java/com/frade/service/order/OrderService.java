package com.frade.service.order;

import java.util.List;

import com.frade.dto.event.PriceExecutedEvent;
import com.frade.dto.event.RealtimeStockEvent;
import com.frade.dto.order.HistoryDTO;
import com.frade.dto.order.OrderInfoDTO;
import com.frade.dto.user.UserCashDTO;

public interface OrderService {

	// 시장가 매수 로직
	public boolean processMarketBuy(OrderInfoDTO orderInfo);
	// 시장가 매도 로직
	public boolean processMarketSell(OrderInfoDTO orderInfo);

	// 지정가 매수 저장
	public boolean saveLimitBuy(OrderInfoDTO orderInfo);
	// 지정가 매도 저장
	public boolean saveLimitSell(OrderInfoDTO orderInfo);


	// 시장가 주문 저장
	public boolean saveMarketPrice(OrderInfoDTO orderInfo);

	public UserCashDTO findUserCashByUserNum(int userNum);

	public List<HistoryDTO> findTradeHistoryByUserNum(int userNum);

}
