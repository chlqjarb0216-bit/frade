package com.frade.dao.portfolio;

import java.util.List;

import com.frade.dto.order.HistoryDTO;

public interface HistoryDAO {
	
	List<HistoryDTO> findTradeHistoryByUserNum(int userNum); //거래내역 조회
	public int insertTradeHistory(HistoryDTO history); //거래내역 추가
	
}
