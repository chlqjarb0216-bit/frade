package com.frade.dao.api.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.frade.common.stock.StockCommonFinalString;
import com.frade.dao.api.KiwoomApiRepository;
import com.frade.dto.api.KiwoomAccessToken;
import com.frade.dto.api.KiwoomIndexChartResponse;
import com.frade.dto.api.KiwoomStockChartResponse;
import com.frade.dto.api.KiwoomStockInfoResponse;
import com.frade.dto.api.KiwoomTokenRequest;
import com.frade.dto.api.StockInfoRawDTO;
import com.frade.dto.api.StockPriceRawDTO;
import com.frade.dto.stock.StockPriceDTO;
import com.frade.util.MarketUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class KiwoomApiRepositoryImpl implements KiwoomApiRepository {

	@Autowired
	private ObjectProvider<KiwoomTokenRequest> tokenRequestProvider;
	@Autowired
	private RestTemplate restTemplate;

	//토큰
	private KiwoomAccessToken accessToken;
	//도메인 주소
	//	private final String host = "https://mockapi.kiwoom.com"; // 모의투자
	private final String host = "https://api.kiwoom.com"; // 실전투자

	public KiwoomAccessToken getOrRefreshAccessToken() {
		if (this.accessToken == null || !this.accessToken.isActive()) {
			log.info("토큰 만료가 1시간 미만으로 남았거나 이미 폐기되었습니다.\n 토큰을 재발급합니다.");
			this.revokeToken();
			this.refreshAccessToken();
		}
		return this.accessToken;
	}

	public void revokeToken() {
		if (this.accessToken == null || this.accessToken.getToken() == null)
			return;

		final String revokeURL = this.host + "/oauth2/revoke";
		try {
			KiwoomTokenRequest tokenRequest = tokenRequestProvider.getObject()
					.toRevokeSpec(this.accessToken.getToken());

			restTemplate.postForObject(revokeURL, tokenRequest, JsonNode.class);
			log.info("토큰을 안전하게 폐기했습니다.");
		} catch (Exception e) {
			log.warn("토큰 폐기 실패", e.getMessage());
		}
	}

	@Override
	public List<StockInfoRawDTO> getMarketAllStockInfo() {
		KiwoomAccessToken token = this.getOrRefreshAccessToken();
		if (token == null) {
			log.error("토큰이 존재하지 않습니다");
			return Collections.emptyList();
		}

		//요청주소
		final String stockInfoURL = this.host + "/api/dostk/stkinfo";
		try {
			//헤더
			HttpHeaders headers = new HttpHeaders();
			headers.set("authorization", token.toTypeToken());
			headers.set("api-id", "ka10099");
			//바디(0: 코스피)
			Map<String, String> body = Map.of("mrkt_tp", "0");

			//요청 객체 조립
			HttpEntity<Object> entity = new HttpEntity<Object>(body, headers);

			// KiwoomStockInfoResponse.class 명세 덕분에 스프링이 Accept: application/json 을 자동으로 심어 통신합니다.
			ResponseEntity<KiwoomStockInfoResponse> responseEntity = restTemplate.exchange(stockInfoURL,
					HttpMethod.POST, entity, KiwoomStockInfoResponse.class);
			return responseEntity.getBody().getList();
		} catch (RestClientException e) {
			log.error("ka10099 종목정보 리스트 받아오는 중 에러발생: {}", e.getMessage());
		}
		return Collections.emptyList();
	}

	private void refreshAccessToken() {
		final String authURL = this.host + "/oauth2/token";
		try {
			KiwoomTokenRequest tokenRequest = tokenRequestProvider.getObject().toRefreshSpec();

			//POST 요청 실행: 응답 JSON 데이터 구조를 스프링+Jackson 콤비가 JsonNode 객체로 즉시 파싱해줍니다!
			KiwoomAccessToken response = restTemplate.postForObject(authURL, tokenRequest, KiwoomAccessToken.class);
			this.accessToken = response;
			log.info("토큰 발급 완료");
		} catch (RestClientException e) {
			log.error("키움 토큰 API 통신 실패. 인증 서버가 응답하지 않습니다. 원인: {}", e.getMessage());
			this.accessToken = null;
		} catch (Exception e) {
			log.error("토큰 가공 레이어 일반 예외 발생: {}", e.getMessage());
			this.accessToken = null;
		}
	}

	@Override
	public List<StockPriceDTO> getKOSPIChartDataByDay(String dayString) {
		LocalDateTime targetDateTime = LocalDateTime.parse(dayString, MarketUtil.DATE_FORM);
		KiwoomAccessToken token = this.getOrRefreshAccessToken();
		if (token == null) {
			log.error("토큰이 존재하지 않습니다");
			return Collections.emptyList();
		}
		String requestUrl = this.host + "/api/dostk/chart";
		List<StockPriceDTO> filteredTotalList = new ArrayList<>();

		// 1. 헤더 기본 셋팅 및 첫 요청 준비
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.set("authorization", token.toTypeToken());
		requestHeaders.set("api-id", "ka20005");
		//바디(001: 코스피, 1:1분봉)
		Map<String, String> requestBody = Map.of("inds_cd", "001", "tic_scope", "1", "base_dt", dayString);

		boolean hasNextPage = true;
		boolean isDateLimitReached = false; // 💡 날짜 리밋 도달 여부 플래그

		// 2. 다음 페이지가 있고, 날짜 제한에 도달하지 않았다면 무한 루프
		while (hasNextPage && !isDateLimitReached) {
			HttpEntity<Object> entity = new HttpEntity<>(requestBody, requestHeaders);

			// 앞서 살려낸 100% 순수 자동 매핑 교환 호출
			ResponseEntity<KiwoomIndexChartResponse> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST,
					entity, KiwoomIndexChartResponse.class);
			KiwoomIndexChartResponse body = responseEntity.getBody();

			if (body != null && body.getIndsMinPoleQry() != null) {
				for (StockPriceRawDTO raw : body.getIndsMinPoleQry()) {
					StockPriceDTO row = raw.toStockPriceDTO(StockCommonFinalString.KOSPI);
					if (row.getDateTime().isBefore(targetDateTime)) {
						log.info("[조기 종료] 기준 날짜({})보다 과거 데이터 감지: {}. 루프를 탈출합니다.", targetDateTime, row.getDateTime());
						isDateLimitReached = true;
						break; // 내부 foreach 문 탈출
					}

					// 기준 날짜 범위 내에 있는 정상 데이터만 최종 바구니에 저장
					filteredTotalList.add(row);
				}
			}

			// 3. 내부 루프에서 컷 당했다면 바깥 while 루프도 즉시 정지
			if (isDateLimitReached) {
				break;
			}

			// 4. 다음 페이지를 가기 위해 헤더 뜯어서 셋팅
			HttpHeaders responseHeaders = responseEntity.getHeaders();
			String contYn = responseHeaders.getFirst("cont-yn");
			String nextKey = responseHeaders.getFirst("next-key");

			if ("Y".equals(contYn) && nextKey != null && !nextKey.isEmpty()) {
				requestHeaders.set("cont-yn", contYn);
				requestHeaders.set("next-key", nextKey); // 다음 페이지 키로 스위칭

				// ⏳ 과부하로 인한 키움 계정 차단 방지를 위한 미세한 휴식 시간 추가
				try {
					Thread.sleep(200);
				} catch (InterruptedException ignored) {
				}
			} else {
				hasNextPage = false; // 키움 쪽에 더 이상 데이터가 없음
			}
		}

		if (!filteredTotalList.isEmpty()) {
			Collections.reverse(filteredTotalList);
		}

		log.info("[연속조회 종료] 수집 완료된 최종 데이터 개수: {}개", filteredTotalList.size());
		return filteredTotalList;
	}

	@Override
	public List<StockPriceDTO> getKOSPIChartDataFromLastData(LocalDateTime last) {
		KiwoomAccessToken token = this.getOrRefreshAccessToken();
		if (token == null) {
			log.error("토큰이 존재하지 않습니다");
			return Collections.emptyList();
		}
		String requestUrl = this.host + "/api/dostk/chart";
		List<StockPriceDTO> filteredTotalList = new ArrayList<>();

		// 1. 헤더 기본 셋팅 및 첫 요청 준비
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.set("authorization", token.toTypeToken());
		requestHeaders.set("api-id", "ka20005");
		//바디(001: 코스피, 1:1분봉)
		Map<String, String> requestBody = Map.of("inds_cd", "001", "tic_scope", "1");

		boolean hasNextPage = true;
		boolean isDateLimitReached = false; // 💡 날짜 리밋 도달 여부 플래그

		// 2. 다음 페이지가 있고, 날짜 제한에 도달하지 않았다면 무한 루프
		while (hasNextPage && !isDateLimitReached) {
			HttpEntity<Object> entity = new HttpEntity<>(requestBody, requestHeaders);

			// 앞서 살려낸 100% 순수 자동 매핑 교환 호출
			ResponseEntity<KiwoomIndexChartResponse> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST,
					entity, KiwoomIndexChartResponse.class);

			KiwoomIndexChartResponse body = responseEntity.getBody();
			if (body != null && body.getIndsMinPoleQry() != null) {
				for (StockPriceRawDTO raw : body.getIndsMinPoleQry()) {
					StockPriceDTO row = raw.toStockPriceDTO(StockCommonFinalString.KOSPI);
					if (row.getDateTime().isBefore(last)) {
						isDateLimitReached = true;
						break; // 내부 foreach 문 탈출
					}

					// 기준 날짜 범위 내에 있는 정상 데이터만 최종 바구니에 저장
					filteredTotalList.add(row);
				}
			}

			// 3. 내부 루프에서 컷 당했다면 바깥 while 루프도 즉시 정지
			if (isDateLimitReached) {
				break;
			}

			// 4. 다음 페이지를 가기 위해 헤더 뜯어서 셋팅
			HttpHeaders responseHeaders = responseEntity.getHeaders();
			String contYn = responseHeaders.getFirst("cont-yn");
			String nextKey = responseHeaders.getFirst("next-key");

			if ("Y".equals(contYn) && nextKey != null && !nextKey.isEmpty()) {
				requestHeaders.set("cont-yn", contYn);
				requestHeaders.set("next-key", nextKey); // 다음 페이지 키로 스위칭

				// ⏳ 과부하로 인한 키움 계정 차단 방지를 위한 미세한 휴식 시간 추가
				try {
					Thread.sleep(200);
				} catch (InterruptedException ignored) {
				}
			} else {
				hasNextPage = false; // 키움 쪽에 더 이상 데이터가 없음
			}
		}

		if (!filteredTotalList.isEmpty()) {
			Collections.reverse(filteredTotalList);
		}

		log.info("[연속조회 종료] 수집 완료된 최종 데이터 개수: {}개", filteredTotalList.size());
		return filteredTotalList;
	}

	@Override
	public List<StockPriceDTO> getStockChartDataByDay(String stockCode, String dayString) {
		LocalDateTime targetDateTime = LocalDateTime.parse(dayString, MarketUtil.DATE_FORM);
		KiwoomAccessToken token = this.getOrRefreshAccessToken();
		if (token == null) {
			log.error("토큰이 존재하지 않습니다");
			return Collections.emptyList();
		}
		String requestUrl = this.host + "/api/dostk/chart";
		List<StockPriceDTO> filteredTotalList = new ArrayList<>();

		// 1. 헤더 기본 셋팅 및 첫 요청 준비
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.set("authorization", token.toTypeToken());
		requestHeaders.set("api-id", "ka10080");
		//바디(001: 코스피, 1:1분봉)
		Map<String, String> requestBody = Map.of("stk_cd", stockCode, "tic_scope", "1", "upd_stkpc_tp", "1", "base_dt",
				dayString);

		boolean hasNextPage = true;
		boolean isDateLimitReached = false; // 💡 날짜 리밋 도달 여부 플래그

		// 2. 다음 페이지가 있고, 날짜 제한에 도달하지 않았다면 무한 루프
		while (hasNextPage && !isDateLimitReached) {
			HttpEntity<Object> entity = new HttpEntity<>(requestBody, requestHeaders);

			// 앞서 살려낸 100% 순수 자동 매핑 교환 호출
			ResponseEntity<KiwoomStockChartResponse> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST,
					entity, KiwoomStockChartResponse.class);
			KiwoomStockChartResponse body = responseEntity.getBody();

			if (body != null && body.getStkMinPoleChartQry() != null) {
				for (StockPriceRawDTO raw : body.getStkMinPoleChartQry()) {
					StockPriceDTO row = raw.toStockPriceDTO(stockCode);
					if (row.getDateTime().isBefore(targetDateTime)) {
						log.info("[조기 종료] 기준 날짜({})보다 과거 데이터 감지: {}. 루프를 탈출합니다.", targetDateTime, row.getDateTime());
						isDateLimitReached = true;
						break; // 내부 foreach 문 탈출
					}

					// 기준 날짜 범위 내에 있는 정상 데이터만 최종 바구니에 저장
					filteredTotalList.add(row);
				}
			}

			// 3. 내부 루프에서 컷 당했다면 바깥 while 루프도 즉시 정지
			if (isDateLimitReached) {
				break;
			}

			// 4. 다음 페이지를 가기 위해 헤더 뜯어서 셋팅
			HttpHeaders responseHeaders = responseEntity.getHeaders();
			String contYn = responseHeaders.getFirst("cont-yn");
			String nextKey = responseHeaders.getFirst("next-key");

			if ("Y".equals(contYn) && nextKey != null && !nextKey.isEmpty()) {
				requestHeaders.set("cont-yn", contYn);
				requestHeaders.set("next-key", nextKey); // 다음 페이지 키로 스위칭

				// ⏳ 과부하로 인한 키움 계정 차단 방지를 위한 미세한 휴식 시간 추가
				try {
					Thread.sleep(200);
				} catch (InterruptedException ignored) {
				}
			} else {
				hasNextPage = false; // 키움 쪽에 더 이상 데이터가 없음
			}
		}

		if (!filteredTotalList.isEmpty()) {
			Collections.reverse(filteredTotalList);
		}

		log.info("[연속조회 종료] 수집 완료된 최종 데이터 개수: {}개", filteredTotalList.size());
		return filteredTotalList;
	}

	@Override
	public List<StockPriceDTO> getStockChartDataFromLastData(String stockCode, LocalDateTime last) {
		KiwoomAccessToken token = this.getOrRefreshAccessToken();
		if (token == null) {
			log.error("토큰이 존재하지 않습니다");
			return Collections.emptyList();
		}
		String requestUrl = this.host + "/api/dostk/chart";
		List<StockPriceDTO> filteredTotalList = new ArrayList<>();

		// 1. 헤더 기본 셋팅 및 첫 요청 준비
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.set("authorization", token.toTypeToken());
		requestHeaders.set("api-id", "ka10080");
		//바디(001: 코스피, 1:1분봉)
		Map<String, String> requestBody = Map.of("stk_cd", stockCode, "tic_scope", "1", "upd_stkpc_tp", "1");

		boolean hasNextPage = true;
		boolean isDateLimitReached = false; // 💡 날짜 리밋 도달 여부 플래그

		// 2. 다음 페이지가 있고, 날짜 제한에 도달하지 않았다면 무한 루프
		while (hasNextPage && !isDateLimitReached) {
			HttpEntity<Object> entity = new HttpEntity<>(requestBody, requestHeaders);

			// 앞서 살려낸 100% 순수 자동 매핑 교환 호출
			ResponseEntity<KiwoomStockChartResponse> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST,
					entity, KiwoomStockChartResponse.class);
			KiwoomStockChartResponse body = responseEntity.getBody();

			if (body != null && body.getStkMinPoleChartQry() != null) {
				for (StockPriceRawDTO raw : body.getStkMinPoleChartQry()) {
					StockPriceDTO row = raw.toStockPriceDTO(stockCode);
					if (row.getDateTime().isBefore(last)) {
						log.info("[조기 종료] 기준 날짜({})보다 과거 데이터 감지: {}. 루프를 탈출합니다.", last, row.getDateTime());
						isDateLimitReached = true;
						break; // 내부 foreach 문 탈출
					}

					// 기준 날짜 범위 내에 있는 정상 데이터만 최종 바구니에 저장
					filteredTotalList.add(row);
				}
			}

			// 3. 내부 루프에서 컷 당했다면 바깥 while 루프도 즉시 정지
			if (isDateLimitReached) {
				break;
			}

			// 4. 다음 페이지를 가기 위해 헤더 뜯어서 셋팅
			HttpHeaders responseHeaders = responseEntity.getHeaders();
			String contYn = responseHeaders.getFirst("cont-yn");
			String nextKey = responseHeaders.getFirst("next-key");

			if ("Y".equals(contYn) && nextKey != null && !nextKey.isEmpty()) {
				requestHeaders.set("cont-yn", contYn);
				requestHeaders.set("next-key", nextKey); // 다음 페이지 키로 스위칭

				// ⏳ 과부하로 인한 키움 계정 차단 방지를 위한 미세한 휴식 시간 추가
				try {
					Thread.sleep(200);
				} catch (InterruptedException ignored) {
				}
			} else {
				hasNextPage = false; // 키움 쪽에 더 이상 데이터가 없음
			}
		}

		if (!filteredTotalList.isEmpty()) {
			Collections.reverse(filteredTotalList);
		}

		log.info("[연속조회 종료] 수집 완료된 최종 데이터 개수: {}개", filteredTotalList.size());
		return filteredTotalList;
	}

}
