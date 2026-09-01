package com.frade.controller.stock;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stock")
public class StockController {

	@GetMapping("")
	public String stockMain() {
		return "stock/stock-main";
	}

}
