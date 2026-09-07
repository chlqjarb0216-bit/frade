package com.frade.controller.stock;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frade.dto.stock.StockPreviewDTO;
import com.frade.dto.user.PortfolioDTO;
import com.frade.dto.user.UserCashDTO;
import com.frade.service.order.OrderService;
import com.frade.service.portfolio.PortfolioService;
import com.frade.service.stock.StockService;
import com.frade.util.LoginManager;
import com.frade.util.MarketUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/stock")
public class StockController {

	@Autowired
	StockService stockService;
	@Autowired
	OrderService orderService;
	@Autowired
	PortfolioService portfolioService;

	@Autowired
	ObjectMapper objectMapper;

	@GetMapping("")
	public String stockMain() {
		return "stock/stock-main";
	}

	@GetMapping("/{stockCode}")
	public String stockTrade(@PathVariable("stockCode") String stockCode, Model model, HttpSession session) {
		StockPreviewDTO prevDTO = stockService.getStockPreviewByStockCode(stockCode);
		if (prevDTO == null) {
			return "redirect:/stock";
		}
		model.addAttribute("stockPreview", prevDTO);
		if (LoginManager.isLogin(session)) {
			int userNum = LoginManager.getLoginUserNum(session);
			UserCashDTO cash = orderService.findUserCashByUserNum(userNum);
			PortfolioDTO portfolio = portfolioService.findUserPortfolioByUserNumAndStockCode(userNum, stockCode);
			model.addAttribute("userCash", cash == null ? 0 : cash.getCash());
			model.addAttribute("stockCnt", portfolio == null ? 0 : portfolio.getUserStockCnt());
			model.addAttribute("isMarketTime",MarketUtil.isMarketOpenTime());
		}
		// 차트 데이터
		List<Object[]> kospiDataList = stockService.getEveryChartDataCached(stockCode);
		String jsonString = "[]";
		try {
			jsonString = objectMapper.writeValueAsString(kospiDataList);
		} catch (JsonProcessingException e) {
			log.error("jsonString 매핑중 에러 발생");
		}
		model.addAttribute("chartDataJson", jsonString);
		return "stock/stock-trade";
	}

}
