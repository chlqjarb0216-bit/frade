package com.frade.controller.user;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.frade.common.ResultCode;
import com.frade.dto.user.UserLoginDTO;
import com.frade.dto.user.UserProfileDTO;
import com.frade.dto.user.UserSessionDTO;
import com.frade.dto.user.UserSignupDTO;
import com.frade.service.user.UserService;
import com.frade.util.LoginManager;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;

	// 로그인 화면
	@GetMapping("/login")
	public String login() {

		return "user/login";
	}

	// 로그인 처리
	@PostMapping("/login")
	public String login(@Valid UserLoginDTO userLoginDTO, BindingResult bindingResult,
			HttpSession session, Model model) {
		
		
		if (bindingResult.hasErrors()) {

			String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();

			model.addAttribute("loginValidationFail", errorMessage);

			return "user/login";
		}

		UserSessionDTO loginUser = userService.userLogin(userLoginDTO);

		if (loginUser == null) {

			model.addAttribute("loginfail", "true");

			return "user/login";

		} else {

			LoginManager.setSessionLoginUser(session, loginUser);

			return "redirect:/user/mypage";
		}
	}
	
	// 로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session) {

	    LoginManager.logout(session);

	    return "redirect:/";
	}
	

	// -----------------------------------------------------------------------------------

	// 회원가입 페이지
	@GetMapping("/signup")
	public String signup() {

		return "user/signup";
	}

	@PostMapping("/signup")
	public String signup(@Valid UserSignupDTO userSignupDTO,BindingResult bindingResult ,String userPwCheck, Model model,
			RedirectAttributes redirectAttributes) {

		// 입력했던 정보 다시 보여주기(아이디, 닉네임, 이메일)
		model.addAttribute("usd", userSignupDTO);

		// DTO 입력값 검증
		if (bindingResult.hasErrors()) {

			String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();

			model.addAttribute("validationFail", errorMessage);

			return "user/signup";
		}

		// 비밀번호 확인 빈칸 확인
		if (userPwCheck == null || userPwCheck.trim().equals("")) {

			model.addAttribute("emptyFail", "비밀번호 확인을 입력해주세요.");

			return "user/signup";
		}

		// 비밀번호 확인
		if (!userSignupDTO.getUserPw().equals(userPwCheck)) {

			model.addAttribute("pwFail", true);

			return "user/signup";
		}

		// 중복일때 처리
		ResultCode signupResult = userService.userSignup(userSignupDTO);

		if (signupResult.getCode().startsWith("rej_")) {

			model.addAttribute("signupFail", signupResult.getMessage());

			return "user/signup";
		}


		// 로그인 페이지에서 가입완료시 보여줄 내용
		redirectAttributes.addFlashAttribute("signupSuccess", "회원가입이 완료되었습니다.");

		return "redirect:/user/login";
	}

	// ------------------------------------------------------------

	// 마이페이지
	@GetMapping("/mypage")
	public String myPage(HttpSession session, Model model) {

		// 로그인 여부 확인
		if (!LoginManager.isLogin(session)) {
			return "redirect:/user/login";
		}

		UserSessionDTO loginUser = LoginManager.getLoginUser(session);

		int loginUserNumber = loginUser.getUserNum();

		UserProfileDTO userProfileDTO = userService.getUserProfile(loginUserNumber);

		model.addAttribute("userProfile", userProfileDTO);

		return "user/mypage";
	}

	@PostMapping("/profile")
	public String updateUserProfile(UserProfileDTO userProfileDTO, HttpSession session,
			RedirectAttributes redirectAttributes) {

		// 로그인 확인
		if (!LoginManager.isLogin(session)) {
		    return "redirect:/user/login";
		}

		UserSessionDTO loginUser = LoginManager.getLoginUser(session);

		int loginUserNumber = loginUser.getUserNum();


		userProfileDTO.setUserNum(loginUserNumber);

		// 비밀번호 변경을 선택한 경우
		if (userProfileDTO.isPasswordChange()) {

			if (userProfileDTO.getCurrentPw() == null || userProfileDTO.getCurrentPw().isEmpty()
					|| userProfileDTO.getNewPw() == null || userProfileDTO.getNewPw().isEmpty()
					|| userProfileDTO.getNewPwCheck() == null || userProfileDTO.getNewPwCheck().isEmpty()) {

				redirectAttributes.addFlashAttribute("profileFail", ResultCode.INVALID_PASSWORD_INPUT.getMessage());

				return "redirect:/user/mypage";
			}

			// DTO에서 새 비밀번호 / 확인 비밀번호 비교
			if (!userProfileDTO.isNewPwMatch()) {

				redirectAttributes.addFlashAttribute("profileFail", ResultCode.PW_NOT_MATCH.getMessage());

				return "redirect:/user/mypage";
			}

			// DTO에서 현재 비밀번호 / 새 비밀번호 비교
			if (!userProfileDTO.isDifferentPw()) {

				redirectAttributes.addFlashAttribute("profileFail", ResultCode.SAME_PASSWORD.getMessage());

				return "redirect:/user/mypage";
			}
		}

		ResultCode result = userService.updateUserProfile(userProfileDTO);

		if (result == ResultCode.SUCCESS) {


		    // 세션의 로그인 유저 정보도 최신 정보로 변경
		    UserSessionDTO updatedLoginUser =
		            new UserSessionDTO(
		                    loginUserNumber,
		                    userProfileDTO.getUserNick(),
		                    userProfileDTO.getUserPhoto()
		            );

			LoginManager.setSessionLoginUser(session, updatedLoginUser);

		    redirectAttributes.addFlashAttribute(
		            "profileSuccess",
		            "프로필이 수정되었습니다.");

		    return "redirect:/user/mypage";
		}

		redirectAttributes.addFlashAttribute(
		        "profileFail",
		        result.getMessage());

		return "redirect:/user/mypage";
	}

	@PostMapping("/withdraw")
	public String deleteUser(HttpSession session) {

		 // 로그인 확인
			if (!LoginManager.isLogin(session)) {
				return "redirect:/user/login";
			}

			UserSessionDTO loginUser = LoginManager.getLoginUser(session);

			int loginUserNumber = loginUser.getUserNum();

			ResultCode result = userService.deleteUser(loginUserNumber);

	    if(result == ResultCode.SUCCESS) {

	    	LoginManager.logout(session);

	        return "redirect:/user/login";
	    }

	    return "redirect:/user/mypage";

	}
}
