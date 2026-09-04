package com.frade.dao.portfolio;

import java.util.List;

import com.frade.dto.order.HistoryDTO;
import com.frade.dto.order.OrderInfoDTO;
import com.frade.dto.user.PortfolioDTO;
import com.frade.dto.user.UserCashDTO;

public interface PortfolioDAO {
	

	public List<PortfolioDTO> findUserPortfolioListByUserNum(int userNum); //portfolio 테이블 정보를 list로 받기
	public List<String> findUserPortfolioStockNameListByUserNum(int userNum); //유저가 가진 종목이름 리스트
	public PortfolioDTO findUserPortfolioByUserNumAndStockCode(int userNum, String stockCode); //종목별 포트폴리오 찾기

	//매도 검증용 행 잠금
	public PortfolioDTO findUserPortfolioByUserNumAndStockCodeForUpdate(int userNum, String stockCode); 
	
	public int insertUserPortfolio(PortfolioDTO portfolio); //포트폴리오 신규저장
	public int updateUserPortfolio(PortfolioDTO portfolio); //포트폴리오 기존 보유종목 업데이트
	public int deleteUserPortfolioByUserNumAndStockCode(int userNum, String stockCode); //전량매도
	public int updateOrInsertUserPortfolio(PortfolioDTO portfolio); //merge into 포트폴리오
	
	
}
