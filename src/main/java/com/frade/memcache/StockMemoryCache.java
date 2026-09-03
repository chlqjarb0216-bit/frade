package com.frade.memcache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.frade.dao.stock.StockDAO;
import com.frade.dto.stock.StockInfoDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockMemoryCache {

	private final StockDAO stockDAO;

	private Map<String, StockInfoDTO> codeCacheMap = new HashMap<>();
	private List<StockInfoDTO> allStockList = new ArrayList<>();

	/**
	 * 서버가 켜질때 딱 1번 실행 
	 * DB에 등록된 100종목을 자바 메모리에 탑재
	 */
	@PostConstruct
	public void initStockCache() {
		refreshCache();
	}

	/**
	 * 캐시를 최신 상태로 새로고침하는 메서드
	 * 서버 시동 시점에 호출
	 */
	public void refreshCache() {
		log.info("DB로부터 100종목 데이터를 로드하여 캐시 생성");

		// DB에서 데이터 로드
		List<StockInfoDTO> dbStocks = stockDAO.selectAllStock();

		if (dbStocks == null || dbStocks.isEmpty()) {
			log.warn("DB에 종목 데이터가 없습니다. 캐시 로드를 스킵합니다.");
			return;
		}

		// 2. 기존 캐시 메모리 초기화
		Map<String, StockInfoDTO> newMap = new HashMap<>();
		List<StockInfoDTO> newList = new ArrayList<>();

		// 3. 초고속 조회를 위한 메모리 적재
		for (StockInfoDTO stock : dbStocks) {
			newMap.put(stock.getStockCode(), stock); // 코드 검색용 맵 채우기
			newList.add(stock); // 이름/자동완성 검색용 리스트 채우기
		}

		this.codeCacheMap = newMap;
		this.allStockList = newList;

		log.info("주식 정보 {}개 캐시 메모리 탑재 완료.", codeCacheMap.size());
	}

	/**
	 * 캐시를 최신 상태로 새로고침하는 메서드
	 * 아침 8시 40분 장전 마스터 배치가 완전히 끝난 직후에 호출
	 */
	public void refreshCache(List<StockInfoDTO> stockList) {
		log.info("키움 REST API로 받아온 데이터로 캐시 생성");

		if (stockList == null || stockList.isEmpty()) {
			log.warn("종목 데이터가 없습니다. 캐시 로드를 스킵합니다.");
			return;
		}

		// 2. 기존 캐시 메모리 초기화
		// 장중에 호출할 시 이부분을 새 객체를 만들어서 갈아끼워야함
		codeCacheMap.clear();
		allStockList.clear();

		// 3. 초고속 조회를 위한 메모리 적재
		for (StockInfoDTO stock : stockList) {
			codeCacheMap.put(stock.getStockCode(), stock); // 코드 검색용 맵 채우기
			allStockList.add(stock); // 이름/자동완성 검색용 리스트 채우기
		}

		log.info("주식 정보 {}개 캐시 메모리 탑재 완료.", codeCacheMap.size());
	}

	/**
	 * 종목코드로 정확히 1건 조회
	 * DB 조회 없이 메모리 캐시에서 꺼내옴
	 */
	public StockInfoDTO getByCode(String stockCode) {
		return codeCacheMap.get(stockCode);
	}

	/**
	 * 종목이름 검색
	 * 수천 명이 타이핑해도 DB 조회는 0건.
	 */
	public List<StockInfoDTO> searchByName(String keyword) {
		if (keyword == null || keyword.trim().isEmpty()) {
			return Collections.emptyList();
		}

		String cleanKeyword = keyword.trim();

		// 메모리에 올라온 100개 글자 안에서만 스트림 필터링 수행
		return allStockList.stream().filter(stock -> stock.getStockName().contains(cleanKeyword)).limit(10) // 너무 많이 뜨면 화면이 지저분하므로 딱 10개만 자동완성 짤라주기
				.collect(Collectors.toList());
	}

	//맵에서 key들만 뽑아다 리스트로 반환
	public List<String> getCodeList() {
		return new ArrayList<String>(codeCacheMap.keySet());
	}

	public List<StockInfoDTO> getAllStockList() {
		return new ArrayList<>(this.allStockList);
	}
}