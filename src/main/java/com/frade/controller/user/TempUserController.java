package com.frade.controller.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.frade.dto.user.UserCashDTO;
import com.frade.service.order.OrderService;
import com.frade.service.portfolio.PortfolioService;

@Controller
@RequestMapping("/user/temp")
public class TempUserController {

	

	@Autowired
	PortfolioService portfolioService;

	@Autowired
	OrderService orderService;
	
	
	@GetMapping("/portfolio")
	public String getPortfolio(Model model) {
		
		int userNum = 1;  //유저번호 더미
		long stockPrice = 210_000;   //주식가격 더미데이터
		
		List<String> stockNameList = portfolioService.findUserPortfolioStockNameListByUserNum(userNum);

		List<Long> stockPriceList = new ArrayList<Long>();
		for(int i = 0; i < stockNameList.size(); i++){
			stockPriceList.add(stockPrice);
		}
		
		
		//포트폴리오에 유저 예치금 추가
		UserCashDTO userCash = orderService.findUserCashByUserNum(userNum);
	
		stockNameList.add("예치금");
		stockPriceList.add(userCash.getCash());
		

		model.addAttribute("stockNameList", stockNameList);
		model.addAttribute("stockPriceList", stockPriceList);

		return "user/mypage";
	}

}
