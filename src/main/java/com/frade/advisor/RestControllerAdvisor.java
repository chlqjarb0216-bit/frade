package com.frade.advisor;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.frade.common.ResultCode;
import com.frade.dto.rest.RestApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class RestControllerAdvisor {
	@ExceptionHandler({ IOException.class, org.apache.catalina.connector.ClientAbortException.class })
	public void handleClientAbortException(Exception e) {
		// 이미 사용자가 떠났으므로 화면(String)을 리턴하지 않고 void로 메서드를 종료합니다.
		// 이 에러는 서버 장애가 아니므로 디버깅용으로 WARN이나 INFO 수준으로 남기는 것이 좋습니다.
		log.warn("[클라이언트 통신 중단] 사용자가 요청 중 브라우저를 닫았거나 취소함: {}", e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<RestApiResponse<String>> handleApiException(Exception e) {
		log.error("[API 오류] 요청 처리 실패: {}", e.getMessage(), e);
		RestApiResponse<String> response = RestApiResponse.customResponse(ResultCode.FAIL, "API_ERROR",
				"서버 내부 오류가 발생했습니다.");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
}
