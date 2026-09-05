package com.frade.dao.portfolio;

import com.frade.dto.user.UserCashDTO;

public interface CashDAO {
	
	public UserCashDTO findUserCashByUserNum(int userNum); //유저 보유 현금 조회

	//거래 시작시 해당 유저 현금데이터 접근 잠금
	public UserCashDTO findUserCashByUserNumForUpdate(int userNum); 
	
	public int updateUserCash(int userNum, int add); 
	
}
