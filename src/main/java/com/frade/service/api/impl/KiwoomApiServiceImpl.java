package com.frade.service.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frade.dao.api.KiwoomApiRepository;
import com.frade.dto.api.StockInfoRawDTO;
import com.frade.dto.stock.StockInfoDTO;
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
		// TODO Auto-generated method stub

	}

	@Override
	public List<StockInfoDTO> getMarketAllStockInfo() {
		List<StockInfoRawDTO> rawInfoList = kiwoomApiRepository.getMarketAllStockInfo();
		List<StockInfoDTO> infoList = rawInfoList.stream().map(rawInfo -> rawInfo.toStockInfo()).toList();
		return infoList;
	}

}
