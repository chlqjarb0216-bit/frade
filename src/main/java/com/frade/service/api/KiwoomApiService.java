package com.frade.service.api;

import java.util.List;

import com.frade.dto.api.StockInfoRawDTO;

public interface KiwoomApiService {
	//토큰 반환
	public String getOrRefreshAccessToken();

	//StockRepository에서 StockInfoListResponse를 받아 처리후 StockInfoRawDTO리스트 반환. synchronized 필요.
	public List<StockInfoRawDTO> getMarketAllStockInfo();
}
