package com.frade.dto.user;

import java.util.List;

import com.frade.dto.order.HistoryForMypageDTO;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class MyPagePortfolioDTO {

	AssetsInfoDTO assetsInfo;
	List<PortfolioInfoDTO> portfolioInfoList;
	List<HistoryForMypageDTO> historyList;
	List<String> stockNameList;
	List<Long> stockPriceList;

}
