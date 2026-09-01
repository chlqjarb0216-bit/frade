package com.frade.controller.stock;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.frade.dto.rest.RestApiResponse;
import com.frade.dto.stock.StockPreviewDTO;
import com.frade.service.stock.StockService;

@Controller
@RequestMapping("/stock")
public class StockController {

	@Autowired
	StockService stockService;

	@GetMapping("")
	public String stockMain(Model model) {
		return "stock/stock-main";
	}

	@GetMapping("/api/stock-list")
	@ResponseBody
	public RestApiResponse<List<StockPreviewDTO>> getStockRankingListPage(@RequestParam int page) {
		List<StockPreviewDTO> stockList = stockService.getSortedStockRankingListPage(page);
		return RestApiResponse.success(stockList);
	}

}
