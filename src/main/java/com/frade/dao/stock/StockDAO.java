package com.frade.dao.stock;

import java.util.List;

import com.frade.dto.stock.StockInfoDTO;

public interface StockDAO {
	//DB에서 전체 데이터 조회
	public List<StockInfoDTO> selectAllStock();
}
