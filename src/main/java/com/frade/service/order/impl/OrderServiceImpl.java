package com.frade.service.order.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.frade.common.order.TradeOptionCommon;
import com.frade.dao.portfolio.CashDAO;
import com.frade.dao.portfolio.HistoryDAO;
import com.frade.dao.portfolio.PortfolioDAO;
import com.frade.dto.event.PriceExecutedEvent;
import com.frade.dto.order.HistoryDTO;
import com.frade.dto.order.OrderInfoDTO;
import com.frade.dto.user.PortfolioDTO;
import com.frade.dto.user.UserCashDTO;
import com.frade.exception.OrderProcessingException;
import com.frade.service.order.LimitOrderMatchingService;
import com.frade.service.order.OrderService;
import com.frade.service.portfolio.PortfolioService;

@Service
public class OrderServiceImpl implements OrderService {

	private final TransactionTemplate transactions;

	@Autowired
	public OrderServiceImpl(DataSource dataSource) {
		this.transactions = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
	}

	@Autowired
	PortfolioDAO portfolioDAO;
	@Autowired
	CashDAO cashDAO;
	@Autowired
	HistoryDAO historyDAO;

	@Autowired
	PortfolioService portfolioService;

	@Autowired
	LimitOrderMatchingService limitOrderMatchingService;

	@Override
	public boolean processMarketBuy(OrderInfoDTO orderInfo) {
		if (orderInfo.getOrderCount() <= 0 || orderInfo.getOrderPrice() <= 0) return false;
		boolean result = transactions.execute(status -> processMarketBuyInTransaction(orderInfo));
		return result;
	}

	private boolean processMarketBuyInTransaction(OrderInfoDTO orderInfo) { // 매수

		int totalPrice = Math.multiplyExact(orderInfo.getOrderCount(), orderInfo.getOrderPrice());

		// 로그인 세션에서 검증한 회원번호
		int userNum = orderInfo.getUserNum();

		UserCashDTO cash = findUserCashByUserNumForUpdate(userNum);
		if (cash == null) {
			throw new IllegalStateException("사용자 현금 데이터 없음");
		}

		// 검증
		if (totalPrice > cash.getCash() - cash.getMargin()) {
			System.out.println("주문 금액이 보유 예치금보다 많음.");
			return false;
		}

		// 현금정보 저장
		if (updateUserCash(userNum, -1 * totalPrice) == 0) {
			throw new OrderProcessingException("현금 정보 갱신 실패");
		}

		// 포트폴리오 merge into
		PortfolioDTO portfolio = new PortfolioDTO();
		portfolio.setUserNum(userNum);
		portfolio.setStockCode(orderInfo.getStockCode());
		portfolio.setUserStockCnt(orderInfo.getOrderCount());
		portfolio.setUserBuyCost(orderInfo.getOrderCount() * orderInfo.getOrderPrice());

		if (portfolioService.updateOrInsertUserPortfolio(portfolio) == 0) {
			throw new OrderProcessingException("포트폴리오 갱신 실패");
		}

		// 거래기록 저장
		HistoryDTO history = new HistoryDTO(orderInfo.getStockCode(), userNum, orderInfo.getOrderPrice(),
				orderInfo.getOrderCount());

		if (insertTradeHistory(history) == 0) {
			throw new OrderProcessingException("거래 기록 insert 실패.");
		}

		return true;
	}

	@Override
	public boolean processMarketSell(OrderInfoDTO orderInfo) {
		if (orderInfo.getOrderCount() <= 0 || orderInfo.getOrderPrice() <= 0) return false;
		boolean result = transactions.execute(status -> processMarketSellInTransaction(orderInfo));
		return result;
	}

	private boolean processMarketSellInTransaction(OrderInfoDTO orderInfo) { // 매도
		
		int totalPrice = Math.multiplyExact(orderInfo.getOrderCount(), orderInfo.getOrderPrice());

		// 로그인 세션에서 검증한 회원번호
		int userNum = orderInfo.getUserNum();
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
			throw new OrderProcessingException("현금 정보 갱신 실패");
		}

