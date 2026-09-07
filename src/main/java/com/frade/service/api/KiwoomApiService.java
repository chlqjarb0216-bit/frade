package com.frade.service.api;

import java.time.LocalDateTime;
import java.util.List;

import com.frade.dto.stock.StockInfoDTO;
import com.frade.dto.stock.StockPriceDTO;

public interface KiwoomApiService {
	//싱싱한 토큰 가져오기
	public String getOrRefreshAccessTokenString();

	public void revokeToken();

	//KiwoomApiRepository에서 StockInfoListResponse를 받아 처리후 StockInfoDTO리스트 반환.
	public List<StockInfoDTO> getMarketAllStockInfo();

	//하루치 코스피 데이터를 받아옴
	public List<StockPriceDTO> getKOSPIChartDataByDay(String dayString);

	//KiwoomApiRepository에서 코스피 데이터를 받아옴
	public List<StockPriceDTO> getKOSPIChartDataFromLastData(LocalDateTime last);

	//하루치 해당종목 데이터를 받아옴
	public List<StockPriceDTO> getStockChartDataByDay(String stockCode, String dayString);
}
