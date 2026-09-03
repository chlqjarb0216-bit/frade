package com.frade.dto.user;

import lombok.Data;

@Data
public class PortfolioDTO {
	
	int userNum; //유저 번호
	String stockCode; //종목 번호
	int userStockCnt; //보유 수량
	long userBuyCost; //현재까지 매수한 가격의 총합
	
}