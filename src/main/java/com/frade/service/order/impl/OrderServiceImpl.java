package com.frade.service.order.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frade.dao.order.OrderDAO;
import com.frade.dto.order.HistoryDTO;
import com.frade.dto.order.OrderInfoDTO;
import com.frade.dto.user.PortfolioDTO;
import com.frade.dto.user.UserCashDTO;
import com.frade.service.order.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderDAO orderDAO;
	
	@Override
	public int processBuy(OrderInfoDTO orderInfo) { //매수 검증

		//유저데이터 생성되면 DAO findByCashByUnum으로
		int userCash = 10000000;
		
		if(orderInfo.getOrderPrice() * orderInfo.getOrderCount() > userCash) {
			System.out.println("주문 금액이 보유 예치금보다 많음.");
			return 0;
		} else if(orderInfo.getOrderCount() <= 0) {
			System.out.println("주문 수량은 1 이상이어야 함.");
			return 0;
		} else if(orderInfo.getOrderPrice() <= 0) {
			System.out.println("주문 금액은 1 이상이어야 함.");
			return 0;
		}

		System.out.println("주문금액 : " + orderInfo.getOrderPrice() * orderInfo.getOrderCount());
		System.out.println("기존현금 : " + userCash);
		System.out.println("남은현금 : " + (userCash - (orderInfo.getOrderPrice() * orderInfo.getOrderCount())));
		
		System.out.println("삼성전자");
		System.out.println(orderInfo.getOrderCount() + "주");
		System.out.println("평균매입가" + orderInfo.getOrderPrice());
		
		return 1;
	}
	
	@Override
	public int processSell(OrderInfoDTO orderInfo){ //매도 검증
		
		int holdCtn = 30;
		
		if(orderInfo.getOrderCount() > holdCtn) {
			System.out.println("매도수량이 보유수량보다 많음");
			return 0;
		}else if(orderInfo.getOrderCount() <= 0) {
			System.out.println("매도수량은 0보다 커야함");
			return 0;
		}else if(orderInfo.getOrderPrice() <= 0) {
			System.out.println("매도가격은 0보다 커야함");
			return 0;
		}
		
		System.out.println("매도금액 : " + orderInfo.getOrderPrice());
		System.out.println("매도 주수 : " + orderInfo.getOrderCount());
		System.out.println("삼성전자");
		

		
		return 1;
	}

	@Override
	public int saveUserPortfolio(PortfolioDTO portfolio) {  //포트폴리오 저장
		int result = orderDAO.saveUserPortfolio(portfolio);
		return 0;
	}

	@Override
	public int saveTradeHistory(HistoryDTO history) {  //거래기록 저장
		int result = orderDAO.saveTradeHistory(history);
		return 0;
	}

	@Override
	public int saveUserCash(UserCashDTO userCash) {  //변동 현금정도 저장
		int result = orderDAO.saveUserCash(userCash);
		return 0;
	}
	

}
