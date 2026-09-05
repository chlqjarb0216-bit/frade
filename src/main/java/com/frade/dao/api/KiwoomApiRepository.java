package com.frade.dao.api;

import java.util.List;

import com.frade.dto.api.KiwoomAccessToken;
import com.frade.dto.api.StockInfoRawDTO;

public interface KiwoomApiRepository {
	public KiwoomAccessToken getOrRefreshAccessToken();

	public void revokeToken();

	public List<StockInfoRawDTO> getMarketAllStockInfo();
}
