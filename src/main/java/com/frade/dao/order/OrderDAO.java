package com.frade.dao.order;

import java.util.List;

import com.frade.dto.order.HistoryDTO;
import com.frade.dto.order.OrderInfoDTO;
import com.frade.dto.user.PortfolioDTO;
import com.frade.dto.user.UserCashDTO;

public interface OrderDAO {
	
//	  - tradeCashCaculator() / tradePortfolioCaculator(): 유저 보유 현금/수량 계산 로직
//	  - saveTradeHistory(), saveUserPortfolio(), saveUserCash(): 거래 후 DB 저장
//	  - findUserPortfolioByUnum(), findUserCashByUnum(): 사전 자산 검증용 조회
	
	public List<PortfolioDTO> findUserPortfolioByUnum(int Unum); //portfolio 테이블 정보를 list로 받기
	public UserCashDTO findUserCashByUnum(int Unum); //유저 보유 현금 조회
	
	public int saveUserPortfolio(PortfolioDTO portfolio); //포트폴리오 저장
	public int saveTradeHistory(HistoryDTO history); //거래내역 저장
	public int saveUserCash(UserCashDTO userCash); //현금정보 저장
	
	
}
