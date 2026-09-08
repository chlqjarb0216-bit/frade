package com.frade.dto.event;

import com.frade.dto.order.OrderInfoDTO;

public record PriceExecutedEvent(OrderInfoDTO orderInfoDTO) {
}
