package com.frade.dto.user;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserSignDTO {
	
	int uNum;
    String uId;
    String uNick;
    String uEmail;
    String uPw;

}
