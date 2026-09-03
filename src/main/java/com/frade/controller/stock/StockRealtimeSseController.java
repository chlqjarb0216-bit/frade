package com.frade.controller.stock;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.frade.common.ResultCode;
import com.frade.dto.Response;
import com.frade.service.stock.SseChartPushService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/stock/api")
@RequiredArgsConstructor
public class StockRealtimeSseController {

	private final SseChartPushService sseChartPushService;

	/**
	 * 💡 1. [실시간 1분 봉 차트 SSE 스트리밍 통로]
	 * 프론트엔드가 EventSource("/api/stock/stream/005930")를 열면 이 파이프라인이 개방됩니다.
	 * 무거운 웹소켓 대신 text/event-stream 규격의 가벼운 단방향 스트링 푸시가 작동합니다.
	 */
	@GetMapping(value = "/stream/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter connectChartStream(@RequestParam String stockCode,
			@RequestParam(required = false) String browserSessionId) {

		log.info("프론트엔드 실시간 차트 연결 인입 ➔ 종목코드: {}", stockCode);

		// 💡 서비스 내부에서 CopyOnWriteArrayList 명부에 다중 세션을 안전하게 누적하고 Emitter를 리턴합니다.
		return sseChartPushService.subscribeStockChart(stockCode, browserSessionId);
	}

	@PostMapping("/stream/switch-room/{browserSessionId}")
	public void switchChartRoom(@PathVariable String browserSessionId, @RequestParam String newStockCode) {
		Response<String> result = sseChartPushService.switchChartRoom(browserSessionId, newStockCode);
		if (result.getResultCode() == ResultCode.SAME_STOCK_CODE) {
			log.info(result.getResultCode().getMessage());
			return;
		}
		log.info("🔄 [구독 전환] UUID [{}] 유저가 [{}]에서 ➔ [{}] 차트 방으로 짐 싸서 스위칭 완료.", browserSessionId, result.getData(),
				newStockCode);
	}

	/*
	
	*/
}