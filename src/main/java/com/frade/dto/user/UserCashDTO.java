package com.frade.dto.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class UserCashDTO {
	
	int userNum; //유저 넘버
	long cash; //유저 보유 현금
	long margin; //증거금
	
}
