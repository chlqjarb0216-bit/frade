package com.frade.dto.user;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
	
	int userNum;
    // INSERT : 0 또는 시퀀스 처리
    // UPDATE : 회원번호

    String userId;
    // INSERT : 값 있음
    // UPDATE : null

    String userNick;
    // INSERT : 값 있음
    // UPDATE : 값 있음

    String userEmail;
    // INSERT : 값 있음
    // UPDATE : null

    String userPw;
    // INSERT : 값 있음
    // UPDATE : 비밀번호 변경 시 값 있음 / 미변경 시 null

    LocalDateTime userRegistedDate;
    // INSERT : DB 기본값
    // UPDATE : null

    int userPortfolioIsPublic;
    // INSERT : 기본값
    // UPDATE : 값 있음

    String userPhoto;
    // INSERT : 기본 프로필이면 null
    // UPDATE : 변경된 프로필 사진명 또는 null
    
    
    // 회원가입용 생성자
    public UserDTO(
            String userId,
            String userNick,
            String userEmail,
            String userPw) {

        this.userId = userId;
        this.userNick = userNick;
        this.userEmail = userEmail;
        this.userPw = userPw;
    }

    // 프로필 수정용 생성자
    public UserDTO(
            int userNum,
            String userNick,
            String userPw,
            int userPortfolioIsPublic,
            String userPhoto) {

        this.userNum = userNum;
        this.userNick = userNick;
        this.userPw = userPw;
        this.userPortfolioIsPublic = userPortfolioIsPublic;
        this.userPhoto = userPhoto;
    }
   


}
