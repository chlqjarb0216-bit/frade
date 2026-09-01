package com.frade.dao.order;

import java.util.List;

import com.frade.dto.order.HistoryDTO;
import com.frade.dto.order.OrderInfoDTO;
import com.frade.dto.user.PortfolioDTO;
import com.frade.dto.user.UserCashDTO;

public interface OrderDAO {
	

//	==========t_portfolio===========
	public List<PortfolioDTO> findUserPortfolioListByUserNum(int userNum); //portfolio 테이블 정보를 list로 받기
	public PortfolioDTO findUserPortfolioByUserNumAndStockCode(int userNum, String stockCode); //종목별 포트폴리오 찾기
	public int insertUserPortfolio(PortfolioDTO portfolio); //포트폴리오 신규저장
	public int updateUserPortfolio(PortfolioDTO portfolio); //포트폴리오 기존 보유종목 업데이트
	public int deleteUserPortfolioByUserNumAndStockCode(int userNum, String stockCode); //전량매도
	
	
//	==========t_cash===========
	public UserCashDTO findUserCashByUserNum(int userNum); //유저 보유 현금 조회
	public int updateUserCash(UserCashDTO userCash); //현금정보 업데이트
	
	
//	==========t_history===========
	List<HistoryDTO> findTradeHistoryByUserNum(int userNum); //거래내역 조회
	public int insertTradeHistory(HistoryDTO history); //거래내역 추가
	
	
	
	
}
