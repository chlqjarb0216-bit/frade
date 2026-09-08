package com.frade.dto.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KiwoomStockChartResponse {
	@JsonProperty("return_code")
	int returnCode;
	@JsonProperty("return_msg")
	String returnMsg;
	@JsonProperty("stk_cd")
	String stkCd;
	@JsonProperty("stk_min_pole_chart_qry")
	List<StockPriceRawDTO> stkMinPoleChartQry;
}
