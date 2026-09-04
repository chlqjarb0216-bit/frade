package com.frade.dao.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.frade.dao.api.KiwoomApiRepository;
import com.frade.dto.api.KiwoomAccessToken;
import com.frade.dto.api.KiwoomTokenRequest;
import com.frade.dto.api.StockInfoRawDTO;

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
	private final String host = "https://mockapi.kiwoom.com"; // 모의투자
	//private final String host = "https://api.kiwoom.com"; // 실전투자

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
			return new ArrayList<>();
		}

		//요청주소
		final String stockInfoURL = this.host + "/api/dostk/stkinfo";
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + this.accessToken);
		headers.set("Accept", "application/json");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		return null;
	}

	private void refreshAccessToken() {
		final String authURL = this.host + "/oauth2/token";
		try {
			KiwoomTokenRequest tokenRequest = tokenRequestProvider.getObject().toRefreshSpec();

			//[POST 요청 실행]: 응답 JSON 데이터 구조를 스프링+Jackson 콤비가 JsonNode 객체로 즉시 파싱해줍니다!
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

}
