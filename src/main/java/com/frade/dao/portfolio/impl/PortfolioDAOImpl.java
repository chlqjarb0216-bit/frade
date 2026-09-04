package com.frade.dao.portfolio.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.frade.dao.portfolio.PortfolioDAO;
import com.frade.dto.order.HistoryDTO;
import com.frade.dto.user.PortfolioDTO;
import com.frade.dto.user.UserCashDTO;

@Repository
public class PortfolioDAOImpl implements PortfolioDAO{
	
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;

	
	@Override
	public List<PortfolioDTO> findUserPortfolioListByUserNum(int userNum) {
		List<PortfolioDTO> portfolio = sqlSessionTemplate.selectList("portfolio_mapper.findUserPortfolioListByUserNum", userNum);
		return portfolio;
	}
	
	@Override
	public PortfolioDTO findUserPortfolioByUserNumAndStockCode(int userNum, String stockCode) {
		Map<String, Object> params = new HashMap<>();
	    params.put("userNum", userNum);
	    params.put("stockCode", stockCode);
		PortfolioDTO portfolio = sqlSessionTemplate.selectOne("portfolio_mapper.findUserPortfolioByUserNumAndStockCode", params
		    );
		return portfolio;
	}

	@Override
	public PortfolioDTO findUserPortfolioByUserNumAndStockCodeForUpdate(int userNum, String stockCode) {
		Map<String, Object> params = new HashMap<>();
		params.put("userNum", userNum);
		params.put("stockCode", stockCode);
		return sqlSessionTemplate.selectOne("portfolio_mapper.findUserPortfolioByUserNumAndStockCodeForUpdate", params);
	}
	
	@Override
	public int insertUserPortfolio(PortfolioDTO portfolio) {
		int result = sqlSessionTemplate.insert("portfolio_mapper.insertUserPortfolio", portfolio);
		return result;
	}
	
	@Override
	public int updateUserPortfolio(PortfolioDTO portfolio) {
		int result = sqlSessionTemplate.update("portfolio_mapper.updateUserPortfolio", portfolio);
		return result;
	}
	
	@Override
	public int deleteUserPortfolioByUserNumAndStockCode(int userNum, String stockCode) {
		Map<String, Object> params = new HashMap<>();
	    params.put("userNum", userNum);
	    params.put("stockCode", stockCode);
		int result = sqlSessionTemplate.delete("portfolio_mapper.deleteUserPortfolioByUserNumAndStockCode", params);
		return result;
	}

	@Override
	public int updateOrInsertUserPortfolio(PortfolioDTO portfolio) {
		int result = sqlSessionTemplate.update("portfolio_mapper.updateOrInsertUserPortfolio", portfolio);
		return result;
	}



}
