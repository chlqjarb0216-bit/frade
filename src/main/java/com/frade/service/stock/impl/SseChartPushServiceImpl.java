package com.frade.service.stock.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.frade.common.ResultCode;
import com.frade.dto.Response;
import com.frade.dto.stock.StockPriceDTO;
import com.frade.service.stock.SseChartPushService;

@Service
public class SseChartPushServiceImpl implements SseChartPushService {

	@Override
	public SseEmitter subscribeStockChart(String stockCode, String browserSessionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<String> switchChartRoom(String browserSessionId, String newStockCode) {
		// TODO Auto-generated method stub
		return Response.error(ResultCode.SAME_STOCK_CODE);
	}

	@Override
	public void pushChartToSse(String stockCode, StockPriceDTO currentDTO, String currentMinuteStr) {
		// TODO Auto-generated method stub

	}

}
