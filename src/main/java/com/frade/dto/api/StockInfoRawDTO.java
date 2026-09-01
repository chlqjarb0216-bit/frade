package com.frade.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.frade.common.stock.StockSector;
import com.frade.dto.stock.StockInfoDTO;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StockInfoRawDTO {
	private String code;
	private String name;
	private String auditInfo;
	private String upName;

	public StockInfoDTO toStockInfo() {
		int scNum = StockSector.getSectorNumber(this.upName);
		int stockStatus = "거래정지".equals(this.auditInfo) ? 1 : 0;
		return new StockInfoDTO(this.code, this.name, scNum, stockStatus);
	}
}
