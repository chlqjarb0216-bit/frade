package com.frade.dto.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class PortfolioDTO {
	
	int userNum; //유저 번호
	String stockCode; //종목 번호
	int userStockCnt; //보유 수량
	long userBuyCost; //현재까지 매수한 가격의 총합
	
}