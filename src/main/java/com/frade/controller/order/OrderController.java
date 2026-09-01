package com.frade.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.frade.common.order.TradeOptionCommon;
import com.frade.dto.order.HistoryDTO;
import com.frade.dto.order.OrderInfoDTO;
import com.frade.service.order.OrderService;

@Controller
public class OrderController {

	@Autowired
	OrderService orderService;

	@GetMapping("/stock/trade")
	public String trade() {

		return "stock/order";
	}

	@PostMapping("/stock/trade")
	public String tradeAction(OrderInfoDTO orderInfo) {

		int result = 0;

		String tradeOption = orderInfo.getTradeOption();

		
		//매수 매도 및 DAO 호출 전 검증
		if (TradeOptionCommon.valueOf(tradeOption) == (TradeOptionCommon.BUY)) {
			if (orderInfo.getOrderCount() <= 0) {
				System.out.println("주문 수량은 1 이상이어야 함.");
			} else if (orderInfo.getOrderPrice() <= 0) {
				System.out.println("주문 금액은 1 이상이어야 함.");
			} else {
				result = orderService.processBuy(orderInfo);
			}
		}
		if (TradeOptionCommon.valueOf(tradeOption) == (TradeOptionCommon.SELL)) {
			if (orderInfo.getOrderCount() <= 0) {
				System.out.println("매도수량은 0보다 커야함");
			} else if (orderInfo.getOrderPrice() <= 0) {
				System.out.println("매도가격은 0보다 커야함");
			} else {
				result = orderService.processSell(orderInfo);
			}
		}

		if (result == 0) {
			System.out.println("거래 실패");
		} else {
			System.out.println("거래 성공");
		}

		return "redirect:/stock/trade";
	}

}
