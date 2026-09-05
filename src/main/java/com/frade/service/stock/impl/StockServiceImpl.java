package com.frade.service.stock.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frade.dao.stock.StockDAO;
import com.frade.dto.stock.StockInfoDTO;
import com.frade.dto.stock.StockPreviewDTO;
import com.frade.dto.stock.StockPriceDTO;
import com.frade.memcache.StockMemoryCache;
import com.frade.service.api.KiwoomApiService;
import com.frade.service.stock.StockService;

@Service
public class StockServiceImpl implements StockService {

	@Autowired
	KiwoomApiService kiwoomApiService;

	@Autowired
	StockDAO stockDAO;

	@Autowired
	StockMemoryCache stockMemoryCache;

	@Override
	public List<StockInfoDTO> searchStockByName(String stockName) {
		// TODO Auto-generated method stub
		List<StockInfoDTO> stockList = new ArrayList<StockInfoDTO>();
		for (int i = 0; i < 10; i++) {
			stockList.add(new StockInfoDTO("000" + i + "00", i + "성전자", i, 0, 90000 + i * 1000));
		}
		return stockList;
	}

	@Override
	public StockPreviewDTO getStockPreviewByStockCode(String stockCode) {
		// TODO Auto-generated method stub
		return new StockPreviewDTO("000300", "3성전자", "전기·전자", 100000 - 3000, 90000);
	}

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
	public int saveMinuteStockPrice(List<StockPriceDTO> stockPriceList) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void revokeApiToken() {
		kiwoomApiService.revokeToken();
	}

	@Override
	public int updateStockInfoList() {
		List<StockInfoDTO> infoList = kiwoomApiService.getMarketAllStockInfo();

		// 1. XML 문자열 조립
		StringBuilder xmlBuilder = new StringBuilder(infoList.size() * 300);
		xmlBuilder.append("<rows>");
		for (StockInfoDTO dto : infoList) {
			xmlBuilder.append("<row>").append("<stockCode>").append(dto.getStockCode()).append("</stockCode>")
					.append("<stockName><![CDATA[").append(dto.getStockName()).append("]]></stockName>")
					.append("<sectorNum>").append(dto.getSectorNum()).append("</sectorNum>").append("<stockStatus>")
					.append(dto.getStockStatus()).append("</stockStatus>").append("<prevDayClosePrice>")
					.append(dto.getPrevDayClosePrice()).append("</prevDayClosePrice>").append("</row>");
		}
		xmlBuilder.append("</rows>");

		// 2. DB 작업
		List<String> updatedCodes = stockDAO.updateAllStockAndReturnMatchedCodeList(xmlBuilder.toString());

		// 3. 100종목 검열
		if (updatedCodes != null && !updatedCodes.isEmpty()) {
			Set<String> updatedCodeSet = new HashSet<>(updatedCodes);

			List<StockInfoDTO> filteredCacheList = infoList.stream()
					.filter(dto -> updatedCodeSet.contains(dto.getStockCode())).toList();

			stockMemoryCache.refreshCache(filteredCacheList);

			return filteredCacheList.size();
		}
		return 0;
	}
}
