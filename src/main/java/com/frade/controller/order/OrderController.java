package com.frade.controller.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.frade.common.order.PriceOptionCommon;
import com.frade.common.order.TradeOptionCommon;
import com.frade.dto.order.OrderInfoDTO;
import com.frade.dto.user.AssetsInfoDTO;
import com.frade.dto.user.PortfolioDTO;
import com.frade.dto.user.UserCashDTO;
import com.frade.service.order.OrderService;
import com.frade.service.portfolio.PortfolioService;



@Controller
public class OrderController {

	@Autowired
	OrderService orderService;
	
	@Autowired
	PortfolioService portfolioService;


	@GetMapping("/stock/trade")
	public String trade(Model model) {

		
		int userNum = 1; //userNum 임시데이터 
		String stockCode = "005930";
		
		UserCashDTO cash = orderService.findUserCashByUserNum(userNum);
		
		PortfolioDTO portfolio = portfolioService.findUserPortfolioByUserNumAndStockCode(userNum, stockCode);
		
		
		model.addAttribute("userCash", cash.getCash());
		model.addAttribute("stockCnt", portfolio.getUserStockCnt());
		
		
		
		return "stock/order";
	}

	@PostMapping("/stock/trade")
	public String tradeAction(OrderInfoDTO orderInfo) {

		boolean result = false;
		
		
		
		
		
		
		
		TradeOptionCommon tradeOption = orderInfo.getTradeOption();
		PriceOptionCommon priceOption = orderInfo.getPriceOption();
		
		
		
		
		//시장가 저장로직 추후 구현 예정
//		if(priceOption == PriceOptionCommon.LIMITPRICE) {
//			orderService.saveMarketPrice(orderInfo);
//			return "redirect:/stock/trade";
//		}
		
		
		//매수 매도 및 DAO 호출 전 검증
		if (tradeOption == (TradeOptionCommon.BUY)) {
			if (orderInfo.getOrderCount() <= 0) {
				System.out.println("주문 수량은 1 이상이어야 함.");
			} else if (orderInfo.getOrderPrice() <= 0) {
				System.out.println("주문 금액은 1 이상이어야 함.");
			} else {
				result = orderService.processBuy(orderInfo);
			}
		}
		if (tradeOption == (TradeOptionCommon.SELL)) {
			if (orderInfo.getOrderCount() <= 0) {
				System.out.println("매도수량은 0보다 커야함");
			} else if (orderInfo.getOrderPrice() <= 0) {
				System.out.println("매도가격은 0보다 커야함");
			} else {
				result = orderService.processSell(orderInfo);
			}
		}

		if (result) {
			System.out.println("거래 성공");
		} else {
			System.out.println("거래 실패");
		}
		
		
		//===========assetInfo 확인용 코드, 추후 마이페이지로 이관 예정============
		AssetsInfoDTO assetsInfo = portfolioService.getAssetsInfo();
		System.out.println(assetsInfo);

		return "redirect:/stock/trade";
	}

}
