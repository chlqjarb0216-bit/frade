package com.frade.dto.user;

import java.util.List;

import com.frade.dto.order.HistoryForMypageDTO;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class MyPagePortfolioDTO {

	AssetsInfoDTO assetsInfo;
	List<PortfolioInfoDTO> portfolioInfoList;
	List<HistoryForMypageDTO> historyList;
	List<String> stockNameList;
	List<Long> stockPriceList;
	
	public MyPagePortfolioDTO(AssetsInfoDTO assetsInfo, List<PortfolioInfoDTO> portfolioInfoList,
			List<HistoryForMypageDTO> historyList, List<String> stockNameList, List<Long> stockPriceList) {
		
		this.assetsInfo = assetsInfo;
		this.portfolioInfoList = portfolioInfoList;
		this.historyList = historyList;
		this.stockNameList = stockNameList;
		this.stockPriceList = stockPriceList;
		
	}
	
	

}
