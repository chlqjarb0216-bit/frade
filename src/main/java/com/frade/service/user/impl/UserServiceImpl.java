package com.frade.service.user.impl;

import org.springframework.stereotype.Service;

import com.frade.common.ResultCode;
import com.frade.dto.user.UserSignDTO;
import com.frade.service.user.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Override
	public int userLogin(UserSignDTO userSignDTO) {
		
		// 로그인 확인용 아이디 비밀번호 아이디:test 비번:1234
		
		if("test".equals(userSignDTO.getUserId()) && "1234".equals(userSignDTO.getUserPw()) ) {
			
			return 1;
			
		}
		
		
		return -1;
	}

	@Override
	public boolean checkUserId(String userId) {
		
		if("test".equals(userId)) {
			return true;
		}
		
		return false;
	}

	@Override
	public boolean checkUserNick(String userNick) {
		 if ("홍명보".equals(userNick)) {
		        return true;
		    }

		    return false;
	}
	
	@Override
	public boolean checkUserEmail(String userEmail) {
		
		if("test@test.com".equals(userEmail)) {
			return true;
		}
		
		return false;
	}

	@Override
	public ResultCode userSignup(UserSignDTO userSignDTO) {

	    boolean idResult = checkUserId(userSignDTO.getUserId());
	    	
	    if(idResult) {
	        return ResultCode.DUP_ID;
	    }

	    boolean nickResult = checkUserNick(userSignDTO.getUserNick());

	    if(nickResult) {
	        return ResultCode.DUP_NICK;
	    }

	    boolean emailResult = checkUserEmail(userSignDTO.getUserEmail());
	    
	    if(emailResult) {
	        return ResultCode.DUP_EMAIL;
	    }

	    System.out.println("회원가입 정보 : " + userSignDTO);

	    return ResultCode.SUCCESS;
		}
	
	

	
	

	
	

	
	

}
