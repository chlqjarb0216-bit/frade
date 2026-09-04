package com.frade.service.stock;

import java.util.List;

import com.frade.dto.stock.StockInfoDTO;
import com.frade.dto.stock.StockPreviewDTO;
import com.frade.dto.stock.StockPriceDTO;

public interface StockService {
	//StockMemoryCache에서 검색
	public List<StockInfoDTO> searchStockByName(String stockName);

	//StockRankingCache에서 종목코드로 StockPreviewDTO를 검색
	public StockPreviewDTO getStockPreviewByStockCode(String stockCode);

	//StockRankingCache에서 정렬된 주식리스트를 받아옴(페이징)
	public List<StockPreviewDTO> getSortedStockRankingListPage(int pageIdx, int pageSize);

	//100개 종목의 1분봉 데이터를 DB에 저장
	public int saveMinuteStockPrice(List<StockPriceDTO> stockPriceList);

	//api토큰 폐기
	public void revokeApiToken();

	//KiwoomApiService에서 StockInfoRawDTO리스트를 받아와 StockInfoDTO리스트로 변환한뒤 DB에 저장
	public List<StockInfoDTO> updateStockInfoList();
}
