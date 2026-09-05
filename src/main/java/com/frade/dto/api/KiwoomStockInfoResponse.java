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
public class KiwoomStockInfoResponse {
	@JsonProperty("return_code")
	int returnCode;
	@JsonProperty("return_msg")
	String returnMsg;
	@JsonProperty("list")
	List<StockInfoRawDTO> list;
}
