package com.frade.service.stock.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.frade.dto.stock.StockPriceDTO;
import com.frade.service.stock.StockDataBufferService;

@Service
public class StockDataBufferServiceImpl implements StockDataBufferService {

	@Override
	public void enqueueRealtimeData(String data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processRealtimeData(String stockJsonText) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<StockPriceDTO> flushCompleteMinuteBuffer() {
		// TODO Auto-generated method stub
		return new ArrayList<StockPriceDTO>();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, Integer> getMinPriceSnapshotMap() {
		// TODO Auto-generated method stub
		return new HashMap<>();
	}

}
