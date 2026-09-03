package com.frade.service.stock;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.frade.dto.Response;
import com.frade.dto.stock.StockPriceDTO;

//멤버로 ObjectMapper와 연결된 종목, 구독 리스트를 저장할 해시맵, 종목마다 마지막으로 쏴준 시간을 저장할 해시맵, 종목마다 화면에 미반영된 마지막 데이터를 저장할 해시맵

public interface SseChartPushService {
	//종목 실시간 차트 구독
	public SseEmitter subscribeStockChart(String stockCode, String browserSessionId);

	public Response<String> switchChartRoom(String browserSessionId, String newStockCode);

	//StockDataBufferService가 처리해서 넘겨준 데이터를 화면으로 쏴주는 메서드
	//0.5초 제한, 마지막으로 쏴 주고나서 0.5초이내에 데이터가 들어오면 저장해두고 쏴주지는 않음, 그 이전 데이터는 삭제
	//분이 바뀌면 저장된 데이터와 새로 들어온 데이터를 둘다 쏴줌	
	public void pushChartToSse(String stockCode, StockPriceDTO currentDTO, String currentMinuteStr);
}
