package com.frade.service.stock;

import java.util.List;

import com.frade.dto.stock.StockInfoDTO;
import com.frade.dto.stock.StockPreviewDTO;
import com.frade.dto.stock.StockPriceDTO;

public interface StockService {
	//StockMemoryCache에서 검색
	public List<StockInfoDTO> searchStockByName(String stockName);

	//StockMemoryCache에서 종목코드 리스트 받아오기
	public List<String> getStockCodeList();

	//StockRankingCache에서 종목코드로 StockPreviewDTO를 검색
	public StockPreviewDTO getStockPreviewByStockCode(String stockCode);

	//StockRankingCache에서 정렬된 주식리스트를 받아옴(페이징)
	public List<StockPreviewDTO> getSortedStockRankingListPage(int pageIdx, int pageSize);

	//StockRankingCache 갱신
	public void refreshAndSwapRealtimeRankingCache();

	//StockPriceMemoryCache에 해당 종목의 해당 날짜 리스트를 집어넣음
	public void putStockPriceListToStockPriceMemoryCache(String stockCode, String dayString,
			List<StockPriceDTO> priceList);

	//버퍼에서 마감데이터를 가져와 DB에 저장
	public void flushCompletedMinuteBufferAndSave();

	//DB에서 해당 종목의 해당날짜 1분봉 데이터 리스트를 긁어옴
	public List<StockPriceDTO> selectMinuteStockPriceListByStockCodeAndDayString(String stockCode, String dayString);

	//api토큰 폐기
	public void revokeApiToken();

	//KiwoomApiService에서 StockInfoRawDTO리스트를 받아와 StockInfoDTO리스트로 변환한뒤 DB에 저장
	public int updateStockInfoList();

	//메모리캐시 초기화
	public void initMemoryCache();

	//메모리캐시 초기화
	void warmUpStockPriceMemoryCache(String todayStr, String yesterdayStr);

	//메모리캐시 청소
	void clearOldStockPriceCache(String dayString);
}
