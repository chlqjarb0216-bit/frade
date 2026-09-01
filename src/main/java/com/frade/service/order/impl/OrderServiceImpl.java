package com.frade.service.order.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frade.dao.portfolio.CashDAO;
import com.frade.dao.portfolio.HistoryDAO;
import com.frade.dao.portfolio.PortfolioDAO;
import com.frade.dto.order.HistoryDTO;
import com.frade.dto.order.OrderInfoDTO;
import com.frade.dto.user.PortfolioDTO;
import com.frade.dto.user.UserCashDTO;
import com.frade.service.order.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	PortfolioDAO orderDAO;
	@Autowired
	CashDAO cashDAO;
	@Autowired
	HistoryDAO historyDAO;

	@Override
	public boolean processBuy(OrderInfoDTO orderInfo) { // 매수
		
		int totalPrice = orderInfo.getOrderCount() * orderInfo.getOrderPrice();
		
		//임시 유저넘버
		int userNum = 1;

		UserCashDTO cash = findUserCashByUserNum(userNum);

		// 검증
		if (totalPrice > cash.getCash()) {
			System.out.println("주문 금액이 보유 예치금보다 많음.");
			return false;
		}


		// 거래기록 저장
		HistoryDTO history = new HistoryDTO();
		history.setStockCode(orderInfo.getStockCode());
		history.setUserNum(userNum);
		history.setTradePrice(orderInfo.getOrderPrice());
		history.setTradeCnt(orderInfo.getOrderCount());
		insertTradeHistory(history);
		
		// 현금정보 저장
		updateUserCash(userNum, -1 * totalPrice);
		
		
		
		
		// 포트폴리오 merge into
		PortfolioDTO portfolio = new PortfolioDTO();
		portfolio.setUserNum(userNum);
		portfolio.setStockCode(orderInfo.getStockCode());
		portfolio.setUserStockCnt(orderInfo.getOrderCount());
		portfolio.setUserBuyCost(orderInfo.getOrderCount() * orderInfo.getOrderPrice());
		

		updateOrInsertUserPortfolio(portfolio);
		

		return true;
	}

	@Override
	public boolean processSell(OrderInfoDTO orderInfo) { // 매도

		int totalPrice = orderInfo.getOrderCount() * orderInfo.getOrderPrice();
		
		//임시 유저넘버
		int userNum = 1;

		PortfolioDTO portfolio = findUserPortfolioByUserNumAndStockCode(userNum, orderInfo.getStockCode());

		// 검증
		if (portfolio == null) {
			System.out.println("해당 주식을 보유하고 있지 않음");
			return false;
		} else if (orderInfo.getOrderCount() > portfolio.getUserStockCnt()) {
			System.out.println("매도수량이 보유수량보다 많음");
			return false;
		} 

		// 거래기록 저장
		HistoryDTO history = new HistoryDTO();
		history.setStockCode(orderInfo.getStockCode());
		history.setUserNum(userNum);
		history.setTradePrice(orderInfo.getOrderPrice());
		history.setTradeCnt(orderInfo.getOrderCount() * -1); // 음수로 매도 구분
		insertTradeHistory(history);

		// 현금정보 저장
		updateUserCash(userNum, totalPrice);

		// 포트폴리오 업데이트
		portfolio.setUserStockCnt(portfolio.getUserStockCnt() - orderInfo.getOrderCount());
		portfolio.setUserBuyCost(portfolio.getUserBuyCost() 
						- totalPrice);
		if(portfolio.getUserStockCnt() == 0) {
			deleteUserPortfolioByUserNumAndStockCode(userNum, orderInfo.getStockCode());
		}else {
			updateUserPortfolio(portfolio);
		}

		return true;
	}
	

//	================DAO==================

//	=============t_portfolio==============
	private List<PortfolioDTO> findUserPortfolioListByUserNum(int userNum) {
		List<PortfolioDTO> portfolioList = orderDAO.findUserPortfolioListByUserNum(userNum);
		return portfolioList;
	}


	private PortfolioDTO findUserPortfolioByUserNumAndStockCode(int userNum, String stockCode) {
		PortfolioDTO portfolio = orderDAO.findUserPortfolioByUserNumAndStockCode(userNum, stockCode);
		return portfolio;
	}

	private int insertUserPortfolio(PortfolioDTO portfolio) { // 포트폴리오 저장
		int result = orderDAO.insertUserPortfolio(portfolio);
		return result;
	}

	private int updateUserPortfolio(PortfolioDTO portfolio) {
		int result = orderDAO.updateUserPortfolio(portfolio);
		return result;
	}

	private int deleteUserPortfolioByUserNumAndStockCode(int userNum, String stockCode) {
		int result = orderDAO.deleteUserPortfolioByUserNumAndStockCode(userNum, stockCode);
		return result;
	}
	
	private int updateOrInsertUserPortfolio(PortfolioDTO portfolio) {
		System.out.println("hi");
		int result = orderDAO.updateOrInsertUserPortfolio(portfolio);
		System.out.println("hi");
		return result;
		
	}
	
	

//	=============t_cash==============
	private UserCashDTO findUserCashByUserNum(int userNum) {
		UserCashDTO userCash = cashDAO.findUserCashByUserNum(userNum);
		return userCash;
	}

	private int updateUserCash(int userNum, int add) { // 변동 현금정도 저장
		int result = cashDAO.updateUserCash(userNum, add);
		return result;
	}

//	=============t_history==============
	private List<HistoryDTO> findTradeHistoryByUsernum(int userNum) {
		List<HistoryDTO> history = historyDAO.findTradeHistoryByUserNum(userNum);
		return history;
	}

	private int insertTradeHistory(HistoryDTO history) { // 거래기록 저장
		int result = historyDAO.insertTradeHistory(history);
		return result;
	}

}
