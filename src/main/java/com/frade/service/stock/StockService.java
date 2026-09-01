package com.frade.service.stock;

import java.util.List;

import com.frade.dto.stock.StockInfoDTO;
import com.frade.dto.stock.StockPriceDTO;

public interface StockService {
	//KiwoomApiService에서 StockInfoRawDTO리스트를 받아와 StockInfoDTO리스트로 변환한뒤 DB에 저장
	public List<StockInfoDTO> updateStockInfoList();

	//100개 종목의 1분봉 데이터를 DB에 저장
	public int saveMinuteStockPrice(List<StockPriceDTO> stockPriceList);
}
