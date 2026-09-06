package com.frade.controller.main;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.frade.common.stock.StockCommonFinalString;
import com.frade.dto.community.PostDTO;
import com.frade.dto.stock.StockPreviewDTO;
import com.frade.service.community.PostService;
import com.frade.service.stock.StockService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainController {

	@Autowired
	private StockService stockService;

	@Autowired
	private PostService postService;

	/**
	 * 메인 대시보드 화면 매핑 (루트 / 및 /main 경로 모두 처리)
	 */
	@GetMapping({ "/", "/main" })
	public String main(Model model) {
		// 1. 실시간 주식 상위 5개 테스트 데이터 조회

		List<StockPreviewDTO> topStocks = stockService.getSortedStockRankingListPage(0, 5);

		// 2. 커뮤니티 인기글 상위 5개 테스트 데이터 조회

		List<PostDTO> topPosts = postService.getPostListPagingSortedByView(5);

		model.addAttribute("topStocks", topStocks);
		model.addAttribute("topPosts", topPosts);

		//코스피 차트 데이터
		String kospiDataList = stockService.getEveryChartDataCached(StockCommonFinalString.KOSPI);
		log.info("코스피 데이터 {}", kospiDataList);
		model.addAttribute("chartDataJson", kospiDataList);

		return "main/main";
	}

}