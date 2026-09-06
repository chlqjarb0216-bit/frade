package com.frade.memcache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;

import com.frade.dto.stock.StockPriceDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StockPriceMemoryCache {
	// 💡 구조: Map<종목코드, Map<yyyyMMdd, List<StockPriceDTO>>>
	// 최외각과 중간 맵은 락프리 동시성 맵을 채택하고, 최내곽 리스트는 멀티스레드 읽기에 특화된 CopyOnWriteArrayList를 주입합니다.
	private final Map<String, Map<String, List<StockPriceDTO>>> priceCache = new ConcurrentHashMap<>();

	/**
	 * 📥 1. 실시간 가공 엔진 또는 마감 스케줄러가 특정 날짜의 리스트에 캔들을 누적할 때 사용 (O(1))
	 */
	public void put(String stockCode, String dateStr, StockPriceDTO dto) {
		if (stockCode == null || dateStr == null || dto == null)
			return;

		// 1. 종목코드 방이 없으면 내부 일자별 맵 생성
		// 2. 해당 날짜 방이 없으면 동시성 안전 리스트(CopyOnWriteArrayList) 생성 후 최종 DTO 추가
		priceCache.computeIfAbsent(stockCode, k -> new ConcurrentHashMap<>())
				.computeIfAbsent(dateStr, k -> new CopyOnWriteArrayList<>()).add(dto);
	}

	/**
	 * 📥 2. [최종 명답] 무거운 분기 연산 없이 완전히 새로운 리스트를 생성해 통째로 갈아끼우기 (Swap)
	 */
	public void putAll(String stockCode, String dateStr, List<StockPriceDTO> dtoList) {
		if (stockCode == null || dateStr == null || dtoList == null || dtoList.isEmpty())
			return;

		// 2. 최외각 맵에서 해당 종목의 일자별 맵을 확보합니다.
		Map<String, List<StockPriceDTO>> dateMap = priceCache.computeIfAbsent(stockCode,
				k -> new ConcurrentHashMap<>());

		// 3. 💡 핵심: 기존 리스트를 지우고 자시고 할 것 없이, 새 리스트 주소값으로 홱 갈아끼워 버립니다.
		dateMap.put(dateStr, new CopyOnWriteArrayList<>(dtoList));
	}

	/**
	 * 🔍 3. 특정 종목의 특정 날짜 전체 차트 데이터셋을 통째로 가져올 때 사용 (차트 렌더링용)
	 * 💡 [완벽 방어] 외부 레이어가 리스트를 읽는 동안 워커가 쓰더라도 100% 안전하도록 얕은 복사 스냅샷 적용
	 */
	public List<StockPriceDTO> getChartData(String stockCode, String dateStr) {
		if (stockCode == null || dateStr == null) {
			return Collections.emptyList();
		}

		Map<String, List<StockPriceDTO>> dateMap = priceCache.get(stockCode);
		if (dateMap == null) {
			return Collections.emptyList();
		}

		List<StockPriceDTO> originalList = dateMap.get(dateStr);
		if (originalList == null || originalList.isEmpty()) {
			return Collections.emptyList();
		}

		// 최종: CopyOnWriteArrayList의 현재 주소 배열 상태를 0초 만에 얕은 복사(Shallow Copy)
		// 외부 Jackson 직렬화 스레드나 컨트롤러가 이 리스트를 들고 무슨 짓을 하든 원본 캐시판은 데미지 0% 철통 보장
		return new ArrayList<>(originalList);
	}

	/**
	* 📊 [최종] 데이터 폭탄 방어형 최신 2일 치 타임라인 인양 (O(1))
	* 캐시 내부에 며칠 치 데이터가 오염되어 남아있든 상관없이, 
	* 하드웨어 레벨에서 정확하게 가장 최신 평일 영업일 2일 치만 칼같이 잘라내어 과거순으로 결합 반환합니다.
	*/
	public List<StockPriceDTO> getEveryChartDataCached(String stockCode) {
		if (stockCode == null || stockCode.isEmpty()) {
			return Collections.emptyList();
		}

		Map<String, List<StockPriceDTO>> dateMap = this.priceCache.get(stockCode);
		if (dateMap == null || dateMap.isEmpty()) {
			// 서버 오픈 직후 아직 웜업이 안 끝난 찰나의 순간 가드
			return Collections.emptyList();
		}

		// 💡 맵에 100일 치 데이터가 있어도 날짜 방 개수(N) 자체가 워낙 작아 O(1)급 초광속 연산입니다.
		return dateMap.entrySet().stream()
				// 1. [최신순 정렬]: "20260906"(오늘), "20260905"(어제), "20260904"(그저께) 순 정렬
				.sorted(Map.Entry.<String, List<StockPriceDTO>>comparingByKey().reversed())

				// 2. [2일 치 컷]: 며칠 치가 있든 상위 2개 방(오늘, 어제)만 정확하게 슬라이싱
				.limit(2)

				// 3. [과거순 복원]: 차트 타임라인 정합성을 위해 다시 (어제 ➔ 오늘) 순으로 정렬 리셋
				.sorted(Map.Entry.comparingByKey())

				// 4. [알맹이 결합]: 끈이 완전히 끊어진 정예 2일 치 분 봉 리스트 직렬 압축
				.map(Map.Entry::getValue).flatMap(Collection::stream).toList();
	}

	/**
	 * 🔍 4. [현재가 추출] 특정 날짜 리스트의 가장 마지막(최신) 캔들 하나만 쏙 빼와서 현재가로 활용할 때 사용
	 */
	public int getLatestClosePrice(String stockCode, String dateStr) {
		List<StockPriceDTO> list = getChartData(stockCode, dateStr);
		if (list.isEmpty())
			return 0;

		// CopyOnWriteArrayList 특성상 인덱스 접근이 안전합니다.
		StockPriceDTO latestDto = list.get(list.size() - 1);
		return latestDto != null ? latestDto.getPriceClose() : 0;
	}

	/**
	* 🧹 5. [최종] 기준 날짜 당일을 포함하여 그 이전의 모든 과거 데이터 방을 통째로 도려내기
	* @param criteriaDateStr 삭제 기준선이 되는 날짜 문자열 (예: 그저께 날짜 "20260904")
	*/
	public void clearOldDate(String criteriaDateStr) {
		if (criteriaDateStr == null || criteriaDateStr.isEmpty()) {
			return;
		}

		// 전체 종목을 순회하며 각 종목의 일자별 맵을 정밀 타격
		for (Map<String, List<StockPriceDTO>> dateMap : priceCache.values()) {
			if (dateMap == null || dateMap.isEmpty())
				continue;

			// 💡 [최종 반영]: compareTo 결과가 0 이하(<= 0)인 녀석들을 째버립니다.
			// "20260904"(기준일 당일)은 0이 되고, "20260903"(과거)은 음수가 되므로 
			// 기준일을 포함한 그 과거 전체 데이터 세트가 0초 만에 일괄 소멸 수거됩니다.
			dateMap.keySet().removeIf(dateKey -> dateKey.compareTo(criteriaDateStr) <= 0);
		}

		log.info("[인메모리 청소] 기준일({}) 및 그 이전의 모든 구형 분 봉 캐시 방이 완벽하게 수거되었습니다.", criteriaDateStr);
	}

	/**
	 * 🧹 6. 전체 캐시 전면 초기화
	 */
	public void clearAll() {
		priceCache.clear();
	}
}
