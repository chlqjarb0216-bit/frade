package com.frade.service.portfolio;

import com.frade.dto.user.MyPagePortfolioDTO;
import com.frade.dto.user.PortfolioDTO;


public interface PortfolioService {

	//마이페이지 포트폴리오 정보
	MyPagePortfolioDTO getMyPagePortfolio(int userNum);

	//userNum, stockCode로 특정 종목 관련 정보
	PortfolioDTO findUserPortfolioByUserNumAndStockCode(int userNum, String stockCode);
	
	//종목 최초매수시 insert
	int insertUserPortfolio(PortfolioDTO portfolio);

	//종목 추가매수시 update
	int updateUserPortfolio(PortfolioDTO portfolio);

	//전량매도 (수량 0)시 userNum, stockcode로 데이터 삭제 
	int deleteUserPortfolioByUserNumAndStockCode(int userNum, String stockCode);
	
	//DB 조회 후 데이터 없으면 insert 있으면 update
	int updateOrInsertUserPortfolio(PortfolioDTO portfolio);

}
