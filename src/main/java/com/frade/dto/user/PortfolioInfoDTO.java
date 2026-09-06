package com.frade.dto.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class PortfolioInfoDTO {
	
	String stockCode; //종목 코드
	String stockName; //종목 이름
	int stockCnt; //보유 수량
	long avgStockBuyCost; //매수 평단가
	long stockNowPrice; //현재가
	long valuationAmount; //평가금액
	long pnl; //평가 손익
	double profitPercent; //수익률
	double weightPercent; //보유 주식 내 비중
	
}
