package com.frade.controller.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.frade.dto.user.PortfolioDTO;
import com.frade.dto.user.PortfolioInfoDTO;
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
		long stockPrice = 210_000;   //현재가격 더미

		List<PortfolioDTO> portfolioList = portfolioService.findUserPortfolioListByUserNum(userNum);
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
			double profitPercent = (double) pnl / portfolio.getUserBuyCost() * 100; //수익률
			double weightPercent = (double) valuationAmount / totalValuationAmount * 100;

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
		UserCashDTO userCash = orderService.findUserCashByUserNum(userNum);
		stockNameList.add("예치금");
		stockPriceList.add(userCash == null ? 0L : userCash.getCash());
		model.addAttribute("stockNameList", stockNameList);
		model.addAttribute("stockPriceList", stockPriceList);
		model.addAttribute("portfolioInfoList", portfolioInfoList);
		model.addAttribute("holdingStockCount", portfolioInfoList.size());

		return "user/mypage";
	}

}
