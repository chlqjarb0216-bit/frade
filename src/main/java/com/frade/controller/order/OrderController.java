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
		
		boolean result = orderService.orderAnalyzer(orderInfo);
		
		
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
