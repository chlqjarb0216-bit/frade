package com.frade.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MarketUtil {

	public static final DateTimeFormatter DATE_FORM = DateTimeFormatter.ofPattern("yyyyMMdd");

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

	public static String[] getLast2MarketDayString() {
		LocalDate now = LocalDate.now();
		DayOfWeek dayOfWeek = now.getDayOfWeek();

		LocalDate targetToday;
		LocalDate targetYesterday;

		// 100% 자바 엔진 내부 시계로만 판단하므로 DB 부하 없이 0초만에 요일 판별
		if (dayOfWeek == DayOfWeek.SATURDAY) {
			// 토요일 부팅 시 ➔ 실제 데이터가 있는 금요일과 목요일로 매핑
			targetToday = now.minusDays(1);
			targetYesterday = now.minusDays(2);
		} else if (dayOfWeek == DayOfWeek.SUNDAY) {
			// 일요일 부팅 시 ➔ 실제 데이터가 있는 금요일과 목요일로 매핑
			targetToday = now.minusDays(2);
			targetYesterday = now.minusDays(3);
		} else if (dayOfWeek == DayOfWeek.MONDAY) {
			// 월요일 부팅 시 ➔ 오늘(월) 데이터와 직전 영업일인 금요일(3일 전)로 매핑
			targetToday = now;
			targetYesterday = now.minusDays(3);
		} else {
			// 화~금 평일 부팅 시 ➔ 오늘 데이터와 어제 데이터로 정상 매핑
			targetToday = now;
			targetYesterday = now.minusDays(1);
		}

		String todayStr = targetToday.format(DATE_FORM);
		String yesterdayStr = targetYesterday.format(DATE_FORM);

		return new String[] { todayStr, yesterdayStr };
	}

}
