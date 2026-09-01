package com.frade.dto.user;

import lombok.Data;

@Data
public class UserProfileDTO {	//로그인,회원가입,프로필 수정
	
    String uNick;        // 닉네임
    String uPhoto;       // 프로필 사진

    String currentPw;    // 현재 비밀번호
    String newPw;        // 변경할 비밀번호

}
