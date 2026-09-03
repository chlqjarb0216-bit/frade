package com.frade.service.main.impl;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.frade.dto.community.PostDTO;
import com.frade.dto.stock.StockPreviewDTO;
import com.frade.service.main.MainService;
/**
 * 메인 대시보드 화면 서비스 구현체
 * (API 설계 전 테스트용 Mock 데이터 제공)
 */
@Service
public class MainServiceImpl implements MainService {
	@Override
	public List<StockPreviewDTO> getTop5Stocks() {
		List<StockPreviewDTO> stockList = new ArrayList<>();
		// 1. 실시간 주식 상위 5개 테스트 데이터
		stockList.add(new StockPreviewDTO("005930", "삼성전자", "전기·전자", 78500, 76700));
		stockList.add(new StockPreviewDTO("000660", "SK하이닉스", "전기·전자", 192400, 185300));
		stockList.add(new StockPreviewDTO("373220", "LG에너지솔루션", "2차전지", 395000, 400000));
		stockList.add(new StockPreviewDTO("005380", "현대차", "자동차", 248000, 244000));
		stockList.add(new StockPreviewDTO("035420", "NAVER", "IT서비스", 172300, 173300));
		return stockList;
	}
	@Override
	public List<PostDTO> getTop5Posts() {
		List<PostDTO> postList = new ArrayList<>();
		// 2. 커뮤니티 인기글 상위 5개 테스트 데이터
		PostDTO p1 = new PostDTO();
		p1.setPostNum(1);
		p1.setUserNum(101);
		p1.setUserName("화성갈끄니까");
		p1.setPostCategoryNum(2); // 자유
		p1.setPostTitle("오늘 삼전 7.8만 돌파 기념 수익 인증합니다 (feat. 10만전자)");
		p1.setPostContent("작년부터 꾸준히 모아왔는데 드디어 수익권 들어왔네요. 다들 성투하세요!");
		p1.setPostViewCnt(4210);
		p1.setPostLikeCnt(188);
		p1.setPostPostedDate(LocalDateTime.now().minusHours(1));
		postList.add(p1);
		PostDTO p2 = new PostDTO();
		p2.setPostNum(2);
		p2.setUserNum(102);
		p2.setUserName("배당파이프라인");
		p2.setPostCategoryNum(1); // 정보
		p2.setPostTitle("초보 투자자를 위한 하반기 배당 성장주 포트폴리오 비중 가이드");
		p2.setPostContent("금리 인하 사이클에 맞춰 배당 안정성과 성장성을 동시에 고려한 포트폴리오입니다.");
		p2.setPostViewCnt(3150);
		p2.setPostLikeCnt(142);
		p2.setPostPostedDate(LocalDateTime.now().minusHours(2));
		postList.add(p2);
		PostDTO p3 = new PostDTO();
		p3.setPostNum(3);
		p3.setUserNum(103);
		p3.setUserName("차트의정석");
		p3.setPostCategoryNum(1); // 정보
		p3.setPostTitle("외국인/기관 순매수 상위 종목 수급 및 차트 기술적 분석");
		p3.setPostContent("반도체 대형주 중심의 외국인 프로그램 순매수 유입 현황 공유드립니다.");
		p3.setPostViewCnt(2680);
		p3.setPostLikeCnt(95);
		p3.setPostPostedDate(LocalDateTime.now().minusHours(3));
		postList.add(p3);
		PostDTO p4 = new PostDTO();
		p4.setPostNum(4);
		p4.setUserNum(104);
		p4.setUserName("스캘핑장인");
		p4.setPostCategoryNum(2); // 자유
		p4.setPostTitle("실전 모의투자 랭킹 1위 회원의 일간 단타 매매일지 복기");
		p4.setPostContent("시초가 갭상승 종목 진입 시 분봉 지지선 확인 원칙을 준수했습니다.");
		p4.setPostViewCnt(2210);
		p4.setPostLikeCnt(84);
		p4.setPostPostedDate(LocalDateTime.now().minusHours(4));
		postList.add(p4);
		PostDTO p5 = new PostDTO();
		p5.setPostNum(5);
		p5.setUserNum(105);
		p5.setUserName("단타요정");
		p5.setPostCategoryNum(1); // 정보
		p5.setPostTitle("단타 매매할 때 호가창 체결강도 및 거래량 보는 핵심 팁 3가지");
		p5.setPostContent("체결강도 120% 이상 유지 여부와 총매수/총매도 잔량 비율 해석법입니다.");
		p5.setPostViewCnt(1890);
		p5.setPostLikeCnt(71);
		p5.setPostPostedDate(LocalDateTime.now().minusHours(5));
		postList.add(p5);
		return postList;
	}
}