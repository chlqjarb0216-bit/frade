package com.frade.service.stock;

public interface StockDataBufferService {
	//실시간 데이터 버퍼에 구겨넣기
	public void enqueueRealtimeData(String data);
}
