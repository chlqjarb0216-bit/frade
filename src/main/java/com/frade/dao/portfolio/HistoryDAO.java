package com.frade.dao.portfolio;

import java.util.List;

import com.frade.dto.order.HistoryDTO;
import com.frade.dto.order.HistoryForMypageDTO;

public interface HistoryDAO {
	
	List<HistoryDTO> findTradeHistoryByUserNum(int userNum); //전체 거래내역 조회
	
	
	List<HistoryDTO> findTradeHistoryTodayByUserNum(int userNum); //오늘 조회
	
	List<HistoryDTO> findTradeHistoryRecentOneWeekByUserNum(int userNum); //일주일 조회
	
	List<HistoryDTO> findTradeHistoryRecentOneMonthByUserNum(int userNum); //한달 조회
	
	public int insertTradeHistory(HistoryDTO history); //거래내역 추가
	
	List<HistoryForMypageDTO> findTradeHistoryForMypageByUserNum(int userNum);
	
	
}
