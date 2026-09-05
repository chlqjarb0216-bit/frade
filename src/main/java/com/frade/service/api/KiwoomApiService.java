package com.frade.service.api;

import java.util.List;

import com.frade.dto.stock.StockInfoDTO;

public interface KiwoomApiService {
	//싱싱한 토큰 가져오기
	public String getOrRefreshAccessTokenString();

	public void revokeToken();

	//StockRepository에서 StockInfoListResponse를 받아 처리후 StockInfoDTO리스트 반환.
	public List<StockInfoDTO> getMarketAllStockInfo();
}
