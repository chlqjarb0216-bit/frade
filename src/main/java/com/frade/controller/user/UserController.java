package com.frade.controller.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.frade.dto.user.UserSignDTO;
import com.frade.service.user.UserService;

@Controller
public class UserController {
	
	@Autowired
	UserService userService;
	
	
	//로그인 화면
	@GetMapping("/user/login")
	public String login() {
		
		
		return "user/login";
	}
	
	//로그인 처리
	@PostMapping("/user/login")
	public String login(UserSignDTO userSignDTO, HttpSession session,Model model) {
		
		int loginUserNumber = userService.userLogin(userSignDTO);
		
		
		
		if(loginUserNumber == -1) {
			model.addAttribute("loginfail", "true");
			
			return "user/login";//로그인페이지 반환
			
		}else {
			
			session.setAttribute("loginUser", loginUserNumber);
			
			return  "redirect:/";//매인페이지로 보내야함
		}
		
		
	}
	
	  // 회원가입 페이지
    @GetMapping("/user/signup")
    public String signup() {

        return "user/signup";
    }

	

}
