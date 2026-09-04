package com.frade.dto.order;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public class HistoryDTO {	//체결 내역
	
	public HistoryDTO(String stockCode2, int userNum2, int orderPrice, int orderCnt) {
		this.stockCode = stockCode2;
		this.userNum = userNum2;
		this.tradePrice = orderPrice;
		this.tradeCnt = orderCnt;
	}
	long tradeNum; //거래 번호
	int userNum; //유저 번호
	String stockCode; //종목 코드
	int tradePrice; //체결 가격
	int tradeCnt; //체결 수량
	String tradeDate; //체결 날짜
	
}
