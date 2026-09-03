package com.frade.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResultCode {
	//성공:suc_ 으로 시작, 실패:rej_ 으로 시작
	//앞자리 0: 공통, 1: 회원가입, 2: 게시판, 3: 트레이딩, 더필요하면 추가

	//공통 영역
	SUCCESS("suc_001", "성공"), SUC_EMPTY("suc_002", "성공했으나 비어있음"), FAIL("rej_001", "실패"),

	//개인정보 영역
	DUP_ID("rej_101", "이미 사용 중인 아이디입니다."), DUP_NICK("rej_102", "이미 사용 중인 닉네임입니다."), DUP_EMAIL("rej_103", "이미 사용 중인 이메일입니다."),
	PW_NOT_MATCH("rej_104", "비밀번호 확인 불일치"),INVALID_PROFILE_FILE("rej_105", "지원하지 않는 프로필 사진 형식입니다."), 
	CURRENT_PW_NOT_MATCH("rej_106", "현재 비밀번호가 일치하지 않습니다."),PROFILE_FILE_SAVE_FAIL("rej_107","프로필 사진 저장에 실패했습니다."),
	INVALID_PASSWORD_INPUT("rej_108", "비밀번호를 모두 입력해주세요."),

	//게시판 영역
	COM_TEXT_FAIL("rej_200", "최대 글자수는 100자입니다.")

	//트레이딩 영역
	
	//추가

	;
	private final String code;
	private final String message;
}
