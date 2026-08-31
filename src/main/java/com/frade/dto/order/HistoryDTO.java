package com.frade.dto.order;

import lombok.Data;

@Data
public class HistoryDTO {	//체결 내역
	
	int uNum; //유저 번호
	String stockCode; //종목 코드
	int orderPrice; //체결 가격
	int orderCount; //체결 수량
	
}
