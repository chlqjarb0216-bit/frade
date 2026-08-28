package com.frade.dto.user;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserDTO {
	
	int uNum;
    String uId;
    String uNick;
    String uEmail;
    String uPw;
    LocalDateTime uRegistedDate;
    int uPIsPublic;
    String uPhoto;
    int uIsDeleted;

}
