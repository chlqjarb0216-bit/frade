package com.frade.memcache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.frade.common.stock.StockSector;
import com.frade.dto.stock.StockInfoDTO;
import com.frade.dto.stock.StockPreviewDTO;

import lombok.extern.slf4j.Slf4j;

//주식 preview정보리스트를 정렬해서 들고 있을 캐시
@Slf4j
@Component
public class StockRankingCache {
	//정렬된 리스트
	private List<StockPreviewDTO> sortedCache = new ArrayList<>();
	//종목코드로 바로 접근하기 위한 해시맵
	private Map<String, StockPreviewDTO> codeCacheMap = new HashMap<>();

	//초기화
	public void init(List<StockInfoDTO> stockInfoList) {
		//갈아끼울 새 맵 작성
		Map<String, StockPreviewDTO> tempMap = new HashMap<>();
		//정렬
		List<StockPreviewDTO> previewList = stockInfoList.stream().map(info -> {

			if (info.getPrevDayClosePrice() <= 0)
				return null;

			// 화면 출력용 객체 조립
			StockPreviewDTO previewDTO = new StockPreviewDTO(info.getStockCode(), info.getStockName(),
					StockSector.getSectorName(info.getSectorNum()), info.getPrevDayClosePrice(),
					info.getPrevDayClosePrice());

			tempMap.put(info.getStockCode(), previewDTO);
			return previewDTO;
		}).filter(Objects::nonNull)
				.sorted((o1, o2) -> Double.compare(o2.getDailyPriceChangeRate(), o1.getDailyPriceChangeRate())) // dailyChangeRate(당일 등락률) 기준 내림차순 정렬
				.toList();

		updateSortedCache(previewList, tempMap);
	}

	public void updateSortedCache(List<StockPreviewDTO> sortedList, Map<String, StockPreviewDTO> newCodeMap) {
		if (sortedList == null || newCodeMap == null)
			return;
		this.sortedCache = sortedList;
		this.codeCacheMap = newCodeMap;
		log.info("랭킹 캐시 갱신 완료");
	}

	public List<StockPreviewDTO> getSortedCachePage(int pageIdx, int pageSize) {
		// 현재 살아있는 전역 리스트 주소를 스냅샷으로 안전하게 확보
		List<StockPreviewDTO> currentSource = this.sortedCache;

		int offset = pageIdx * pageSize;

		int totalSize = currentSource.size();
		if (totalSize == 0 || offset >= totalSize) {
			return Collections.emptyList(); // 데이터가 없거나 범위를 완전히 벗어나면 안전하게 빈 리스트 리턴
		}

		// 끝 인덱스 계산 및 오버플로우 가드레일 (데이터가 100개 미만일 때 터지는 것 방지)
		int toIndex = Math.min(offset + pageSize, totalSize);

		return currentSource.subList(offset, toIndex);
	}

	public StockPreviewDTO getPreviewByStockCode(String stockCode) {
		return this.codeCacheMap.get(stockCode);
	}
}
