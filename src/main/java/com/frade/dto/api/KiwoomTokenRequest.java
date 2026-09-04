package com.frade.dto.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
@Component
@Scope("prototype") // 호출될 때마다 매번 새로 생성되도록 설정
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KiwoomTokenRequest {

	private String grantType;
	private final String appkey;
	private final String secretkey;
	private String token;

	// 스프링이 이 객체를 생성할 때 프로퍼티 값을 딱 꽂아줍니다.
	@Autowired
	public KiwoomTokenRequest(@Value("${kiwoom.apikey}") String appkey,
			@Value("${kiwoom.secretkey}") String secretkey) {
		this.appkey = appkey;
		this.secretkey = secretkey;
	}

	// 토큰 발급용 스펙으로 변환 (token은 null이므로 Jackson이 검열)
	public KiwoomTokenRequest toRefreshSpec() {
		this.grantType = "client_credentials";
		return this;
	}

	// 토큰 폐기용 스펙으로 변환 (grantType은 null이므로 Jackson이 검열)
	public KiwoomTokenRequest toRevokeSpec(String token) {
		this.token = token;
		return this;
	}

}