package com.frade.dto.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KiwoomTokenRequest {
	@JsonProperty("grant_type")
	String grantType;
	@JsonProperty("appkey")
	final String appkey;
	@JsonProperty("secretkey")
	final String secretkey;
	@JsonProperty("token")
	String token;

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