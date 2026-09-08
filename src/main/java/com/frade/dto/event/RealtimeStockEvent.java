package com.frade.dto.event;

import com.frade.dto.stock.StockPriceDTO;

public record RealtimeStockEvent(StockPriceDTO stockPriceDto) {
}
