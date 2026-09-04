package com.frade.dto.user;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileDTO {	// 프로필 정보 및 수정
	
	int userNum;		//유저 고유 식별번호?
    String userNick;        // 닉네임
    String userPhoto;       // 프로필 사진

    String currentPw;    // 현재 비밀번호
    String newPw;        // 변경할 비밀번호
    String newPwCheck;   // 변경할 새 비밀번호 확인
    
    LocalDateTime userRegistedDate;	//가입일
    
    
    int userPortfolioIsPublic; //포트폴리오 공개,비공개 여부
    
    
    // 프로필 수정 폼에서 넘어오는 값
    MultipartFile profilePhoto;  // 새 프로필 사진
    boolean defaultPhoto;        // 기본 이미지로 변경 여부
    boolean passwordChange;      // 비밀번호 변경 여부
    
    
    
    public boolean isNewPwMatch() {
        return newPw.equals(newPwCheck);
    }

    public boolean isDifferentPw() {
        return !currentPw.equals(newPw);
    }
    
    
    
    //가입화면 출력용
    public String getUserRegistedDateText() {

        if(userRegistedDate == null) {
            return "";
        }

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy.MM.dd");

        return userRegistedDate.format(formatter);
    }
    
    public UserProfileDTO() {
    }
    
    public UserProfileDTO(
            int userNum,
            String userNick,
            String userPhoto,
            int userPortfolioIsPublic,
            LocalDateTime userRegistedDate) {

        this.userNum = userNum;
        this.userNick = userNick;
        this.userPhoto = userPhoto;
        this.userPortfolioIsPublic = userPortfolioIsPublic;
        this.userRegistedDate = userRegistedDate;
    }
    
    

}
