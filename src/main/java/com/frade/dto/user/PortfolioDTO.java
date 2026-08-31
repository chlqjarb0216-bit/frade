package com.frade.dto.user;

import lombok.Data;

@Data
public class PortfolioDTO {
	
	int uNum; //유저 번호
	String stockCode; //종목 번호
	int stockCtn; //보유 수량
	int buyCost; //현재까지 매수한 가격의 총합
	
}
