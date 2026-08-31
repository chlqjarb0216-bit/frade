package com.frade.service.order;

import com.frade.dto.order.HistoryDTO;
import com.frade.dto.order.OrderInfoDTO;
import com.frade.dto.user.PortfolioDTO;
import com.frade.dto.user.UserCashDTO;

public interface OrderService {
	
	public int processBuy(OrderInfoDTO orderInfo); //매수 검증
	public int processSell(OrderInfoDTO orderInfo); //매도 검증
	
	public int saveUserPortfolio(PortfolioDTO portfolio); //포트폴리오 저장
	public int saveTradeHistory(HistoryDTO history); //거래내역 저장
	public int saveUserCash(UserCashDTO userCash); //현금정보 저장
}
