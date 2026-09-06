package com.frade.service.portfolio.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frade.dao.portfolio.CashDAO;
import com.frade.dao.portfolio.HistoryDAO;
import com.frade.dao.portfolio.PortfolioDAO;
import com.frade.dto.order.HistoryForMypageDTO;
import com.frade.dto.user.AssetsInfoDTO;
import com.frade.dto.user.MyPagePortfolioDTO;
import com.frade.dto.user.PortfolioDTO;
import com.frade.dto.user.PortfolioInfoDTO;
import com.frade.dto.user.UserCashDTO;
import com.frade.service.portfolio.PortfolioService;

@Service
public class PortfolioServiceImpl implements PortfolioService {

	private static final long INITIAL_ASSET = 10_000_000L;
	private static final long DEFAULT_CURRENT_PRICE = 210_000L;
	private static final Map<String, Long> CURRENT_PRICE_MAP = Map.of(
			"005930", 72_000L,
			"000660", 195_000L);

	@Autowired
	PortfolioDAO portfolioDAO;

	@Autowired
	CashDAO cashDAO;

	@Autowired
	HistoryDAO historyDAO;

	@Override
	public MyPagePortfolioDTO getMyPagePortfolio(int userNum) {
		List<PortfolioDTO> portfolioList = portfolioDAO.findUserPortfolioListByUserNum(userNum);
		UserCashDTO userCash = cashDAO.findUserCashByUserNum(userNum);
		List<HistoryForMypageDTO> historyList = historyDAO.findTradeHistoryForMypageByUserNum(userNum);

		List<PortfolioInfoDTO> portfolioInfoList = new ArrayList<>();
		List<String> stockNameList = new ArrayList<>();
		List<Long> stockPriceList = new ArrayList<>();
		long totalValuation = 0L;

		for (PortfolioDTO portfolio : portfolioList) {
			int stockCnt = portfolio.getUserStockCnt();
			long currentPrice = getCurrentPrice(portfolio.getStockCode());
			long valuationAmount = currentPrice * stockCnt;
			long buyCost = portfolio.getUserBuyCost();
			long pnl = valuationAmount - buyCost;

			PortfolioInfoDTO portfolioInfo = new PortfolioInfoDTO();
			portfolioInfo.setStockCode(portfolio.getStockCode());
			portfolioInfo.setStockName(portfolio.getStockName());
			portfolioInfo.setStockCnt(stockCnt);
			portfolioInfo.setAvgStockBuyCost(buyCost / stockCnt);
			portfolioInfo.setStockNowPrice(currentPrice);
			portfolioInfo.setValuationAmount(valuationAmount);
			portfolioInfo.setPnl(pnl);
			portfolioInfo.setProfitPercent(calculatePercent(pnl, buyCost));
			portfolioInfoList.add(portfolioInfo);

			totalValuation += valuationAmount;
			stockNameList.add(portfolio.getStockName());
			stockPriceList.add(valuationAmount);
		}

		long cash = userCash == null ? 0L : userCash.getCash();
		long totalAsset = totalValuation + cash;

		for (PortfolioInfoDTO portfolioInfo : portfolioInfoList) {
			portfolioInfo.setWeightPercent(
					calculatePercent(portfolioInfo.getValuationAmount(), totalAsset));
		}

		AssetsInfoDTO assetsInfo = new AssetsInfoDTO(
				totalValuation, cash, portfolioInfoList.size(), historyList.size(), INITIAL_ASSET);

		stockNameList.add("예치금");
		stockPriceList.add(cash);

		MyPagePortfolioDTO myPagePortfolio = new MyPagePortfolioDTO();
		myPagePortfolio.setAssetsInfo(assetsInfo);
		myPagePortfolio.setPortfolioInfoList(portfolioInfoList);
		myPagePortfolio.setHistoryList(historyList);
		myPagePortfolio.setStockNameList(stockNameList);
		myPagePortfolio.setStockPriceList(stockPriceList);

		return myPagePortfolio;
	}

	private long getCurrentPrice(String stockCode) {
		return CURRENT_PRICE_MAP.getOrDefault(stockCode, DEFAULT_CURRENT_PRICE);
	}

	private double calculatePercent(long amount, long total) {
		if (total == 0) {
			return 0.0;
		}

		return Math.round((double) amount / total * 10000) / 100.0;
	}


	//	=============t_portfolio DAO==============

	@Override
	public PortfolioDTO findUserPortfolioByUserNumAndStockCode(int userNum, String stockCode) {
		PortfolioDTO portfolio = portfolioDAO.findUserPortfolioByUserNumAndStockCode(userNum, stockCode);
		return portfolio;
	}

	@Override
	public int insertUserPortfolio(PortfolioDTO portfolio) { // 포트폴리오 저장
		int result = portfolioDAO.insertUserPortfolio(portfolio);
		return result;
	}

	@Override
	public int updateUserPortfolio(PortfolioDTO portfolio) {
		int result = portfolioDAO.updateUserPortfolio(portfolio);
		return result;
	}

	@Override
	public int deleteUserPortfolioByUserNumAndStockCode(int userNum, String stockCode) {
		int result = portfolioDAO.deleteUserPortfolioByUserNumAndStockCode(userNum, stockCode);
		return result;
	}

	@Override
	public int updateOrInsertUserPortfolio(PortfolioDTO portfolio) {
		int result = portfolioDAO.updateOrInsertUserPortfolio(portfolio);
		return result;

	}
	
}
