package com.frade.dto.order;

import com.frade.common.order.PriceOptionCommon;
import com.frade.common.order.TradeOptionCommon;

import lombok.Data;

@Data
public class OrderInfoDTO {	//사용자가 입력한 주문정보
	
	int userNum; // 유저 번호
	String stockCode; // 종목 코드
	PriceOptionCommon priceOption;//지정가-시장가 옵션
	TradeOptionCommon tradeOption; //매수-매도 옵션
	int orderPrice; //주문 가격
	int orderCount; //주문 수량
	
}
