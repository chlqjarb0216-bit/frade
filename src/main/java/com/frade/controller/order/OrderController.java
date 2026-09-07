package com.frade.controller.order;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.frade.common.order.PriceOptionCommon;
import com.frade.common.order.TradeOptionCommon;
import com.frade.dto.order.OrderInfoDTO;
import com.frade.dto.stock.StockPreviewDTO;
import com.frade.service.stock.StockService;
import com.frade.util.LoginManager;
import org.springframework.web.bind.annotation.RequestParam;
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
	@Autowired
	StockService stockService;

	@GetMapping("/stock/trade")
	public String trade(@RequestParam("stockCode") String stockCode, Model model, HttpSession session) {

		if (!LoginManager.isLogin(session)) {
			return "redirect:/user/login";
		}
		StockPreviewDTO stock = stockService.getStockPreviewByStockCode(stockCode);
		if (stock == null) {
			return "redirect:/stock";
		}
		model.addAttribute("stockPreview", stock);
		int userNum = LoginManager.getLoginUserNum(session);

		UserCashDTO cash = orderService.findUserCashByUserNum(userNum);
		model.addAttribute("userCash", cash == null ? 0 : cash.getCash());

		PortfolioDTO portfolio = portfolioService.findUserPortfolioByUserNumAndStockCode(userNum, stockCode);

		int userStockCnt = 0;
		if (portfolio != null) {
			userStockCnt = portfolio.getUserStockCnt();
		}
		model.addAttribute("stockCnt", userStockCnt);

		return "stock/order";
	}

	@PostMapping("/stock/trade")
	public String tradeAction(OrderInfoDTO orderInfo, HttpSession session) {
		if (!LoginManager.isLogin(session)) {
			return "redirect:/user/login";
		}

		orderInfo.setUserNum(LoginManager.getLoginUserNum(session));
		if (orderInfo.getStockCode() == null || orderInfo.getStockCode().isBlank()) {
			return "redirect:/stock";
		}
		StockPreviewDTO stock = stockService.getStockPreviewByStockCode(orderInfo.getStockCode());
		if (stock == null) {
			return "redirect:/stock";
		}
		orderInfo.setStockCode(stock.getStockCode());

		if (orderInfo.getPriceOption() == null
				|| (long) orderInfo.getOrderCount() * orderInfo.getOrderPrice() > Integer.MAX_VALUE) {
			return "redirect:/stock/" + stock.getStockCode();
		}

		boolean result = false;

		TradeOptionCommon tradeOption = orderInfo.getTradeOption();
		PriceOptionCommon priceOption = orderInfo.getPriceOption();

		// 시장가 저장로직 추후 구현 예정
//		if(priceOption == PriceOptionCommon.LIMITPRICE) {
//			orderService.saveMarketPrice(orderInfo);
//			return "redirect:/stock/trade";
//		}

		// 매수 매도 및 DAO 호출 전 검증
		if (tradeOption == (TradeOptionCommon.BUY)) {
			if (orderInfo.getOrderCount() <= 0) {
				System.out.println("주문 수량은 1 이상이어야 함.");
			} else if (orderInfo.getOrderPrice() <= 0) {
				System.out.println("주문 금액은 1 이상이어야 함.");
			} else {
				if (orderInfo.getPriceOption() == PriceOptionCommon.MARKETPRICE) {
					System.out.println("시장가");
					result = orderService.processMarketBuy(orderInfo);
				} else {
					System.out.println("지정가");
					result = orderService.saveLimitBuy(orderInfo);
				}
			}
		}
		if (tradeOption == (TradeOptionCommon.SELL)) {
			if (orderInfo.getOrderCount() <= 0) {
				System.out.println("매도수량은 0보다 커야함");
			} else if (orderInfo.getOrderPrice() <= 0) {
				System.out.println("매도가격은 0보다 커야함");
			} else {
				if (orderInfo.getPriceOption() == PriceOptionCommon.MARKETPRICE) {
					System.out.println("시장가");
					result = orderService.processMarketSell(orderInfo);
				} else {
					System.out.println("지정가");
					result = orderService.saveLimitSell(orderInfo);
				}

			}
		}
		
		
		if(orderInfo.getPriceOption() == PriceOptionCommon.MARKETPRICE) {
			if (result) {
				System.out.println("거래 성공");
			} else {
				System.out.println("거래 실패");
			}
		}else {
			if (result) {
				System.out.println("지정가 저장 완료");
			}else {
				System.out.println("지정가 저장 실패");
			}
		}

		return "redirect:/stock/" + orderInfo.getStockCode();
	}

}
