package com.frade.controller.order;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.frade.common.order.PriceOptionCommon;
import com.frade.dto.order.OrderInfoDTO;
import com.frade.dto.stock.StockPreviewDTO;
import com.frade.service.order.OrderService;
import com.frade.service.portfolio.PortfolioService;
import com.frade.service.stock.StockService;
import com.frade.util.LoginManager;
import com.frade.util.MarketUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class OrderController {

	@Autowired
	OrderService orderService;

	@Autowired
	PortfolioService portfolioService;
	@Autowired
	StockService stockService;

	@PostMapping("/stock/trade")
	public String tradeAction(OrderInfoDTO orderInfo, HttpSession session, RedirectAttributes rttr) {
		if (!LoginManager.isLogin(session)) {
			return "redirect:/user/login";
		}

		if (!MarketUtil.isMarketOpenTime()) {
			return "redirect:/stock";
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
			rttr.addFlashAttribute("tradeResultMsg", "총 주문 금액이 너무 큽니다");
			return "redirect:/stock/" + stock.getStockCode();
		}

		try {
			boolean result = orderService.orderAnalyzer(orderInfo);

			if (orderInfo.getPriceOption() == PriceOptionCommon.MARKETPRICE) {
				if (result) {
					rttr.addFlashAttribute("tradeResultMsg", "거래 성공");
				} else {
					rttr.addFlashAttribute("tradeResultMsg", "거래 실패");
				}
			} else {
				if (result) {
					rttr.addFlashAttribute("tradeResultMsg", "지정가 거래 성공");
				} else {
					rttr.addFlashAttribute("tradeResultMsg", "지정가 거래 실패");
				}
			}
		} catch (Exception e) {
			log.warn("거래 중 에러 발생: {}", e.getMessage());
			rttr.addFlashAttribute("tradeResultMsg", "거래 실패" + e.getMessage());
		}

		return "redirect:/stock/" + orderInfo.getStockCode();
	}

}
