package com.frade.dao.api;

import java.util.List;

import com.frade.dto.api.KiwoomAccessToken;
import com.frade.dto.api.StockInfoRawDTO;
import com.frade.dto.stock.StockPriceDTO;

public interface KiwoomApiRepository {
	public KiwoomAccessToken getOrRefreshAccessToken();

	public void revokeToken();

	public List<StockInfoRawDTO> getMarketAllStockInfo();

	public List<StockPriceDTO> getKOSPIChartDataByDay(String dayString);

	public List<StockPriceDTO> getStockChartDataByDay(String stockCode, String dayString);
}
