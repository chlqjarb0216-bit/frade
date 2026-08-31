package com.frade.dao.order.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.frade.dao.order.OrderDAO;
import com.frade.dto.order.HistoryDTO;
import com.frade.dto.user.PortfolioDTO;
import com.frade.dto.user.UserCashDTO;

@Repository
public class OrderDAOImpl implements OrderDAO{
	
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public List<PortfolioDTO> findUserPortfolioByUnum(int Unum) {
		return null;
	}

	@Override
	public UserCashDTO findUserCashByUnum(int Unum) {
		return null;
	}

	@Override
	public int saveTradeHistory(HistoryDTO history) {
		return 0;
	}

	@Override
	public int saveUserPortfolio(PortfolioDTO portfolio) {
		return 0;
	}

	@Override
	public int saveUserCash(UserCashDTO userCash) {
		return 0;
	}

}
