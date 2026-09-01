package com.frade.service.stock.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.frade.dto.stock.StockInfoDTO;
import com.frade.dto.stock.StockPriceDTO;
import com.frade.service.stock.StockService;

@Service
public class StockServiceImpl implements StockService {

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
