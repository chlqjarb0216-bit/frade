package com.frade.dto.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockInfoListResponse {
	private String returnMsg;
	private String returnCode;
	private List<StockInfoRawDTO> list;
}
