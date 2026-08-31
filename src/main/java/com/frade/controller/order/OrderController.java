package com.frade.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
		
		if(orderInfo.getTradeOption().equals("BUY")) {
			result = orderService.processBuy(orderInfo);
		}else {
			result = orderService.processSell(orderInfo);
		}
			
		if(result == 0) {
			System.out.println("거래 실패");
		}else {
			System.out.println("거래 성공");
		}
		
		return "redirect:/stock/trade";
	}
	

}
