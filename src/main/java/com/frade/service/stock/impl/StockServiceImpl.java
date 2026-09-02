package com.frade.service.stock.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.frade.dto.stock.StockInfoDTO;
import com.frade.dto.stock.StockPreviewDTO;
import com.frade.dto.stock.StockPriceDTO;
import com.frade.service.stock.StockService;

@Service
public class StockServiceImpl implements StockService {

	@Override
	public List<StockPreviewDTO> getSortedStockRankingListPage(int page) {
		// TODO Auto-generated method stub
		List<StockPreviewDTO> stockList = new ArrayList<StockPreviewDTO>();
		for (int i = 0; i < 10; i++) {
			stockList.add(new StockPreviewDTO("000" + i + "00", "삼" + i + "전자", 100000 - i * 1000, 99990));
		}
		return stockList;
	}

	@Override
	public List<StockInfoDTO> updateStockInfoList() {
		// TODO Auto-generated method stub
		return new ArrayList<StockInfoDTO>();
	}

	@Override
	public int saveMinuteStockPrice(List<StockPriceDTO> stockPriceList) {
		// TODO Auto-generated method stub
		return 0;
	}

}
