package com.frade.service.user;

import com.frade.dto.user.UserSignDTO;

public interface UserService {
	
	int userLogin(UserSignDTO userSignDTO);//로그인 처리가 되었냐 안되었냐 -1이면 로그인 실패 성공시 유저넘버 받음
	
	String checkUserId(String uId); //유저아이디 중복 확인 Y/N
	
	String checkUserNick(String uNick);//유저 닉네임 중복 확인
	
	String userSignup(UserSignDTO userSignDTO);//회원가입 처리 함수
	
	String checkUserEmail(String uEmail); // 유저 이메일 중복 확인
	
	
	
	
	
	

}
