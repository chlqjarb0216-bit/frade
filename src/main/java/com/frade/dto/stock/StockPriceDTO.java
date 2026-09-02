package com.frade.dto.stock;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StockPriceDTO {
	private String stockCode;
	private LocalDateTime dateTime;
	private int priceOpen;
	private int priceHigh;
	private int priceLow;
	private int priceClose;
	//매수량
	private int volumeBuy = 0;
	//매도량
	private int volumeSell = 0;

	//종목코드 dateTime 가격 volume을 받는 생성자
	public StockPriceDTO(String stockCode, LocalDateTime dateTime, int priceClose, String rawVolume) {
		this.stockCode = stockCode;
		this.dateTime = dateTime;
		this.priceOpen = priceClose;
		this.priceHigh = priceClose;
		this.priceLow = priceClose;
		this.priceClose = priceClose;

		String cleanVolumeStr = rawVolume.replace("+", "").trim();
		int volume = Integer.parseInt(cleanVolumeStr);
		//volume이 양수일 때만 누적
		this.volumeBuy += Math.max(0, volume);
		//volume이 음수일 때만 누적
		this.volumeSell += Math.max(0, -volume);
	}

	//새 데이터가 들어와서 갱신
	public void updateRealtimeData(int newPrice, String rawVolume) {
		//종가는 항상 갱신
		this.priceClose = newPrice;

		//고가는 더 큰값으로
		this.priceHigh = Math.max(this.priceHigh, newPrice);
		//저가는 더 작은값으로
		this.priceLow = Math.min(this.priceLow, newPrice);

		// 거래량은 1분 마감 전까지 계속 누적(+=, -=).
		String cleanVolumeStr = rawVolume.replace("+", "").trim();
		int volume = Integer.parseInt(cleanVolumeStr);
		//volume이 양수일 때만 누적
		this.volumeBuy += Math.max(0, volume);
		//volume이 음수일 때만 누적
		this.volumeSell += Math.max(0, -volume);
	}
}
