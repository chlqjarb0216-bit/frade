package com.frade.dto.stock;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor //더미데이터 생성용 생성자
public class StockInfoDTO {
	private String stockCode;
	private String stockName;
	private int sectorNum;
	private int stockStatus;
	private int prevDayClosePrice;
}
