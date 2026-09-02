package com.frade.service.portfolio.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frade.dao.portfolio.CashDAO;
import com.frade.dao.portfolio.HistoryDAO;
import com.frade.dao.portfolio.PortfolioDAO;
import com.frade.dto.order.HistoryDTO;
import com.frade.dto.user.AssetsInfoDTO;
import com.frade.dto.user.PortfolioDTO;
import com.frade.dto.user.UserCashDTO;
import com.frade.service.portfolio.PortfolioService;

@Service
public class PortfolioServiceImpl implements PortfolioService {

	@Autowired
	PortfolioDAO portfolioDAO;

	@Autowired
	CashDAO cashDAO;

	@Autowired
	HistoryDAO historyDAO;

	@Autowired

	@Override
	public AssetsInfoDTO getAssetsInfo() {
		
		List<PortfolioDTO> portfolioList = findUserPortfolioListByUserNum(1);
		UserCashDTO userCash = cashDAO.findUserCashByUserNum(1);
		List<HistoryDTO> historyList = historyDAO.findTradeHistoryByUserNum(1);

		AssetsInfoDTO assetsInfo = new AssetsInfoDTO();
		long valuation = totalValuationCaculator(portfolioList);
		long cash = userCash.getCash();

		assetsInfo.setTotalAsset(valuation + cash);
		assetsInfo.setTotalValuation(valuation);
		assetsInfo.setCash(cash);
		assetsInfo.setStockCnt(portfolioList.size());
		assetsInfo.setTradeCnt(historyList.size());
		assetsInfo.setTotalRevenue(assetsInfo.getTotalAsset() - 10000000);
		assetsInfo.setRevenuePercent((double)assetsInfo.getTotalRevenue() / 10000000 * 100);

		return assetsInfo;
	}

	private long totalValuationCaculator(List<PortfolioDTO> portfolioList) { // 주식평가금 계산
		long result = 0;

		// 현재가 임시 데이터
		Map<String, Long> currentPriceMap = new HashMap<>();
		currentPriceMap.put("005930", 72_000L);
		currentPriceMap.put("000660", 195_000L);

		for (PortfolioDTO portfolio : portfolioList) {

			Long currentPrice = currentPriceMap.get(portfolio.getStockCode());

			// 현재가가 존재하는 종목만 계산
			if (currentPrice != null) {
				result += currentPrice * portfolio.getUserStockCnt();
			}
		}

		return result;
	}

//	=============t_portfolio DAO==============

	@Override
	public List<PortfolioDTO> findUserPortfolioListByUserNum(int userNum) {
		List<PortfolioDTO> portfolioList = portfolioDAO.findUserPortfolioListByUserNum(userNum);
		return portfolioList;
	}

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
