package com.frade.dao.user;

import com.frade.dto.user.UserSignDTO;

public interface UserDAO {
	
	UserSignDTO findUserById(String uId); //DB에서 해당 아이디의 회원 조회

}
