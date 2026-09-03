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
	    List<HistoryDTO> history = sqlSessionTemplate.selectList("order_mapper.findTradeHistoryByUserNum", userNum);
	    return history;
	}
	
	@Override
	public int insertTradeHistory(HistoryDTO history) {
		int result = sqlSessionTemplate.insert("order_mapper.insertTradeHistory", history);
		return result;
	}

}
