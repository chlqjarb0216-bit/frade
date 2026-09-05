package com.frade.dto.api;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KiwoomAccessToken {
	@JsonProperty("token")
	String token;
	@JsonProperty("token_type")
	String tokenType;

	@JsonProperty("expires_dt")
	@JsonFormat(pattern = "yyyyMMddHHmmss")
	LocalDateTime expiresDt = LocalDateTime.now();

	public boolean isActive() {
		return this.token != null && this.tokenType != null && !LocalDateTime.now().isAfter(expiresDt.minusMinutes(60));
	}

	public String toTypeToken() {
		return this.tokenType + " " + this.token;
	}
}