		// 포트폴리오 업데이트
		long soldCost = portfolio.getUserBuyCost() / portfolio.getUserStockCnt() * orderInfo.getOrderCount()
				+ portfolio.getUserBuyCost() % portfolio.getUserStockCnt() * orderInfo.getOrderCount() / portfolio.getUserStockCnt();
		portfolio.setUserStockCnt(portfolio.getUserStockCnt() - orderInfo.getOrderCount());
		portfolio.setUserBuyCost(portfolio.getUserBuyCost() - soldCost);
		if (portfolio.getUserStockCnt() == 0) {
			if (portfolioService.deleteUserPortfolioByUserNumAndStockCode(userNum, orderInfo.getStockCode()) == 0) {
				throw new OrderProcessingException("포트폴리오 삭제 실패");
			}
		} else {
			if (portfolioService.updateUserPortfolio(portfolio) == 0) {
				throw new OrderProcessingException("포트폴리오 갱신 실패");
			}
		}
		// 거래기록 저장
		HistoryDTO history = new HistoryDTO(orderInfo.getStockCode(), userNum, orderInfo.getOrderPrice(),
				orderInfo.getOrderCount() * -1);

		if (insertTradeHistory(history) == 0) {
			throw new OrderProcessingException("거래기록 insert 실패");
		}

