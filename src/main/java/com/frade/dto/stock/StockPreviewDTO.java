package com.frade.dto.stock;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockPreviewDTO {
	private String stockCode;
	private String stockName;
	private String sectorName;
	private int price;
	private int prevDayClosePrice;

	public int getDailyPriceChange() {
		return this.price - this.prevDayClosePrice;
	}

	public double getDailyPriceChangeRate() {
		return (double) this.getDailyPriceChange() / this.prevDayClosePrice;
	}

	public double getDailyPriceChangeRoundedPercent() {
		return Math.round(this.getDailyPriceChangeRate() * 10000) / 100.;
	}
}
