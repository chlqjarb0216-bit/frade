package com.frade.advisor;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice(annotations = Controller.class)
public class ControllerAdvisor {
	// 💡 1. 클라이언트가 연결을 끊어서 발생한 예외 처리
	@ExceptionHandler({ IOException.class, org.apache.catalina.connector.ClientAbortException.class })
	public void handleClientAbortException(Exception e) {
		// 이미 사용자가 떠났으므로 화면(String)을 리턴하지 않고 void로 메서드를 종료합니다.
		// 이 에러는 서버 장애가 아니므로 디버깅용으로 WARN이나 INFO 수준으로 남기는 것이 좋습니다.
		log.warn("[클라이언트 통신 중단] 사용자가 요청 중 브라우저를 닫았거나 취소함: {}", e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public String handleHtmlException(Exception e, Model model) {
		// 💡 서버 콘솔에 에러의 구체적인 내용과 발생 위치(t)를 남깁니다.
		log.error("[WEB 화면 오류] URL 처리 중 예외 발생: {}", e.getMessage(), e);

		model.addAttribute("errorMessage", "요청을 처리하는 중 오류가 발생했습니다.");
		return "error/generic";
	}
}
