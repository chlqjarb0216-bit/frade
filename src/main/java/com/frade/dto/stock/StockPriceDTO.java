package com.frade.dto.stock;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockPriceDTO {
	String stockCode;
	LocalDateTime dateTime;
	int priceOpen;
	int priceHigh;
	int priceLow;
	int priceClose;
	long volume;
}
