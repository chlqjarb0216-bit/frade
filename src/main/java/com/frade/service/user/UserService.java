package com.frade.service.user;

import com.frade.common.ResultCode;
import com.frade.dto.user.UserSignDTO;

public interface UserService {
	
	int userLogin(UserSignDTO userSignDTO);//로그인 처리가 되었냐 안되었냐 -1이면 로그인 실패 성공시 유저넘버 받음
	
	boolean checkUserId(String userId); //유저아이디 //중복 확인 3개다 공통입니다 중복한다는거 중복 일시 true 중복이 아니면 false를 리턴합니다.
	
	boolean checkUserNick(String userNick);//유저 닉네임 중복 확인
	
	boolean checkUserEmail(String userEmail); // 유저 이메일 중복 확인
	
	ResultCode userSignup(UserSignDTO userSignDTO);//회원가입 처리 함수
	
	
	
	
	
	
	
	

}
