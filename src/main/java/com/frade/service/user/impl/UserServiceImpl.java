package com.frade.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frade.dto.user.UserSignDTO;
import com.frade.service.user.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Override
	public int userLogin(UserSignDTO userSignDTO) {
		
		
		
		return -1;
	}

	@Override
	public String checkUserId(String uId) {
		
		if("test".equals(uId)) {
			return "Y";
		}
		
		return "N";
	}

	@Override
	public String checkUserNick(String uNick) {
		 if ("홍명보".equals(uNick)) {
		        return "Y";
		    }

		    return "N";
	}

	@Override
	public int userSignup(UserSignDTO userSignDTO) {

	    String idResult = checkUserId(userSignDTO.getUId());

		    if(idResult.equals("Y")) {
		        return 1;
		    }

		    String nickResult = checkUserNick(userSignDTO.getUNick());

		    if(nickResult.equals("Y")) {
		        return 2;
		    }

		    String emailResult = checkUserEmail(userSignDTO.getUEmail());
		    
		    if(emailResult.equals("Y")) {
		        return 3;
		    }

		    System.out.println("회원가입 정보 : " + userSignDTO);

		    return 0;
		}

	@Override
	public String checkUserEmail(String uEmail) {
		
		if("test@test.com".equals(uEmail)) {
			return"Y";
		}
		
		return "N";
	}
	

	
	

	
	

}
