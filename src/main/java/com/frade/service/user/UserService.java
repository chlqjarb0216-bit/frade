package com.frade.service.user;

import org.springframework.web.multipart.MultipartFile;

import com.frade.common.ResultCode;
import com.frade.dto.user.UserProfileDTO;
import com.frade.dto.user.UserSignDTO;

public interface UserService {
	
	int userLogin(UserSignDTO userSignDTO);//로그인 처리가 되었냐 안되었냐 -1이면 로그인 실패 성공시 유저넘버 받음
	
	boolean checkUserId(String userId); //유저아이디 //중복 확인 3개다 공통입니다 중복한다는거 중복 일시 true 중복이 아니면 false를 리턴합니다.
	
	boolean checkUserNick(String userNick);//유저 닉네임 중복 확인
	
	boolean checkUserEmail(String userEmail); // 유저 이메일 중복 확인
	
	ResultCode userSignup(UserSignDTO userSignDTO);//회원가입 처리 함수
	
	ResultCode updateUserProfile(UserProfileDTO userProfileDTO, MultipartFile profilePhoto, boolean defaultPhoto,boolean passwordChange);//프로필 정보 수정
	//닉네임, 비밀번호, 프로필사진, 포트폴리오 공개 여부 수정
	
	
	ResultCode deleteUser(int userNum); //로그인한 회원 탈퇴 처리
	
	UserProfileDTO getUserProfile(int userNum); //회원(번호로) 프로필 정보 조회
	
	
	
	
	

}
