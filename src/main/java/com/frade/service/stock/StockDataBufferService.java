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

	//내부적으로 stockBufferTaskExecutor에게 무한 루프를 돌며 큐를 처리하는 private 메서드 startConsumingWorker()를 올림.
	public void init();

	//버퍼에서 데이터를 꺼내 처리후 맵에 반영 및 chartService로 데이터 전달
	//처리한 데이터의 시간을 기억해두었다가 분이 바뀌면 5초뒤에 이전분의 시간문자열을 큐에 저장
	//private 메서드로 변경
	public void processRealtimeData(String stockJsonText);
}
