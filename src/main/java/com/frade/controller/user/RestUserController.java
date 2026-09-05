package com.frade.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.frade.common.ResultCode;
import com.frade.dto.rest.RestApiResponse;
import com.frade.service.user.UserService;

@RestController
@RequestMapping("/api/user")
public class RestUserController {
	
	
	@Autowired
    UserService userService;


    // 아이디 중복 확인
    @PostMapping("/checkId")
    public RestApiResponse<Void> checkUserId(
            @RequestBody String userId) {

        boolean result =
                userService.checkUserId(userId);

        if(result) {
            return RestApiResponse.error(
                    ResultCode.DUP_ID);
        }

        return RestApiResponse.success();
    }


    // 닉네임 중복 확인
    @PostMapping("/checkNick")
    public RestApiResponse<Void> checkUserNick(
            @RequestBody String userNick) {

        boolean result =
                userService.checkUserNick(userNick);

        if(result) {
            return RestApiResponse.error(
                    ResultCode.DUP_NICK);
        }

        return RestApiResponse.success();
    }


    // 이메일 중복 확인
    @PostMapping("/checkEmail")
    public RestApiResponse<Void> checkUserEmail(
            @RequestBody String userEmail) {

        boolean result =
                userService.checkUserEmail(userEmail);

        if(result) {
            return RestApiResponse.error(
                    ResultCode.DUP_EMAIL);
        }

        return RestApiResponse.success();
    }
}


