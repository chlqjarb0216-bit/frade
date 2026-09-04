package com.frade.dto.user;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@Data
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

    Integer userIsDeleted;


}
