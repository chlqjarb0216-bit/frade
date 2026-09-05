package com.frade.service.stock;

import java.util.List;
import java.util.Map;

import com.frade.dto.stock.StockPriceDTO;

public interface StockDataBufferService {
	//실시간 데이터 버퍼에 구겨넣기
	public void enqueueRealtimeData(String data);

	//준비완료된 시간문자열을 큐에서 꺼내 해당하는 데이터를 맵에서 찾아 리스트로 반환
	public List<StockPriceDTO> flushCompleteMinuteBuffer();

	//현재 최신 종가를 종목코드와 함께 반환
	public Map<String, Integer> getMinPriceSnapshotMap();

	//종목의 최신 종가를 반환
	public int getMinPriceSnapshotByStockCode(String stockCode);
}
