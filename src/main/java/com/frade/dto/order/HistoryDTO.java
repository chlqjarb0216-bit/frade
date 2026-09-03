package com.frade.dto.order;

import lombok.Data;

@Data
public class HistoryDTO {	//체결 내역
	
	long tradeNum; //거래 번호
	int userNum; //유저 번호
	String stockCode; //종목 코드
	int tradePrice; //체결 가격
	int tradeCnt; //체결 수량
	String tradeDate; //체결 날짜
	
}
