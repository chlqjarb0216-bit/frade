package com.frade.dao.portfolio.impl;

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
	public int updateUserCash(UserCashDTO userCash) {
		int result = sqlSessionTemplate.update("order_mapper.updateUserCash", userCash);
		return result;
	}
	
}
