package com.frade.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class UserSignupDTO {
	
	 @NotBlank(message = "아이디를 입력해주세요.")
	    @Size(min = 6, max = 16, message = "아이디는 6~16자로 입력해주세요.")
	    @Pattern(
	        regexp = "^[a-zA-Z0-9]+$",
	        message = "아이디는 영문과 숫자만 사용할 수 있습니다."
	    )
	    String userId;


	    @NotBlank(message = "닉네임을 입력해주세요.")
	    @Size(min = 2, max = 16, message = "닉네임은 2~16자로 입력해주세요.")
	    @Pattern(
	        regexp = "^[가-힣a-zA-Z0-9]+$",
	        message = "닉네임은 한글, 영문, 숫자만 사용할 수 있습니다."
	    )
	    String userNick;


	    @NotBlank(message = "이메일을 입력해주세요.")
	    @Email(message = "이메일 형식을 확인해주세요.")
	    @Size(max = 320, message = "이메일은 320자 이하로 입력해주세요.")
	    String userEmail;


	    @NotBlank(message = "비밀번호를 입력해주세요.")
	    @Size(min = 10, max = 20, message = "비밀번호는 10~20자로 입력해주세요.")
	    @Pattern(
	        regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9가-힣\\s])[^가-힣\\s]+$",
	        message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다."
	    )
	    String userPw;
	}


