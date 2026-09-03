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
	public List<StockPreviewDTO> getSortedStockRankingListPage(int pageIdx, int pageSize) {
		// TODO Auto-generated method stub
		List<StockPreviewDTO> stockList = new ArrayList<StockPreviewDTO>();
		for (int i = 0; i < 10; i++) {
			stockList.add(new StockPreviewDTO("000" + i + "00", i + "성전자", "전기·전자", 100000 - i * 1000, 90000));
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

	@Override
	public StockPreviewDTO getStockPreviewByStockCode(String stockCode) {
		// TODO Auto-generated method stub
		return new StockPreviewDTO("000300", "3성전자", "전기·전자", 100000 - 3000, 90000);
	}

	@Override
	public List<StockInfoDTO> searchStockByName(String stockName) {
		// TODO Auto-generated method stub
		List<StockInfoDTO> stockList = new ArrayList<StockInfoDTO>();
		for (int i = 0; i < 10; i++) {
			stockList.add(new StockInfoDTO("000" + i + "00", i + "성전자", i, 0, 90000 + i * 1000));
		}
		return stockList;
	}

}
