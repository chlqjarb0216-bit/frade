package com.frade.service.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.frade.dto.api.StockInfoRawDTO;
import com.frade.service.api.KiwoomApiService;

@Service
public class KiwoomApiServiceImpl implements KiwoomApiService {

	@Override
	public String getOrRefreshAccessToken() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public List<StockInfoRawDTO> getMarketAllStockInfo() {
		// TODO Auto-generated method stub
		return new ArrayList<StockInfoRawDTO>();
	}

}
