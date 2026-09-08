package com.frade.dto.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KiwoomIndexChartResponse {
	@JsonProperty("return_code")
	int returnCode;
	@JsonProperty("return_msg")
	String returnMsg;
	@JsonProperty("inds_cd")
	String indsCd;
	@JsonProperty("inds_min_pole_qry")
	List<StockPriceRawDTO> indsMinPoleQry;
}
