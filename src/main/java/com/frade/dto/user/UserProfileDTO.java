package com.frade.dto.user;

import lombok.Data;

@Data
public class UserProfileDTO {	// 프로필 정보 및 수정
	
	int userNum;		//유저 고유 식별번호?
    String userNick;        // 닉네임
    String userPhoto;       // 프로필 사진

    String currentPw;    // 현재 비밀번호
    String newPw;        // 변경할 비밀번호
    String newPwCheck;   // 변경할 새 비밀번호 확인
    
    int userPortfolioIsPublic; //포트폴리오 공개,비공개 여부

}
