package com.frade.service.order.impl;

import java.util.List;

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
	public int processBuy(OrderInfoDTO orderInfo) { // 매수

		// 임시데이터 하드코딩
		String stockCode = "000660"; // 삼성전자
		int userNum = 1;

		UserCashDTO cash = findUserCashByUserNum(userNum);

		// 검증
		if (orderInfo.getOrderPrice() * orderInfo.getOrderCount() > cash.getCash()) {
			System.out.println("주문 금액이 보유 예치금보다 많음.");
			return 0;
		} else if (orderInfo.getOrderCount() <= 0) {
			System.out.println("주문 수량은 1 이상이어야 함.");
			return 0;
		} else if (orderInfo.getOrderPrice() <= 0) {
			System.out.println("주문 금액은 1 이상이어야 함.");
			return 0;
		}

		// 거래기록 저장
		HistoryDTO history = new HistoryDTO();
		history.setStockCode(stockCode);
		history.setUserNum(userNum);
		history.setTradePrice(orderInfo.getOrderPrice());
		history.setTradeCnt(orderInfo.getOrderCount());
		insertTradeHistory(history);

		// 현금정보 저장
		cash.setCash(cash.getCash() - orderInfo.getOrderCount() * orderInfo.getOrderPrice());
		updateUserCash(cash);

		// 포트폴리오 업데이트
		PortfolioDTO portfolio = new PortfolioDTO();
		if(findUserPortfolioByUserNumAndStockCode(userNum, stockCode) != null) {
			portfolio = findUserPortfolioByUserNumAndStockCode(userNum, stockCode);
			portfolio.setUserStockCnt(portfolio.getUserStockCnt() + orderInfo.getOrderCount());
			portfolio.setUserBuyCost(portfolio.getUserBuyCost() 
					+ orderInfo.getOrderCount() * orderInfo.getOrderPrice());
			updateUserPortfolio(portfolio);
		}else {
			portfolio.setUserNum(userNum);
			portfolio.setStockCode(stockCode);
			portfolio.setUserStockCnt(orderInfo.getOrderCount());
			portfolio.setUserBuyCost(orderInfo.getOrderCount() * orderInfo.getOrderPrice());
			insertUserPortfolio(portfolio);
		}
		return 1;
	}

	@Override
	public int processSell(OrderInfoDTO orderInfo) { // 매도

		// 임시데이터 하드코딩
		String stockCode = "000660"; // 삼성전자
		int userNum = 1;

		PortfolioDTO portfolio = findUserPortfolioByUserNumAndStockCode(userNum, stockCode);

		// 검증
		if (portfolio == null) {
			System.out.println("해당 주식을 보유하고 있지 않음");
		} else if (orderInfo.getOrderCount() > portfolio.getUserStockCnt()) {
			System.out.println("매도수량이 보유수량보다 많음");
			return 0;
		} else if (orderInfo.getOrderCount() <= 0) {
			System.out.println("매도수량은 0보다 커야함");
			return 0;
		} else if (orderInfo.getOrderPrice() <= 0) {
			System.out.println("매도가격은 0보다 커야함");
			return 0;
		}

		// 거래기록 저장
		HistoryDTO history = new HistoryDTO();
		history.setStockCode(stockCode);
		history.setUserNum(userNum);
		history.setTradePrice(orderInfo.getOrderPrice());
		history.setTradeCnt(orderInfo.getOrderCount() * -1); // 음수로 매도 구분
		insertTradeHistory(history);

		// 현금정보 저장
		UserCashDTO cash = findUserCashByUserNum(userNum);
		cash.setCash(cash.getCash() + orderInfo.getOrderCount() * orderInfo.getOrderPrice());
		updateUserCash(cash);

		// 포트폴리오 업데이트
		portfolio.setUserStockCnt(portfolio.getUserStockCnt() - orderInfo.getOrderCount());
		portfolio.setUserBuyCost(portfolio.getUserBuyCost() 
						- orderInfo.getOrderCount() * orderInfo.getOrderPrice());
		if(portfolio.getUserStockCnt() == 0) {
			deleteUserPortfolioByUserNumAndStockCode(userNum, stockCode);
		}else {
			updateUserPortfolio(portfolio);
		}

		return 1;
	}

//	================DAO==================

//	=============t_portfolio==============
	@Override
	public List<PortfolioDTO> findUserPortfolioListByUserNum(int userNum) {
		List<PortfolioDTO> portfolioList = orderDAO.findUserPortfolioListByUserNum(userNum);
		return portfolioList;
	}

	@Override
	public PortfolioDTO findUserPortfolioByUserNumAndStockCode(int userNum, String stockCode) {
		PortfolioDTO portfolio = orderDAO.findUserPortfolioByUserNumAndStockCode(userNum, stockCode);
		return portfolio;
	}

	@Override
	public int insertUserPortfolio(PortfolioDTO portfolio) { // 포트폴리오 저장
		int result = orderDAO.insertUserPortfolio(portfolio);
		return result;
	}

	@Override
	public int updateUserPortfolio(PortfolioDTO portfolio) {
		int result = orderDAO.updateUserPortfolio(portfolio);
		return result;
	}

	@Override
	public int deleteUserPortfolioByUserNumAndStockCode(int userNum, String stockCode) {
		int result = orderDAO.deleteUserPortfolioByUserNumAndStockCode(userNum, stockCode);
		return result;
	}

//	=============t_cash==============
	@Override
	public UserCashDTO findUserCashByUserNum(int userNum) {
		UserCashDTO userCash = orderDAO.findUserCashByUserNum(userNum);
		return userCash;
	}

	@Override
	public int updateUserCash(UserCashDTO userCash) { // 변동 현금정도 저장
		int result = orderDAO.updateUserCash(userCash);
		return result;
	}

//	=============t_history==============
	@Override
	public List<HistoryDTO> findTradeHistoryByUsernum(int userNum) {
		List<HistoryDTO> history = orderDAO.findTradeHistoryByUserNum(userNum);
		return history;
	}

	@Override
	public int insertTradeHistory(HistoryDTO history) { // 거래기록 저장
		int result = orderDAO.insertTradeHistory(history);
		return result;
	}

}
