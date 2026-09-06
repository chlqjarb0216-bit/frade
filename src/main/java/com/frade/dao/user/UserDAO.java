package com.frade.dao.user;

import com.frade.dto.user.UserDTO;
import com.frade.dto.user.UserProfileDTO;

public interface UserDAO {

	// 아이디에 해당하는 user_num 조회
	// 없으면 null
	Integer findUserNumById(String userId);

	// 닉네임에 해당하는 user_num 조회
	// 없으면 null
	Integer findUserNumByNick(String userNick);

	// 이메일에 해당하는 user_num 조회
	// 없으면 null
	Integer findUserNumByEmail(String userEmail);

    // 회원가입에 사용할 다음 user_num 조회
    int getNextUserNum();

    // 회원정보 저장
    int saveUser(UserDTO userDTO);

    // 회원 지갑 생성
    int saveUserCash(int userNum);
    
	// 아이디로 로그인에 필요한 회원정보 조회
	UserDTO findUserById(String userId);
	
	// 회원번호로 마이페이지 프로필 정보 조회
	UserProfileDTO findUserProfileByUserNum(int userNum);
	
	// 회원 프로필 수정
	int updateUserProfile(UserDTO userDTO);
	
	// 회원 탈퇴 처리
	int deleteUser(int userNum);
	
	// 회원번호로 현재 비밀번호 조회
	String findUserPwByUserNum(int userNum);
    
    
}