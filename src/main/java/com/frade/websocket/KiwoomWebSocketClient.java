package com.frade.websocket;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.frade.memcache.StockMemoryCache;
import com.frade.service.api.KiwoomApiService;
import com.frade.service.stock.StockDataBufferService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KiwoomWebSocketClient extends WebSocketClient {

	private final ObjectMapper objectMapper;

	private final StockDataBufferService stockDataBufferService;
	private final KiwoomApiService kiwoomApiService;

	private final StockMemoryCache stockMemoryCache;

	private static final String SOCKET_URL = "wss://mockapi.kiwoom.com:10000/api/dostk/websocket"; // 접속 URL 

	public KiwoomWebSocketClient(ObjectMapper mapper, StockDataBufferService stockDataBufferService,
			KiwoomApiService kiwoomApiService, StockMemoryCache stockMemoryCache) throws URISyntaxException {
		super(new URI(SOCKET_URL));
		this.objectMapper = mapper;
		this.stockDataBufferService = stockDataBufferService;
		this.kiwoomApiService = kiwoomApiService;
		this.stockMemoryCache = stockMemoryCache;
	}

	//클라이언트 시동용 메서드
	public void boot() {
		if (this.isOpen()) {
			log.warn("이미 웹소켓 세션이 열려있습니다.");
			return;
		}

		if (this.getReadyState() == ReadyState.NOT_YET_CONNECTED) {
			log.info("최초 연결 가동 (connect)");
			this.connect();
		} else {
			log.info("재연결 (reconnect)");
			this.reconnect();
		}
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		log.info("서버와 연결되었습니다.");

		//토큰 확인
		String accessToken = kiwoomApiService.getOrRefreshAccessToken();

		// 로그인 메시지 전송
		ObjectNode loginMessage = objectMapper.createObjectNode();
		loginMessage.put("trnm", "LOGIN");
		loginMessage.put("token", accessToken);
		sendMessage(loginMessage);
	}

	@Override
	public void onMessage(String message) {
		try {
			ObjectNode response = (ObjectNode) objectMapper.readTree(message);
			String trnm = response.has("trnm") ? response.get("trnm").asText() : "";

			if ("LOGIN".equals(trnm)) {
				if (!"0".equals(response.get("return_code").asText())) {
					log.warn("로그인 실패: {}", response.get("return_msg").asText());
					close();
				} else {
					log.info("로그인 성공");
					registStockCodes();
				}
			} else if ("PING".equals(trnm)) {
				sendMessage(response); // 그대로 응답
			} else if ("REG".equals(trnm)) {
				//TODO
			} else if ("REAL".equals(trnm)) {
				ArrayNode dataArray = (ArrayNode) response.get("data");
				for (JsonNode node : dataArray) {
					stockDataBufferService.enqueueRealtimeData(node.toString());
				}
			}
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		//TODO
		log.info("연결 종료: {}", reason);
	}

	@Override
	public void onError(Exception ex) {
		//TODO
		log.warn("WebSocket 오류 발생: {}", ex.getMessage());
	}

	//전송실패시 처리 필요
	private void sendMessage(ObjectNode message) {
		if (this.isOpen()) {
			try {
				String jsonMessage = objectMapper.writeValueAsString(message);
				this.send(jsonMessage);
				log.info("Message sent: {}", jsonMessage);
			} catch (Exception e) {
				log.warn(e.getMessage());
			}
		}
	}

	//DB에 등록된 종목을 웹소켓에 요청
	private void registStockCodes() {
		List<String> cacheCodeList = stockMemoryCache.getCodeList();
		int stockCnt = cacheCodeList.size();
		log.info("DB에서 {}개 종목코드 로드.", stockCnt);

		final int CHUNK_SIZE = 10;

		log.info("총 {}개의 종목을 {}개씩 분할하여 구독 등록을 시작합니다.", stockCnt, CHUNK_SIZE);

		for (int i = 0; i < stockCnt; i += CHUNK_SIZE) {
			// 0.1초 대기 
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				log.error("분할 전송 대기 중 인터럽트 예외 발생. 전송을 중단합니다.");
				break;
			}

			//리스트 쪼개기
			List<String> chunk = cacheCodeList.subList(i, Math.min(i + CHUNK_SIZE, stockCnt));

			ObjectNode registerMessage = objectMapper.createObjectNode();
			registerMessage.put("trnm", "REG"); // 서비스명(REGISTER)
			registerMessage.put("grp_no", "1"); // 그룹번호
			registerMessage.put("refresh", "1"); // 기존등록유지여부(유지)

			//쪼갠 종목코드 리스트를 item 배열에 추가
			ArrayNode itemArray = objectMapper.valueToTree(cacheCodeList);
			// "type" 값을 배열로 설정
			ArrayNode typeArray = objectMapper.createArrayNode();
			typeArray.add("0B"); // 실시간 체결

			// 실시간 항목, 종목 jsonObject 등록 
			ObjectNode dataObject = objectMapper.createObjectNode();
			dataObject.set("item", itemArray);
			dataObject.set("type", typeArray);
			ArrayNode dataArray = objectMapper.createArrayNode();
			dataArray.add(dataObject);
			registerMessage.set("data", dataArray); // 실시간 등록 리스트

			// 등록 메세지 전송
			sendMessage(registerMessage);
			log.info("분할 전송 중... ({}/{}) - 묶음 크기: {}개", Math.min(i + CHUNK_SIZE, stockCnt), stockCnt, chunk.size());
		}
	}

}
