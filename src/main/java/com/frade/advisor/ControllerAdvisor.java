package com.frade.advisor;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(1)
@ControllerAdvice
public class ControllerAdvisor {
	// 💡 404 에러 전용 핸들러
	@ExceptionHandler(NoHandlerFoundException.class)
	public String handle404(NoHandlerFoundException e, Model model) {
		log.warn("[404 에러 발생] 존재하지 않는 URL 요청: {}", e.getRequestURL());

		model.addAttribute("errorMessage", "요청하신 페이지를 찾을 수 없습니다.");
		return "error/404"; // src/main/resources/templates/error/404.html 등으로 이동
	}
}

//2. [화면 컨트롤러 전용] 일반 화면 단에서 터진 예외만 따로 잡는 어드바이저
@Slf4j
@ControllerAdvice(annotations = Controller.class) // 💡 다시 필터를 걸어줍니다.
class HtmlExceptionAdvisor {
	// 💡 1. 클라이언트가 연결을 끊어서 발생한 예외 처리
	@ExceptionHandler({ IOException.class, org.apache.catalina.connector.ClientAbortException.class })
	public void handleClientAbortException(Exception e) {
		// 이미 사용자가 떠났으므로 화면(String)을 리턴하지 않고 void로 메서드를 종료합니다.
		// 이 에러는 서버 장애가 아니므로 디버깅용으로 WARN이나 INFO 수준으로 남기는 것이 좋습니다.
		log.warn("[클라이언트 통신 중단] 사용자가 요청 중 브라우저를 닫았거나 취소함: {}", e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public String handleHtmlException(Exception e, Model model) {
		log.error("[WEB 화면 오류] URL 처리 중 예외 발생: {}", e.getMessage());
		model.addAttribute("errorMessage", "일시적인 오류가 발생했습니다.");
		return "error/generic";
	}
}
