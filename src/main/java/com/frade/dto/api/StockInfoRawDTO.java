package com.frade.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.frade.common.stock.StockSector;
import com.frade.dto.stock.StockInfoDTO;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockInfoRawDTO {
	@JsonProperty("code")
	String code;
	@JsonProperty("name")
	String name;
	@JsonProperty("auditInfo")
	String auditInfo;
	@JsonProperty("lastPrice")
	int lastPrice;
	@JsonProperty("upName")
	String upName;

	public StockInfoDTO toStockInfo() {
		int scNum = StockSector.getSectorNumber(this.upName);
		int stockStatus = "거래정지".equals(this.auditInfo) ? 1 : 0;
		return new StockInfoDTO(this.code, this.name, scNum, stockStatus, this.lastPrice);
	}
}
