package com.frade.memcache;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.frade.dto.stock.StockPriceDTO;

@Component
public class StockPriceMemoryCache {
	private Map<String, Map<String, StockPriceDTO>> priceCache;
}
