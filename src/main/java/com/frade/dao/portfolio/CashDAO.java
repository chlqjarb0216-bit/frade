package com.frade.dao.portfolio;

import com.frade.dto.user.UserCashDTO;

public interface CashDAO {
	
	public UserCashDTO findUserCashByUserNum(int userNum); //유저 보유 현금 조회
	
	public int updateUserCash(int userNum, int add); 
	
}