		return true;
	}

	// 지정가 매수 체결
	private boolean processLimitBuy(OrderInfoDTO orderInfo) {

		int totalPrice = Math.multiplyExact(orderInfo.getOrderCount(), orderInfo.getOrderPrice());

		int userNum = orderInfo.getUserNum();

		UserCashDTO cash = findUserCashByUserNumForUpdate(userNum);
		if (cash == null || cash.getMargin() < totalPrice || cash.getCash() < totalPrice) {
			throw new OrderProcessingException("지정가 매수 증거금 부족");
		}

		// 현금정보 저장
		if (cashDAO.updateUserMargin(userNum, totalPrice) == 0) {
			throw new OrderProcessingException("현금 정보 갱신 실패");
		}

		// 포트폴리오 merge into
		PortfolioDTO portfolio = new PortfolioDTO();
		portfolio.setUserNum(userNum);
		portfolio.setStockCode(orderInfo.getStockCode());
		portfolio.setUserStockCnt(orderInfo.getOrderCount());
		portfolio.setUserBuyCost(orderInfo.getOrderCount() * orderInfo.getOrderPrice());

		if (portfolioService.updateOrInsertUserPortfolio(portfolio) == 0) {
			throw new OrderProcessingException("포트폴리오 갱신 실패");
		}

		// 거래기록 저장
		HistoryDTO history = new HistoryDTO(orderInfo.getStockCode(), userNum, orderInfo.getOrderPrice(),
				orderInfo.getOrderCount());

		if (insertTradeHistory(history) == 0) {
			throw new OrderProcessingException("거래 기록 insert 실패.");
		}

		return true;
	}

	// 지정가 매도 체결
	private boolean processLimitSell(OrderInfoDTO orderInfo) {
		int totalPrice = Math.multiplyExact(orderInfo.getOrderCount(), orderInfo.getOrderPrice());

		// 로그인 세션에서 검증한 회원번호
		int userNum = orderInfo.getUserNum();

		UserCashDTO cash = findUserCashByUserNumForUpdate(userNum);
		if (cash == null) throw new OrderProcessingException("사용자 현금 데이터 없음");

		PortfolioDTO portfolio = portfolioDAO.findUserPortfolioByUserNumAndStockCodeForUpdate(orderInfo.getUserNum(),
				orderInfo.getStockCode());

		if (portfolio == null || portfolio.getUserStockCnt() < orderInfo.getOrderCount()) {
			throw new OrderProcessingException("지정가 매도 보유수량 부족");
		}

		// 현금정보 저장
		if (updateUserCash(userNum, totalPrice) == 0) {
			throw new OrderProcessingException("현금 정보 갱신 실패");
		}

		// 포트폴리오 업데이트
		long soldCost = portfolio.getUserBuyCost() / portfolio.getUserStockCnt() * orderInfo.getOrderCount()
				+ portfolio.getUserBuyCost() % portfolio.getUserStockCnt() * orderInfo.getOrderCount() / portfolio.getUserStockCnt();
		portfolio.setUserStockCnt(portfolio.getUserStockCnt() - orderInfo.getOrderCount());
		portfolio.setUserBuyCost(portfolio.getUserBuyCost() - soldCost);
		if (portfolio.getUserStockCnt() == 0) {
			if (portfolioService.deleteUserPortfolioByUserNumAndStockCode(userNum, orderInfo.getStockCode()) == 0) {
				throw new OrderProcessingException("포트폴리오 삭제 실패");
			}
		} else {
			if (portfolioService.updateUserPortfolio(portfolio) == 0) {
				throw new OrderProcessingException("포트폴리오 갱신 실패");
			}
		}

		// 거래기록 저장
		HistoryDTO history = new HistoryDTO(orderInfo.getStockCode(), userNum, orderInfo.getOrderPrice(),
				orderInfo.getOrderCount() * -1);

		if (insertTradeHistory(history) == 0) {
			throw new OrderProcessingException("거래기록 insert 실패");
		}

		return true;
	}
	
	@Override // 지정가 매수정보 저장
	public boolean saveLimitBuy(OrderInfoDTO orderInfo) {
		if (orderInfo.getOrderCount() <= 0 || orderInfo.getOrderPrice() <= 0) return false;
		boolean result = transactions.execute(status -> saveLimitBuyInTransaction(orderInfo));
		if (result) limitOrderMatchingService.registerOrder(orderInfo);
		return result;
	}

	private boolean saveLimitBuyInTransaction(OrderInfoDTO orderInfo) {
		int totalPrice = Math.multiplyExact(orderInfo.getOrderCount(), orderInfo.getOrderPrice());


		UserCashDTO cash = findUserCashByUserNumForUpdate(orderInfo.getUserNum());
		if (cash == null) {
			throw new IllegalStateException("사용자 현금 데이터 없음");
		}

		// 검증
		if (totalPrice > cash.getCash() - cash.getMargin()) {
			System.out.println("주문 금액이 보유 예치금보다 많음.");
			return false;
		}
		
		if (cashDAO.updateUserCashByLimitPrice(orderInfo.getUserNum(), totalPrice) != 1) {
			throw new OrderProcessingException("증거금 예약 실패");
		}
		return true;
	}
	
	@Override // 지정가 매도정보 저장
	public boolean saveLimitSell(OrderInfoDTO orderInfo) {
		if (orderInfo.getOrderCount() <= 0 || orderInfo.getOrderPrice() <= 0) return false;
		boolean result = transactions.execute(status -> saveLimitSellInTransaction(orderInfo));
		if (result) limitOrderMatchingService.registerOrder(orderInfo);
		return result;
	}

	private boolean saveLimitSellInTransaction(OrderInfoDTO orderInfo) {
		
		
		UserCashDTO cash = findUserCashByUserNumForUpdate(orderInfo.getUserNum());
		if (cash == null) {
			throw new IllegalStateException("사용자 현금 데이터 없음");
		}
		
		PortfolioDTO portfolio = portfolioDAO.findUserPortfolioByUserNumAndStockCodeForUpdate(orderInfo.getUserNum(),
				orderInfo.getStockCode());

		// 검증
		if (portfolio == null) {
			System.out.println("해당 주식을 보유하고 있지 않음");
			return false;
		} else if (orderInfo.getOrderCount() > portfolio.getUserStockCnt()) {
			System.out.println("매도수량이 보유수량보다 많음");
			return false;
		}
		
		return true;
	}

	@EventListener
	private void handlePriceExecutedEvent(PriceExecutedEvent event) {

		OrderInfoDTO orderInfo = event.orderInfoDTO();

		transactions.execute(status -> {
			if (orderInfo.getTradeOption() == TradeOptionCommon.BUY) {
				return processLimitBuy(orderInfo);
			}
			return processLimitSell(orderInfo);
		});
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

	@Override
	public List<HistoryDTO> findTradeHistoryByUserNum(int userNum) {
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
