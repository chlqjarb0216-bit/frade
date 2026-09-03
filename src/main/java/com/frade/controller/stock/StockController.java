package com.frade.controller.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.frade.dto.stock.StockPreviewDTO;
import com.frade.service.stock.StockService;

@Controller
@RequestMapping("/stock")
public class StockController {

	@Autowired
	StockService stockService;

	@GetMapping("")
	public String stockMain() {
		return "stock/stock-main";
	}

	@GetMapping("/{stockCode}")
	public String stockTrade(@PathVariable("stockCode") String stockCode, Model model) {
		StockPreviewDTO prevDTO = stockService.getStockPreviewByStockCode(stockCode);
		model.addAttribute("stockPreview", prevDTO);
		return "stock/stock-trade";
	}

}
