package com.frade.controller.stock;

import javax.servlet.http.HttpSession;
import com.frade.dto.user.UserCashDTO;
import com.frade.dto.user.PortfolioDTO;
import com.frade.service.order.OrderService;
import com.frade.service.portfolio.PortfolioService;
import com.frade.util.LoginManager;
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
	@Autowired
	OrderService orderService;
	@Autowired
	PortfolioService portfolioService;

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
        }
		return "stock/stock-trade";
	}

}
