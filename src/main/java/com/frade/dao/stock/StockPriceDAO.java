package com.frade.dao.stock;

import java.util.List;

import com.frade.dto.stock.StockPriceDTO;

public interface StockPriceDAO {
	public int insertMinuteStockPrice(List<StockPriceDTO> stockPriceList);
}
