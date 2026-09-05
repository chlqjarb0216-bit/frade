package com.frade.service.portfolio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frade.dao.portfolio.PortfolioDAO;
import com.frade.dto.order.HistoryDTO;
import com.frade.dto.user.AssetsInfoDTO;
import com.frade.dto.user.PortfolioDTO;
import com.frade.dto.user.UserCashDTO;


public interface PortfolioService {

	//userNum으로 포트폴리오 리스트
	List<PortfolioDTO> findUserPortfolioListByUserNum(int userNum);

	//유저가 가진 종목이름 리스트
	List<String> findUserPortfolioStockNameListByUserNum(int userNum);

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

	//Asset 정보
	AssetsInfoDTO getAssetsInfo(int userNum);
	
}
