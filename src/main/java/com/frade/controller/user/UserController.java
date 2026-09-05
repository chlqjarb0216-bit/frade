package com.frade.controller.user;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frade.common.ResultCode;
import com.frade.dto.order.HistoryDTO;
import com.frade.dto.order.HistoryForMypageDTO;
import com.frade.dto.rest.RestApiResponse;
import com.frade.dto.user.AssetsInfoDTO;
import com.frade.dto.user.PortfolioDTO;
import com.frade.dto.user.PortfolioInfoDTO;
import com.frade.dto.user.UserCashDTO;
import com.frade.dto.user.UserProfileDTO;
import com.frade.dto.user.UserSignDTO;
import com.frade.service.order.OrderService;
import com.frade.service.portfolio.PortfolioService;
import com.frade.service.user.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	PortfolioService portfolioService;
	
	@Autowired
	OrderService orderService;

	// 로그인 화면
	@GetMapping("/login")
	public String login() {

		return "user/login";
	}

	// 로그인 처리
	@PostMapping("/login")
	public String login(UserSignDTO userSignDTO, HttpSession session, Model model) {

		int loginUserNumber = userService.userLogin(userSignDTO);

		if (loginUserNumber == -1) {
			model.addAttribute("loginfail", "true");

			return "user/login";// 로그인페이지 반환

		} else {

			session.setAttribute("loginUser", loginUserNumber);

			return "redirect:/user/mypage";// 매인페이지로 보내야함
		}

	}
	
	
	// -----------------------------------------------------------------------------------

	// 회원가입 페이지
	@GetMapping("/signup")
	public String signup() {

		return "user/signup";
	}

	@PostMapping("/signup")
	public String signup(UserSignDTO userSignDTO, String userPwCheck, Model model, RedirectAttributes redirectAttributes) {
		
		//입력했던 정보 다시 보여주기(아이디, 닉네임, 이메일)
		model.addAttribute("usd", userSignDTO);
		
		// 아이디 빈칸 확인
		if(userSignDTO.getUserId() == null 
		        || userSignDTO.getUserId().trim().equals("")) {

		    model.addAttribute("emptyFail", "아이디를 입력해주세요.");
		    return "user/signup";
		}

		// 닉네임 빈칸 확인
		if(userSignDTO.getUserNick() == null 
		        || userSignDTO.getUserNick().trim().equals("")) {

		    model.addAttribute("emptyFail", "닉네임을 입력해주세요.");
		    return "user/signup";
		}

		// 비밀번호 빈칸 확인
		if(userSignDTO.getUserPw() == null 
		        || userSignDTO.getUserPw().trim().equals("")) {

		    model.addAttribute("emptyFail", "비밀번호를 입력해주세요.");
		    return "user/signup";
		}

		// 비밀번호 확인 빈칸 확인
		if(userPwCheck == null 
		        || userPwCheck.trim().equals("")) {

		    model.addAttribute("emptyFail", "비밀번호 확인을 입력해주세요.");
		    return "user/signup";
		}

		// 이메일 빈칸 확인
		if(userSignDTO.getUserEmail() == null 
		        || userSignDTO.getUserEmail().trim().equals("")) {

		    model.addAttribute("emptyFail", "이메일을 입력해주세요.");
		    return "user/signup";
		}

		// 공백 확인
		if (userSignDTO.getUserId().matches(".*\\s.*") || userSignDTO.getUserNick().matches(".*\\s.*")
				|| userSignDTO.getUserPw().matches(".*\\s.*")) {

			model.addAttribute("spaceFail", true);

			return "user/signup";
		}

		// 비밀번호 확인
		if (!userSignDTO.getUserPw().equals(userPwCheck)) {

			model.addAttribute("pwFail", true);

			return "user/signup";
		}
		// 중복일때 처리 
		ResultCode signupResult = userService.userSignup(userSignDTO);

		if(signupResult.getCode().startsWith("rej_")) {
		    model.addAttribute("signupFail", signupResult.getMessage());
		    return "user/signup";
		}
		//로그인 페이지에서 가입완료시 보여줄 내용
		redirectAttributes.addFlashAttribute("signupSuccess", "회원가입이 완료되었습니다.");
		
		return "redirect:/user/login";
	}

	@PostMapping("/api/checkId") // 아이디 중복 확인
	@ResponseBody
	public RestApiResponse<Void> checkUserId(@RequestBody String userId) {

		boolean result = userService.checkUserId(userId);
		
		if(result) {
			return RestApiResponse.error(ResultCode.DUP_ID);
		}
		

		return RestApiResponse.success();
	}

	@PostMapping("/api/checkNick") // 닉네임 중복 확인
	@ResponseBody
	public RestApiResponse<Void> checkUserNick(@RequestBody String userNick) {

		boolean result = userService.checkUserNick(userNick);
		
		if(result) {
			return RestApiResponse.error(ResultCode.DUP_NICK);
		}

		return RestApiResponse.success();
	}
	
	@PostMapping("/api/checkEmail") //이메일 중복 확인
	@ResponseBody
	public RestApiResponse<Void> checkUserEmail(@RequestBody String userEmail) {

	    boolean result = userService.checkUserEmail(userEmail);
	    
	    if(result) {
	    	return RestApiResponse.error(ResultCode.DUP_EMAIL);
	    }

	    return RestApiResponse.success();
	}
	
	
	//------------------------------------------------------------
	
	//마이페이지
	@GetMapping("/mypage")
	public String myPage(HttpSession session, Model model) throws JsonProcessingException {
		
		 // 로그인 여부 확인
	    if(session.getAttribute("loginUser") == null) {
	        return "redirect:/user/login";
	    }
		int loginUserNumber = (int)session.getAttribute("loginUser");
		
		UserProfileDTO userProfileDTO = userService.getUserProfile(loginUserNumber);
		
		model.addAttribute("userProfile",userProfileDTO);
		

		long stockPrice = 210_000;   //현재가격 더미

		List<PortfolioDTO> portfolioList = portfolioService.findUserPortfolioListByUserNum(loginUserNumber);
		List<String> stockNameList = new ArrayList<String>();
		List<Long> stockPriceList = new ArrayList<Long>();
		List<PortfolioInfoDTO> portfolioInfoList = new ArrayList<PortfolioInfoDTO>();

		long totalValuationAmount = 0;
		for (PortfolioDTO portfolio : portfolioList) {
			if (portfolio.getUserStockCnt() > 0) {
				totalValuationAmount += stockPrice * portfolio.getUserStockCnt();
			}
		}

		for (PortfolioDTO portfolio : portfolioList) {
			int stockCnt = portfolio.getUserStockCnt();
			if (stockCnt <= 0) {
				continue;
			}

			long currentPrice = stockPrice; //현재가
			long avgStockBuyCost = Math.round((double) portfolio.getUserBuyCost() / stockCnt); //평균단가
			long valuationAmount = currentPrice * stockCnt; //평가 가치
			long pnl = valuationAmount - portfolio.getUserBuyCost(); //평가 손익
			double profitPercent = Math.round((double) pnl / portfolio.getUserBuyCost() * 100) * 100 / 100.0; //수익률
			double weightPercent = Math.round((double) valuationAmount / totalValuationAmount * 100) * 100 / 100.0;

			PortfolioInfoDTO portfolioInfo = new PortfolioInfoDTO();
			portfolioInfo.setStockCode(portfolio.getStockCode());
			portfolioInfo.setStockName(portfolio.getStockName());
			portfolioInfo.setStockCnt(stockCnt);
			portfolioInfo.setAvgStockBuyCost(avgStockBuyCost);
			portfolioInfo.setStockNowPrice(currentPrice);
			portfolioInfo.setValuationAmount(valuationAmount);
			portfolioInfo.setPnl(pnl);
			portfolioInfo.setProfitPercent(profitPercent);
			portfolioInfo.setWeightPercent(weightPercent);
			portfolioInfoList.add(portfolioInfo);

			stockNameList.add(portfolio.getStockName());
			stockPriceList.add(currentPrice);
		}
		
		

		//포트폴리오에 유저 예치금 추가
		UserCashDTO userCash = orderService.findUserCashByUserNum(loginUserNumber);
		stockNameList.add("예치금");
		stockPriceList.add(userCash == null ? 0L : userCash.getCash());
		
		
		//포트폴리오 정보 전달
		model.addAttribute("stockNameList", stockNameList);  //종목 이르
		model.addAttribute("stockPriceList", stockPriceList); //종목 현재가
		model.addAttribute("portfolioInfoList", portfolioInfoList); //포트폴리오 정보
		model.addAttribute("holdingStockCount", portfolioInfoList.size()); //보유 종목 수
		
		//거래기록 전달
		List<HistoryForMypageDTO> historyList= portfolioService.findTradeHistoryForMypageByUserNum(loginUserNumber);
		model.addAttribute("historyList", historyList);
		
		
		AssetsInfoDTO assetsInfo = portfolioService.getAssetsInfo(loginUserNumber);
		model.addAttribute("assetsInfo", assetsInfo);
		
		
		ObjectMapper objectMapper = new ObjectMapper();

	    model.addAttribute(
	        "stockNameList",
	        objectMapper.writeValueAsString(stockNameList)
	    );

	    model.addAttribute(
	        "stockPriceList",
	        objectMapper.writeValueAsString(stockPriceList)
	    );

		
		return "user/mypage";
	}
	
	
	@PostMapping("/api/profile")
	@ResponseBody
	public RestApiResponse<Void> updateUserProfile(UserProfileDTO userProfileDTO,
					@RequestParam(value="profilePhoto", required=false) MultipartFile profilePhoto,
					@RequestParam(value="defaultPhoto", defaultValue="false") boolean defaultPhoto,
					@RequestParam(value="passwordChange", defaultValue="false") boolean passwordChange,
					HttpSession session){
		
		// 로그인한 회원 번호
		int loginUserNumber = (int)session.getAttribute("loginUser");
		
		// 새션의 회원 번호를 DTO에 저장
		userProfileDTO.setUserNum(loginUserNumber);
		
		// 비밀번호 변경 요청인 경우
		if(passwordChange) {

		    // 비밀번호 입력값 확인
		    if(userProfileDTO.getCurrentPw() == null
		            || userProfileDTO.getCurrentPw().isEmpty()
		            || userProfileDTO.getNewPw() == null
		            || userProfileDTO.getNewPw().isEmpty()
		            || userProfileDTO.getNewPwCheck() == null
		            || userProfileDTO.getNewPwCheck().isEmpty()) {

		        return RestApiResponse.error(
		                ResultCode.INVALID_PASSWORD_INPUT);
		    }

		    // 새 비밀번호 확인
		    if(!userProfileDTO.getNewPw()
		            .equals(userProfileDTO.getNewPwCheck())) {

		        return RestApiResponse.error(
		                ResultCode.PW_NOT_MATCH);
		    }
		}
		// 확인용
		System.out.println("비밀번호 변경 여부 : " + passwordChange);
		
		//확인용
		System.out.println("기본 이미지 변경 여부: " + defaultPhoto);
		
		//사진 확인용
		if(profilePhoto!= null && !profilePhoto.isEmpty()) {
			
			System.out.println("프로필 사진 이름: " + profilePhoto.getOriginalFilename());
		}
		
		//service로 프로필 수정 정보 전달
		ResultCode result = userService.updateUserProfile(userProfileDTO, profilePhoto, defaultPhoto, passwordChange);
		
		return RestApiResponse.response(result, null);
		
	}
	
	@PostMapping("/withdraw")
	public String deleteUser(HttpSession session){

	    int loginUserNumber =
	            (int)session.getAttribute("loginUser");

	    ResultCode result =
	            userService.deleteUser(loginUserNumber);

	    if(result == ResultCode.SUCCESS){

	        session.invalidate();

	        return "redirect:/user/login";
	    }

	    return "redirect:/user/mypage";
	}
	

	

}
