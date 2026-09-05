package com.frade.dto.stock;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockPriceComputableDTO {
	String stockCode;
	LocalDateTime dateTime;
	int priceOpen;
	int priceHigh;
	int priceLow;
	int priceClose;
	long volume;

	//종목코드 dateTime 가격 volume을 받는 생성자
	public StockPriceComputableDTO(String stockCode, LocalDateTime dateTime, int priceClose, long volume) {
		this.stockCode = stockCode;
		this.dateTime = dateTime;
		this.priceOpen = priceClose;
		this.priceHigh = priceClose;
		this.priceLow = priceClose;
		this.priceClose = priceClose;
		this.volume = Math.abs(volume);
	}

	//새 데이터가 들어와서 갱신
	public void updateRealtimeData(int newPrice, long addVolume) {
		//종가는 항상 갱신
		this.priceClose = newPrice;

		//고가는 더 큰값으로
		this.priceHigh = Math.max(this.priceHigh, newPrice);
		//저가는 더 작은값으로
		this.priceLow = Math.min(this.priceLow, newPrice);

		//볼륨은 누적
		this.volume += Math.abs(addVolume);
	}

	//불변객체로
	public StockPriceDTO toFinalDTO() {
		return new StockPriceDTO(stockCode, dateTime, priceOpen, priceHigh, priceLow, priceClose, volume);
	}
}
