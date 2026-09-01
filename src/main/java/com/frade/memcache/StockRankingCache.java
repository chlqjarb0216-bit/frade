package com.frade.memcache;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;

import com.frade.dto.stock.StockPreviewDTO;

@Component
public class StockRankingCache {

	private List<StockPreviewDTO> sortedCache = new CopyOnWriteArrayList<>();

	public void updateSortedCache(List<StockPreviewDTO> sortedList) {
		this.sortedCache = new CopyOnWriteArrayList<>(sortedList);
	}

	public List<StockPreviewDTO> getSortedCache() {
		return this.sortedCache;
	}
}
