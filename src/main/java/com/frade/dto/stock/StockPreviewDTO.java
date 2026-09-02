package com.frade.dto.stock;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockPreviewDTO {
	private String stockCode;
	private String stockName;
	private int price;
	private double dailyChangeRate;
}
