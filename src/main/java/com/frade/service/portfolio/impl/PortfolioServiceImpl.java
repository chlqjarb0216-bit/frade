package com.frade.service.portfolio.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frade.dao.portfolio.PortfolioDAO;
import com.frade.dto.user.PortfolioDTO;
import com.frade.service.portfolio.PortfolioService;

@Service
public class PortfolioServiceImpl implements PortfolioService{
	
	@Autowired
	PortfolioDAO portfolioDAO;
	
//	=============t_portfolio==============
	
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
