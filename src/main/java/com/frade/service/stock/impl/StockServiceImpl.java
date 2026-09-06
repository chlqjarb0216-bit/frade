package com.frade.service.stock.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frade.common.stock.StockSector;
import com.frade.dao.stock.StockDAO;
import com.frade.dao.stock.StockPriceDAO;
import com.frade.dto.stock.StockInfoDTO;
import com.frade.dto.stock.StockPreviewDTO;
import com.frade.dto.stock.StockPriceDTO;
import com.frade.memcache.StockMemoryCache;
import com.frade.memcache.StockPriceMemoryCache;
import com.frade.memcache.StockRankingCache;
import com.frade.service.api.KiwoomApiService;
import com.frade.service.stock.StockDataBufferService;
import com.frade.service.stock.StockService;
import com.frade.util.MarketUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StockServiceImpl implements StockService {

	@Autowired
	KiwoomApiService kiwoomApiService;
	@Autowired
	StockDataBufferService stockDataBufferService;

	@Autowired
	StockDAO stockDAO;
	@Autowired
	StockPriceDAO stockPriceDAO;

	@Autowired
	StockMemoryCache stockMemoryCache;
	@Autowired
	StockRankingCache stockRankingCache;
	@Autowired
	StockPriceMemoryCache stockPriceMemoryCache;

	@Override
	public List<StockInfoDTO> searchStockByName(String stockName) {
		return stockMemoryCache.searchByName(stockName);
	}

	@Override
	public List<String> getStockCodeList() {
		return stockMemoryCache.getStockCodeList();
	}

	@Override
	public StockPreviewDTO getStockPreviewByStockCode(String stockCode) {
		return stockRankingCache.getPreviewByStockCode(stockCode);
	}

	@Override
	public List<StockPreviewDTO> getSortedStockRankingListPage(int pageIdx, int pageSize) {
		return stockRankingCache.getSortedCachePage(pageIdx, pageSize);
	}

	@Override
	public void refreshAndSwapRealtimeRankingCache() {
		//현재가를 버퍼에서 맵으로 가져옴
		Map<String, Integer> priceSnapshots = stockDataBufferService.getMinPriceSnapshotMap();
		if (priceSnapshots.isEmpty())
			return;

		//갈아끼울 새 맵 작성
		Map<String, StockPreviewDTO> tempMap = new HashMap<>();
		//정렬
		List<StockPreviewDTO> previewList = stockMemoryCache.getAllStockList().stream().map(info -> {
			if (info.getPrevDayClosePrice() <= 0)
				return null;
			Integer currentPrice = priceSnapshots.get(info.getStockCode());
			if (currentPrice == null) {
				StockPreviewDTO prevCache = stockRankingCache.getPreviewByStockCode(info.getStockCode());
				currentPrice = prevCache != null ? prevCache.getPrice() : 0;
				if (currentPrice <= 0) {
					currentPrice = info.getPrevDayClosePrice();
				}
			}

			// 화면 출력용 객체 조립
			StockPreviewDTO previewDTO = new StockPreviewDTO(info.getStockCode(), info.getStockName(),
					StockSector.getSectorName(info.getSectorNum()), currentPrice, info.getPrevDayClosePrice());

			tempMap.put(info.getStockCode(), previewDTO);
			return previewDTO;
		}).filter(Objects::nonNull)
				.sorted((o1, o2) -> Double.compare(o2.getDailyPriceChangeRate(), o1.getDailyPriceChangeRate())) // dailyChangeRate(당일 등락률) 기준 내림차순 정렬
				.toList();
		stockRankingCache.updateSortedCache(previewList, tempMap);
	}

	@Override
	public void putStockPriceListToStockPriceMemoryCache(String stockCode, String dayString,
			List<StockPriceDTO> priceList) {
		stockPriceMemoryCache.putAll(stockCode, dayString, priceList);
	}

	@Override
	public void flushCompletedMinuteBufferAndSave() {
		// 1. 인메모리 버퍼 팩토리에서 마감된 데이터 리스트 제로카피로 안전 인양
		List<StockPriceDTO> completedList = stockDataBufferService.flushCompleteMinuteBuffer();

		// 2. [가드] 없으면 서비스 레이어 단에서 조용히 트랜잭션 종료
		if (completedList == null || completedList.isEmpty()) {
			return;
		}

		// 3. 오라클 레포지토리로 토스하여 원샷 벌크 인서트 때려박기
		int result = stockPriceDAO.insertMinuteStockPrice(completedList);

		// 💡 4. [최종 완성] 오라클 적재 성공 직후, 분리 상주 중인 대형 일자별 캐시에 동시 동기화 적재(put)
		String todayStr = LocalDate.now().format(MarketUtil.DATE_FORM);

		for (StockPriceDTO dto : completedList) {
			// 구조: Map<종목코드, Map<yyyyMMdd, List<StockPriceDTO>>> 공간에 차곡차곡 누적
			stockPriceMemoryCache.put(dto.getStockCode(), todayStr, dto);
		}

		log.info("분단위 DB저장 완료. 기준시간: {} ➔ 총 {}건 오라클 적재 및 메모리캐시 갱신 성공", completedList.get(0).getDateTime(), result);
	}

	@Override
	public List<StockPriceDTO> selectMinuteStockPriceListByStockCodeAndDayString(String stockCode, String dayString) {
		return stockPriceDAO.selectMinuteStockPriceListByStockCodeAndDayString(stockCode, dayString);
	}

	@Override
	public void revokeApiToken() {
		kiwoomApiService.revokeToken();
	}

	@Override
	public int updateStockInfoList() {
		List<StockInfoDTO> infoList = kiwoomApiService.getMarketAllStockInfo();

		// 1. XML 문자열 조립
		StringBuilder xmlBuilder = new StringBuilder(infoList.size() * 300);
		xmlBuilder.append("<rows>");
		for (StockInfoDTO dto : infoList) {
			xmlBuilder.append("<row>").append("<stockCode>").append(dto.getStockCode()).append("</stockCode>")
					.append("<stockName><![CDATA[").append(dto.getStockName()).append("]]></stockName>")
					.append("<sectorNum>").append(dto.getSectorNum()).append("</sectorNum>").append("<stockStatus>")
					.append(dto.getStockStatus()).append("</stockStatus>").append("<prevDayClosePrice>")
					.append(dto.getPrevDayClosePrice()).append("</prevDayClosePrice>").append("</row>");
		}
		xmlBuilder.append("</rows>");

		// 2. DB 작업
		List<String> updatedCodes = stockDAO.updateAllStockAndReturnMatchedCodeList(xmlBuilder.toString());

		// 3. 100종목 검열
		if (updatedCodes != null && !updatedCodes.isEmpty()) {
			Set<String> updatedCodeSet = new HashSet<>(updatedCodes);

			List<StockInfoDTO> filteredCacheList = infoList.stream()
					.filter(dto -> updatedCodeSet.contains(dto.getStockCode())).toList();

			stockMemoryCache.refreshCache(filteredCacheList);

			return filteredCacheList.size();
		}
		return 0;
	}

	@Override
	public void initMemoryCache() {
		stockMemoryCache.initStockCache();
	}

	@Override
	public void warmUpStockPriceMemoryCache(String todayStr, String yesterdayStr) {
		List<String> activeStockCodes = this.getStockCodeList();
		if (activeStockCodes == null || activeStockCodes.isEmpty())
			return;

		int loadedCount = 0;

		for (String stockCode : activeStockCodes) {
			// 1. 어제 자(직전 영업일) 과거 분 봉 세트 인양 및 캐시 주소 스왑 (380개 봉 확보)
			List<StockPriceDTO> yesterdayDbBars = stockPriceDAO
					.selectMinuteStockPriceListByStockCodeAndDayString(stockCode, yesterdayStr);
			if (!yesterdayDbBars.isEmpty()) {
				stockPriceMemoryCache.putAll(stockCode, yesterdayStr, yesterdayDbBars);
			}

			// 2. 오늘 자(장중 서버 재부팅 시 대비) 오전 데이터 인양 및 캐시 주소 스왑
			List<StockPriceDTO> todayDbBars = stockPriceDAO.selectMinuteStockPriceListByStockCodeAndDayString(stockCode,
					todayStr);
			if (!todayDbBars.isEmpty()) {
				stockPriceMemoryCache.putAll(stockCode, todayStr, todayDbBars);
			}

			loadedCount++;
		}

		log.info("오늘({}) 및 직전 영업일({}) 이틀 치 오라클 데이터 인메모리 복원 완료 (총 {}개 종목 웜업 종결)", todayStr, yesterdayStr, loadedCount);
	}

	@Override
	public void clearOldStockPriceCache(String dayString) {
		stockPriceMemoryCache.clearOldDate(dayString);
	}
}
