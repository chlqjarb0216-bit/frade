package com.frade.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResultCode {
	//성공:suc_ 으로 시작, 실패:rej_ 으로 시작
	//앞자리 0: 공통, 1: 회원가입, 2: 게시판, 3: 트레이딩, 4: 차트영역 더필요하면 추가

	//공통 영역
	SUCCESS("suc_001", "성공"), SUC_EMPTY("suc_002", "성공했으나 비어있음"), FAIL("rej_001", "실패"),

	//회원가입 영역
	DUP_ID("rej_101", "이미 사용 중인 아이디입니다."), DUP_NICK("rej_102", "이미 사용 중인 닉네임입니다."),
	DUP_EMAIL("rej_103", "이미 사용 중인 이메일입니다."), PW_NOT_MATCH("rej_104", "비밀번호 확인 불일치"),

	//게시판 영역

	//트레이딩 영역

	//차트 영역
	SAME_STOCK_CODE("rej_401", "이미 구독중인 차트입니다.")

	//추가

	;

	private final String code;
	private final String message;
}
