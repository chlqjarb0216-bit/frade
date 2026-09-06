package com.frade.service.stock;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.frade.dto.Response;
import com.frade.dto.event.RealtimeStockEvent;

//멤버로 ObjectMapper와 연결된 종목, 구독 리스트를 저장할 해시맵, 종목마다 마지막으로 쏴준 시간을 저장할 해시맵, 종목마다 화면에 미반영된 마지막 데이터를 저장할 해시맵

public interface SseChartPushService {
	//종목 실시간 차트 구독
	public SseEmitter subscribeStockChart(String stockCode, String browserSessionId);

	public Response<String> switchChartRoom(String browserSessionId, String newStockCode);

	public void handleRealtimeStockEvent(RealtimeStockEvent event);
}
