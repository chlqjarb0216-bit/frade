package com.frade.dao.portfolio.impl;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.frade.dao.portfolio.CashDAO;
import com.frade.dto.user.UserCashDTO;

@Repository
public class CashDAOImpl implements CashDAO{
	
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;

	@Override
	public UserCashDTO findUserCashByUserNum(int userNum) {
		UserCashDTO userCash = sqlSessionTemplate.selectOne("order_mapper.findUserCashByUserNum", userNum);
		return userCash;
	}
	
	@Override
	public int updateUserCash(int userNum, int add) {
		Map<String, Integer> params = new HashMap<>();
	    params.put("userNum", userNum);
	    params.put("add", add);
		int result = sqlSessionTemplate.update("order_mapper.updateUserCash", params);
		return result;
	}

	
}
