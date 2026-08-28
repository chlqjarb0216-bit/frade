package com.frade.controller.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.frade.dto.user.UserSignDTO;
import com.frade.service.user.UserService;

@Controller
public class UserController {

	@Autowired
	UserService userService;

	// 로그인 화면
	@GetMapping("/user/login")
	public String login() {

		return "user/login";
	}

	// 로그인 처리
	@PostMapping("/user/login")
	public String login(UserSignDTO userSignDTO, HttpSession session, Model model) {

		int loginUserNumber = userService.userLogin(userSignDTO);

		if (loginUserNumber == -1) {
			model.addAttribute("loginfail", "true");

			return "user/login";// 로그인페이지 반환

		} else {

			session.setAttribute("loginUser", loginUserNumber);

			return "redirect:/";// 매인페이지로 보내야함
		}

	}

	// 회원가입 페이지
	@GetMapping("/user/signup")
	public String signup() {

		return "user/signup";
	}

	@PostMapping("/user/signup")
	public String signup(UserSignDTO userSignDTO, String uPwCheck, Model model) {
		
		//입력했던 정보 다시 보여주기(아이디, 닉네임, 이메일)
		model.addAttribute("uId", userSignDTO.getUId());
		model.addAttribute("uNick", userSignDTO.getUNick());
		model.addAttribute("uEmail", userSignDTO.getUEmail());
		
		// 아이디 빈칸 확인
		if(userSignDTO.getUId() == null 
		        || userSignDTO.getUId().trim().equals("")) {

		    model.addAttribute("emptyFail", "아이디를 입력해주세요.");
		    return "user/signup";
		}

		// 닉네임 빈칸 확인
		if(userSignDTO.getUNick() == null 
		        || userSignDTO.getUNick().trim().equals("")) {

		    model.addAttribute("emptyFail", "닉네임을 입력해주세요.");
		    return "user/signup";
		}

		// 비밀번호 빈칸 확인
		if(userSignDTO.getUPw() == null 
		        || userSignDTO.getUPw().trim().equals("")) {

		    model.addAttribute("emptyFail", "비밀번호를 입력해주세요.");
		    return "user/signup";
		}

		// 비밀번호 확인 빈칸 확인
		if(uPwCheck == null 
		        || uPwCheck.trim().equals("")) {

		    model.addAttribute("emptyFail", "비밀번호 확인을 입력해주세요.");
		    return "user/signup";
		}

		// 이메일 빈칸 확인
		if(userSignDTO.getUEmail() == null 
		        || userSignDTO.getUEmail().trim().equals("")) {

		    model.addAttribute("emptyFail", "이메일을 입력해주세요.");
		    return "user/signup";
		}

		// 공백 확인
		if (userSignDTO.getUId().matches(".*\\s.*") || userSignDTO.getUNick().matches(".*\\s.*")
				|| userSignDTO.getUPw().matches(".*\\s.*")) {

			model.addAttribute("spaceFail", true);

			return "user/signup";
		}

		// 비밀번호 확인
		if (!userSignDTO.getUPw().equals(uPwCheck)) {

			model.addAttribute("pwFail", true);

			return "user/signup";
		}
		
		 String signupResult = userService.userSignup(userSignDTO);


		if(signupResult.equals("DUP_ID")) {

		    model.addAttribute("signupFail", "이미 사용 중인 아이디입니다.");
		    return "user/signup";

		}

		if(signupResult.equals("DUP_NICK")) {

		    model.addAttribute("signupFail", "이미 사용 중인 닉네임입니다.");
		    return "user/signup";

		}

		if(signupResult.equals("DUP_EMAIL")) {

		    model.addAttribute("signupFail", "이미 사용 중인 이메일입니다.");
		    return "user/signup";

		}

		return "redirect:/user/login";
	}

	@PostMapping("/user/checkId") // 아이디 중복 확인
	@ResponseBody
	public String checkUserId(@RequestBody String uId) {

		String result = userService.checkUserId(uId);

		return result;
	}

	@PostMapping("/user/checkNick") // 닉네임 중복 확인
	@ResponseBody
	public String checkUserNick(@RequestBody String uNick) {

		String result = userService.checkUserNick(uNick);

		return result;
	}
	
	@PostMapping("/user/checkEmail")
	@ResponseBody
	public String checkUserEmail(@RequestBody String uEmail) {

	    String result = userService.checkUserEmail(uEmail);

	    return result;
	}

}
