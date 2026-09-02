package com.frade.common.stock;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 업종명을 확인하기위해 매번 DB를 조회하는 것을 피하기 위한 ENUM

@Getter
@AllArgsConstructor
public enum StockSector {
	CHEMICAL(1, "화학"), OTHER_FINANCE(2, "기타금융"), ELECTRIC_ELECTRONIC(3, "전기·전자"), RETAIL(4, "유통"),
	TRANSPORT_EQUIPMENT(5, "운송장비·부품"), METAL(6, "금속"), PHARMACEUTICAL(7, "제약"), FOOD_TOBACCO(8, "음식료·담배"),
	CONSTRUCTION(9, "건설"), GENERAL_SERVICE(10, "일반서비스"), MACHINERY(11, "기계·장비"), SECURITIES(12, "증권"),
	TEXTILE_CLOTHING(13, "섬유·의류"), TRANSPORT_WAREHOUSE(14, "운송·창고"), IT_SERVICE(15, "IT 서비스"), REAL_ESTATE(16, "부동산"),
	NON_METAL(17, "비금속"), PAPER_WOOD(18, "종이·목재"), INSURANCE(19, "보험"), ENTERTAINMENT(20, "오락·문화"),
	ELECTRIC_GAS(21, "전기·가스"), OTHER_MANUFACTURE(22, "기타제조"), MEDICAL_PRECISION(23, "의료·정밀기기"), TELECOM(24, "통신"),
	BANK(25, "은행"), AGRICULTURE(26, "농업, 임업 및 어업"), ETC(99, "임시분류"); //디폴트 예외처리용

	private final int scNum;
	private final String scName;

	// 업종명 탐색용 해시맵
	private static final Map<String, Integer> NAME_TO_NUM_CACHE_MAP = new HashMap<>();
	// 업종번호 탐색용 해시맵
	private static final Map<Integer, String> NUM_TO_NAME_CACHE_MAP = new HashMap<>();
	// 클래스가 메모리에 로드될 때(서버 부팅 시) 딱 1번만 실행되는 스태틱 블록
	static {
		for (StockSector sector : values()) {
			NAME_TO_NUM_CACHE_MAP.put(sector.getScName(), sector.getScNum());
		}
		for (StockSector sector : values()) {
			NUM_TO_NAME_CACHE_MAP.put(sector.getScNum(), sector.getScName());
		}
	}

	// 업종명 탐색 (기본값 99임시분류)
	public static int getSectorNumber(String upName) {
		if (upName == null || upName.trim().isEmpty()) {
			return ETC.scNum;
		}
		return NAME_TO_NUM_CACHE_MAP.getOrDefault(upName, ETC.scNum);
	}

	// 업종번호 탐색 (기본값 99임시분류)
	public static String getSectorName(int scNum) {
		return NUM_TO_NAME_CACHE_MAP.getOrDefault(scNum, ETC.scName);
	}
}
