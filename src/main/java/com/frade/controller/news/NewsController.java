package com.frade.controller.news;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/news")
public class NewsController {

	/**
	 * 뉴스 메인 목록 화면
	 * @param category 카테고리 (all, market, enterprise, economy, global, crypto)
	 * @param keyword 검색 키워드
	 * @param model
	 * @return news/news 뷰
	 */
	@GetMapping({"", "/"})
	public String newsList(
			@RequestParam(value = "category", required = false, defaultValue = "all") String category,
			@RequestParam(value = "keyword", required = false) String keyword,
			Model model) {

		model.addAttribute("currentCategory", category);
		model.addAttribute("keyword", keyword);

		return "news/news";
	}
}
