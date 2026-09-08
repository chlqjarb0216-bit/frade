package com.frade.dto.api;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.frade.dto.stock.StockPriceDTO;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockPriceRawDTO {
	@JsonProperty("cur_prc")
	int curPrc;
	@JsonProperty("trde_qty")
	long trdeQty;
	@JsonProperty("cntr_tm")
	@JsonFormat(pattern = "yyyyMMddHHmmss")
	LocalDateTime cntrTm;
	@JsonProperty("open_pric")
	int openPric;
	@JsonProperty("high_pric")
	int highPric;
	@JsonProperty("low_pric")
	int lowPric;

	public StockPriceDTO toStockPriceDTO(String stockCode) {
		return new StockPriceDTO(stockCode, cntrTm, Math.abs(openPric), Math.abs(highPric), Math.abs(lowPric),
				Math.abs(curPrc), Math.abs(trdeQty));
	}
}
