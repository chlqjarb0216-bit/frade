package com.frade.controller.stock;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.frade.common.ResultCode;
import com.frade.dto.rest.RestApiResponse;
import com.frade.dto.stock.StockInfoDTO;
import com.frade.dto.stock.StockPreviewDTO;
import com.frade.service.stock.StockService;

@RestController
@RequestMapping("/stock/api")
public class StockRestController {

	@Autowired
	StockService stockService;

	@GetMapping("/stock-list")
	@ResponseBody
	public RestApiResponse<List<StockPreviewDTO>> getStockRankingListPage(@RequestParam int page) {
		List<StockPreviewDTO> stockList = stockService.getSortedStockRankingListPage(page - 1, 10);
		return RestApiResponse.success(stockList);
	}

	/**
	 * 💡 2. [관심 종목 초고속 자동완성 검색 API]
	 * 사용자가 한 글자 타이핑할 때마다 디바운싱을 타고 이 API를 호출합니다.
	 * 아침 8시 40분에 ApiService ➔ StockService 를 거쳐 이미 RAM에 완벽하게 탑재된 캐시 풀에서만 
	 * 스트림 필터링을 수행하므로, 수천 명이 동시에 난타해도 외부 DB 조회(I/O)는 무조건 '0건'으로 철벽 가드됩니다.
	 */
	@GetMapping("/search-preview")
	public RestApiResponse<List<StockInfoDTO>> searchStockAutocomplete(@RequestParam String keyword) {
		//키워드가 없을 시 반환
		if (keyword == null || keyword.trim().length() < 1)
			return RestApiResponse.success();
		// 💡 RAM 메모리 안에서 단 0초만에 10개 커팅하여 즉시 JSON Array로 리턴합니다.
		List<StockInfoDTO> result = stockService.searchStockByName(keyword);
		if (result.size() == 0)
			return RestApiResponse.response(ResultCode.SUC_EMPTY, null);
		return RestApiResponse.success(result);
	}

}
