package com.frade.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.frade.dto.api.KiwoomTokenRequest;

@Configuration
public class KiwoomApiConfig {

	@Value("${kiwoom.apikey}")
	String appkey;
	@Value("${kiwoom.secretkey}")
	String secretkey;

	@Bean
	@Scope("prototype")
	public KiwoomTokenRequest kiwoomTokenRequest() {
		return new KiwoomTokenRequest(appkey, secretkey);
	}
}
