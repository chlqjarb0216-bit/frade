package com.frade.dto.rest;

import com.frade.common.ResultCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**

프론트엔드와 통신할 때 사용하는 공통 응답(JSON) 껍데기 객체입니다.*/
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED) // 외부 API응답에 사용할경우 파싱용
public class RestApiResponse<T> {

	private final String code; //응답코드(수정불가)
	private final String message; //응답메세지
	private final T data; // 실제 전달할 데이터 (DTO, List, Map 등 형태가 자유로움)

	// 1. 생성자: 외부에서 new ApiResponse()로 무분별하게 생성하는 것을 막기 위해 private으로 닫아둡니다.
	private RestApiResponse(String code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	// =================================================================
	// 2. 성공(Success) 응답을 위한 static 메서드들
	// =================================================================

	// 데이터 없이 '성공' 메시지만 보낼 때 (예: 단순 글 삭제 성공, 좋아요 성공)
	public static <T> RestApiResponse<T> success() {
		return new RestApiResponse<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
	}

	// 데이터를 함께 보낼 때 (예: 게시글 목록 조회, 상세 조회)
	public static <T> RestApiResponse<T> success(T data) {
		return new RestApiResponse<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
	}

	// =================================================================
	// 3. 에러(Error) 응답을 위한 static 메서드들
	// =================================================================

	// Enum에 정의된 에러 코드와 메시지를 그대로 보낼 때 (가장 많이 사용)
	public static <T> RestApiResponse<T> error(ResultCode resultCode) {
		return new RestApiResponse<>(resultCode.getCode(), resultCode.getMessage(), null);
	}

	// Enum의 코드는 쓰되, 에러 메시지만 상세하게 덮어씌워서 보낼 때 (예: 유효성 검사 실패 시 상세 이유)
	public static <T> RestApiResponse<T> error(ResultCode resultCode, String customMessage) {
		return new RestApiResponse<>(resultCode.getCode(), customMessage, null);
	}

	// =================================================================
	// 4. 범용 응답을 위한 static 메서드들
	// =================================================================
	public static <T> RestApiResponse<T> response(ResultCode resultCode, T data) {
		return new RestApiResponse<>(resultCode.getCode(), resultCode.getMessage(), data);
	}

}