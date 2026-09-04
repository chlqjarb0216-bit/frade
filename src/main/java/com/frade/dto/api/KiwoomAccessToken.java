package com.frade.dto.api;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class KiwoomAccessToken {
	private String token;
	private String token_type;

	@JsonFormat(pattern = "yyyyMMddHHmmss")
	private LocalDateTime expiresDt = LocalDateTime.now();

	public boolean isActive() {
		return this.token != null && this.token_type != null
				&& !LocalDateTime.now().isAfter(expiresDt.minusMinutes(60));
	}

	public String toTypeToken() {
		return this.token_type + " " + this.token;
	}
}
