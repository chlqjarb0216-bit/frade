package com.frade.dto.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserLoginDTO {

	@NotBlank(message = "아이디를 입력해주세요.")
	@Size(min = 6, max = 16,
	      message = "아이디는 6~16자로 입력해주세요.")
	@Pattern(
	    regexp = "^[a-zA-Z0-9]+$",
	    message = "아이디는 영문과 숫자만 사용할 수 있습니다."
	)
	String userId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    String userPw;
}