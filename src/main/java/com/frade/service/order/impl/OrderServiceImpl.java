package com.frade.service.order.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.frade.dao.portfolio.CashDAO;
import com.frade.dao.portfolio.HistoryDAO;
import com.frade.dao.portfolio.PortfolioDAO;
import com.frade.dto.order.HistoryDTO;
import com.frade.dto.order.OrderInfoDTO;
import com.frade.dto.user.PortfolioDTO;
import com.frade.dto.user.UserCashDTO;
import com.frade.service.order.OrderService;
import com.frade.service.portfolio.PortfolioService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	PortfolioDAO portfolioDAO;
	@Autowired
	CashDAO cashDAO;
	@Autowired
	HistoryDAO historyDAO;
	
	@Autowired
	PortfolioService portfolioService;

	@Override
	@Transactional
	public boolean processBuy(OrderInfoDTO orderInfo) { // 매수
		
		int totalPrice = orderInfo.getOrderCount() * orderInfo.getOrderPrice();

		//임시 유저넘버
		int userNum = 1;

		UserCashDTO cash = findUserCashByUserNumForUpdate(userNum);
		if (cash == null) {
			throw new IllegalStateException("사용자 현금 데이터 없음");
		}

		// 검증
		if (totalPrice > cash.getCash()) {
			System.out.println("주문 금액이 보유 예치금보다 많음.");
			return false;
		}


		// 현금정보 저장
		if (updateUserCash(userNum, -1 * totalPrice) == 0) {
			throw new IllegalStateException("현금 정보 갱신 실패");
		}

		// 포트폴리오 merge into
		PortfolioDTO portfolio = new PortfolioDTO();
		portfolio.setUserNum(userNum);
		portfolio.setStockCode(orderInfo.getStockCode());
		portfolio.setUserStockCnt(orderInfo.getOrderCount());
		portfolio.setUserBuyCost(orderInfo.getOrderCount() * orderInfo.getOrderPrice());
		

		if (portfolioService.updateOrInsertUserPortfolio(portfolio) == 0) {
			throw new IllegalStateException("포트폴리오 갱신 실패");
		}

		// 거래기록 저장
		HistoryDTO history = new HistoryDTO(orderInfo.getStockCode(), userNum, 
				orderInfo.getOrderPrice(), orderInfo.getOrderCount() * -1);

		if (insertTradeHistory(history) == 0) {
			throw new IllegalStateException("거래 기록 insert 실패.");
		}

		return true;
	}

	@Override
	@Transactional
	public boolean processSell(OrderInfoDTO orderInfo) { // 매도

		int totalPrice = orderInfo.getOrderCount() * orderInfo.getOrderPrice();
		
		//임시 유저넘버
		int userNum = 1;

		UserCashDTO cash = findUserCashByUserNumForUpdate(userNum);
		if (cash == null) {
			throw new IllegalStateException("사용자 현금 데이터 없음");
		}

		PortfolioDTO portfolio = portfolioDAO.findUserPortfolioByUserNumAndStockCodeForUpdate(userNum,
				orderInfo.getStockCode());

		// 검증
		if (portfolio == null) {
			System.out.println("해당 주식을 보유하고 있지 않음");
			return false;
		} else if (orderInfo.getOrderCount() > portfolio.getUserStockCnt()) {
			System.out.println("매도수량이 보유수량보다 많음");
			return false;
		} 

		// 현금정보 저장
		if (updateUserCash(userNum, totalPrice) == 0) {
			throw new IllegalStateException("현금 정보 갱신 실패");
		}

		// 포트폴리오 업데이트
		portfolio.setUserStockCnt(portfolio.getUserStockCnt() - orderInfo.getOrderCount());
		portfolio.setUserBuyCost(portfolio.getUserBuyCost() 
						- totalPrice);
		if(portfolio.getUserStockCnt() == 0) {
			if (portfolioService.deleteUserPortfolioByUserNumAndStockCode(userNum, orderInfo.getStockCode()) == 0) {
				throw new IllegalStateException("포트폴리오 삭제 실패");
			}
		}else {
			if (portfolioService.updateUserPortfolio(portfolio) == 0) {
				throw new IllegalStateException("포트폴리오 갱신 실패");
			}
		}

		// 거래기록 저장
		HistoryDTO history = new HistoryDTO(orderInfo.getStockCode(), userNum, 
				orderInfo.getOrderPrice(), orderInfo.getOrderCount() * -1);
		

		if (insertTradeHistory(history) == 0) {
			throw new IllegalStateException("거래기록 insert 실패");
		}

		return true;
	}
	

//	=============t_cash==============
	

	
	@Override
	public UserCashDTO findUserCashByUserNum(int userNum) {
		UserCashDTO userCash = cashDAO.findUserCashByUserNum(userNum);
		return userCash;
	}
	
	private UserCashDTO findUserCashByUserNumForUpdate(int userNum) {
		UserCashDTO userCash = cashDAO.findUserCashByUserNumForUpdate(userNum);
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

	
	@Override
	public boolean saveMarketPrice(OrderInfoDTO orderInfo) {
		
		return false;
	}

	

}
