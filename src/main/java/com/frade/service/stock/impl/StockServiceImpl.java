package com.frade.service.stock.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frade.dao.stock.StockDAO;
import com.frade.dao.stock.StockPriceDAO;
import com.frade.dto.stock.StockInfoDTO;
import com.frade.dto.stock.StockPreviewDTO;
import com.frade.dto.stock.StockPriceDTO;
import com.frade.memcache.StockMemoryCache;
import com.frade.memcache.StockRankingCache;
import com.frade.service.api.KiwoomApiService;
import com.frade.service.stock.StockService;

@Service
public class StockServiceImpl implements StockService {

	@Autowired
	KiwoomApiService kiwoomApiService;

	@Autowired
	StockDAO stockDAO;
	@Autowired
	StockPriceDAO stockPriceDAO;

	@Autowired
	StockMemoryCache stockMemoryCache;
	@Autowired
	StockRankingCache stockRankingCache;

	@Override
	public List<StockInfoDTO> searchStockByName(String stockName) {
		return stockMemoryCache.searchByName(stockName);
	}

	@Override
	public StockPreviewDTO getStockPreviewByStockCode(String stockCode) {
		return stockRankingCache.getPreviewByStockCode(stockCode);
	}

	@Override
	public List<StockPreviewDTO> getSortedStockRankingListPage(int pageIdx, int pageSize) {
		return stockRankingCache.getSortedCachePage(pageIdx, pageSize);
	}

	@Override
	public int saveMinuteStockPrice(List<StockPriceDTO> stockPriceList) {
		return stockPriceDAO.insertMinuteStockPrice(stockPriceList);
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
