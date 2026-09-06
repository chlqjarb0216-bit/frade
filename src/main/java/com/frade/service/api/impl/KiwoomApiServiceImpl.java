package com.frade.service.api.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frade.dao.api.KiwoomApiRepository;
import com.frade.dto.api.StockInfoRawDTO;
import com.frade.dto.stock.StockInfoDTO;
import com.frade.dto.stock.StockPriceDTO;
import com.frade.service.api.KiwoomApiService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KiwoomApiServiceImpl implements KiwoomApiService {

	@Autowired
	KiwoomApiRepository kiwoomApiRepository;

	public String getOrRefreshAccessTokenString() {
		return kiwoomApiRepository.getOrRefreshAccessToken().getToken();
	}

	@Override
	public void revokeToken() {
		kiwoomApiRepository.revokeToken();
	}

	@Override
	public List<StockInfoDTO> getMarketAllStockInfo() {
		List<StockInfoRawDTO> rawInfoList = kiwoomApiRepository.getMarketAllStockInfo();
		List<StockInfoDTO> infoList = rawInfoList.stream().map(rawInfo -> rawInfo.toStockInfo()).toList();
		return infoList;
	}

	@Override
	public List<StockPriceDTO> getKOSPIChartDataByDay(String dayString) {
		return kiwoomApiRepository.getKOSPIChartDataByDay(dayString);
	}

	@Override
	public List<StockPriceDTO> getKOSPIChartDataFromLastData(LocalDateTime last) {
		return kiwoomApiRepository.getKOSPIChartDataFromLastData(last);
	}

	@Override
	public List<StockPriceDTO> getStockChartDataByDay(String stockCode, String dayString) {
		return kiwoomApiRepository.getStockChartDataByDay(stockCode, dayString);
	}

}
