package com.frade.service.order;

import java.util.List;

import com.frade.dto.order.HistoryDTO;
import com.frade.dto.order.OrderInfoDTO;
import com.frade.dto.user.PortfolioDTO;
import com.frade.dto.user.UserCashDTO;

public interface OrderService {
	
	public int processBuy(OrderInfoDTO orderInfo); //매수 검증
	public int processSell(OrderInfoDTO orderInfo); //매도 검증
	
	
	

	
//	==============DAO===============
	
//	=============t_history==============
	public List<PortfolioDTO> findUserPortfolioListByUserNum(int userNum); //portfolio 테이블 정보를 list로 받기
	public PortfolioDTO findUserPortfolioByUserNumAndStockCode(int userNum, String stockCode); //종목별 포트폴리오 찾기
	public int insertUserPortfolio(PortfolioDTO portfolio); //포트폴리오 신규저장
	public int updateUserPortfolio(PortfolioDTO portfolio); //포트폴리오 기존 보유종목 업데이트
	public int deleteUserPortfolioByUserNumAndStockCode(int userNum, String stockCode); //전량매도
	
//	=============t_cash==============
	public UserCashDTO findUserCashByUserNum(int userNum); //현금정보 찾기
	public int updateUserCash(UserCashDTO userCash); //현금정보 저장
	
//	=============t_history==============
	public List<HistoryDTO> findTradeHistoryByUsernum(int userNum); //거래내역 조회
	public int insertTradeHistory(HistoryDTO history); //거래내역 저장
	
}
