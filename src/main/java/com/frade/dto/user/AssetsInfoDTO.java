package com.frade.dto.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class AssetsInfoDTO {
	
	long totalAsset; //총 자산 (주식 평가금 + 예수금)
	long totalValuation;  //주식 평가금
	long cash; //예수금
	int stockCnt; //보유 종목 수 (userNum으로 t_portfolio count)
	int tradeCnt; //거래 횟수 (userNum으로 t_history count)
	long totalRevenue; //총 이익금
	double revenuePercent; //총이익률
	


	public AssetsInfoDTO(long totalValuation, long cash, int stockCnt, int tradeCnt, long INITIAL_ASSET) {
		
		this.totalAsset = totalValuation + cash;
		this.totalRevenue = totalAsset - INITIAL_ASSET;
		this.totalValuation = totalValuation;
		this.cash = cash;
		this.stockCnt = stockCnt;
		this.tradeCnt = tradeCnt;
		this.revenuePercent = Math.round((double) totalRevenue / INITIAL_ASSET * 10000) / 100.0;
	}
	
}
