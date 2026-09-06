package com.frade.util;

import java.time.LocalDateTime;

public class MarketUtil {

	//평일(월~금)이면서 시각이 09:00:00 ~ 15:30:00 사이인지 판정
	public static boolean isMarketOpenTime() {
		LocalDateTime now = LocalDateTime.now();
		java.time.DayOfWeek day = now.getDayOfWeek();

		//토요일이거나 일요일이면 장이 닫혔으므로 탈락
		if (day == java.time.DayOfWeek.SATURDAY || day == java.time.DayOfWeek.SUNDAY) {
			return false;
		}

		//시간과 분을 이어붙여 직관적인 크기 비교
		int hhmm = now.getHour() * 100 + now.getMinute();

		// 09시 00분부터 15시 30분 사이일 때만 True 반환!
		return hhmm >= 900 && hhmm <= 1530;
	}

}
