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
		UserCashDTO userCash = sqlSessionTemplate.selectOne("cash_mapper.findUserCashByUserNum", userNum);
		return userCash;
	}

	@Override
	public UserCashDTO findUserCashByUserNumForUpdate(int userNum) {
		return sqlSessionTemplate.selectOne("cash_mapper.findUserCashByUserNumForUpdate", userNum);
	}
	
	@Override
	public int updateUserCash(int userNum, int add) {
		Map<String, Integer> params = new HashMap<>();
	    params.put("userNum", userNum);
	    params.put("add", add);
		int result = sqlSessionTemplate.update("cash_mapper.updateUserCash", params);
		return result;
	}

	
}
