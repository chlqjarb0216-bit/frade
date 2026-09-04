package com.frade.dao.portfolio.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.frade.dao.portfolio.HistoryDAO;
import com.frade.dto.order.HistoryDTO;

@Repository
public class HistoryDAOImpl implements HistoryDAO {
	
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
	
//	==========t_history===========
	@Override
	public List<HistoryDTO> findTradeHistoryByUserNum(int userNum) {
	    List<HistoryDTO> history = sqlSessionTemplate.selectList
	    					("history_mapper.findTradeHistoryByUserNum", userNum);
	    return history;
	}
	

	@Override
	public List<HistoryDTO> findTradeHistoryTodayByUserNum(int userNum) {
		List<HistoryDTO> history = sqlSessionTemplate.selectList
							("history_mapper.findTradeHistoryTodayByUserNum", userNum);
		return history;
	}

	@Override
	public List<HistoryDTO> findTradeHistoryRecentOneWeekByUserNum(int userNum) {
		List<HistoryDTO> history = sqlSessionTemplate.selectList
							("history_mapper.findTradeHistoryRecentOneWeekByUserNum", userNum);
		return history;
	}

	@Override
	public List<HistoryDTO> findTradeHistoryRecentOneMonthByUserNum(int userNum) {
		List<HistoryDTO> history = sqlSessionTemplate.selectList
							("history_mapper.findTradeHistoryRecentOneMonthByUserNum", userNum);
		return history;
	}
	
	@Override
	public int insertTradeHistory(HistoryDTO history) {
		int result = sqlSessionTemplate.insert("history_mapper.insertTradeHistory", history);
		return result;
	}

}
