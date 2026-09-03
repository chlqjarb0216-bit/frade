package com.frade.dto.user;

import lombok.Data;

@Data
public class AssetsInfoDTO {
	
	long totalAsset; //총 자산 (주식 평가금 + 예수금)
	long totalValuation;  //주식 평가금
	long cash; //예수금
	int stockCnt; //보유 종목 수 (userNum으로 t_portfolio count)
	int tradeCnt; //거래 횟수 (userNum으로 t_history count)
	long totalRevenue; //총 이익금
	double revenuePercent; //총이익률
	
}
