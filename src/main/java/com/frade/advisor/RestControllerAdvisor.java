package com.frade.advisor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.frade.common.ResultCode;
import com.frade.dto.rest.RestApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class RestControllerAdvisor {
	@ExceptionHandler(Exception.class)
	public ResponseEntity<RestApiResponse<String>> handleApiException(Exception e) {
		log.error("[API 오류] 요청 처리 실패: {}", e.getMessage(), e);
		RestApiResponse<String> response = RestApiResponse.customResponse(ResultCode.FAIL, "API_ERROR",
				"서버 내부 오류가 발생했습니다.");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
}
