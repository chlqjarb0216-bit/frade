package com.frade.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResultCode {
	//성공:suc_ 으로 시작, 실패:rej_ 으로 시작
	//앞자리 0: 공통, 1: 회원가입, 2: 게시판, 3: 트레이딩, 더필요하면 추가

	//공통 영역
	SUCCESS("suc_001", "성공"),

	//회원가입 영역
	DUP_ID("rej_101", "아이디 중복"), DUP_NICK("rej_102", "닉네임 중복"), DUP_EMAIL("rej_103", "이메일 중복"),
	PW_NOT_MATCH("rej_104", "비밀번호 확인 불일치"),

	//게시판 영역
	SUC_NO_LIST("suc_202", "성공했으나 비어있음");

	//트레이딩 영역

	//추가

	private final String code;
	private final String message;
}
