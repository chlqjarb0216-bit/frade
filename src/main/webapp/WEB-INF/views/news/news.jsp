<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>실시간 증시 뉴스 - Frade</title>

<!-- 부트스트랩 5 CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
	crossorigin="anonymous">
<!-- 부트스트랩 아이콘 -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

<style>
:root {
	--main-bg: #f8fafc;
	--card-bg: #ffffff;
	--border-color: #e2e8f0;
	--text-primary: #1e293b;
	--text-secondary: #64748b;
	--text-muted: #94a3b8;
	--color-up: #e53935;
	--color-up-bg: #fee2e2;
	--color-down: #1e88e5;
	--color-down-bg: #dbeafe;
	--color-primary: #2563eb;
	--radius-card: 12px;
}

body {
	background-color: var(--main-bg);
	color: var(--text-primary);
	font-family: -apple-system, BlinkMacSystemFont, "Apple SD Gothic Neo", "Pretendard", Roboto, "Noto Sans KR", sans-serif;
	margin: 0;
	padding: 0;
}

.news-container {
	max-width: 1200px;
	margin: 24px auto 60px;
	padding: 0 16px;
}

/* 상단 타이틀 영역 */
.news-page-title-box {
	margin-bottom: 20px;
	padding-bottom: 12px;
	border-bottom: 2px solid #e2e8f0;
	display: flex;
	justify-content: space-between;
	align-items: flex-end;
}

.news-page-title {
	font-size: 24px;
	font-weight: 800;
	color: #0f172a;
	margin: 0;
	display: flex;
	align-items: center;
	gap: 8px;
}

.news-page-desc {
	font-size: 13px;
	color: var(--text-secondary);
	margin: 6px 0 0;
}

/* 실시간 속보 티커 배너 */
.breaking-banner {
	background: linear-gradient(90deg, #1e293b 0%, #0f172a 100%);
	border-radius: 10px;
	padding: 10px 18px;
	margin-bottom: 20px;
	display: flex;
	align-items: center;
	gap: 12px;
	color: #fff;
	box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.breaking-badge {
	background-color: #ef4444;
	color: #fff;
	font-size: 11px;
	font-weight: 700;
	padding: 3px 8px;
	border-radius: 4px;
	letter-spacing: 0.5px;
	white-space: nowrap;
	display: flex;
	align-items: center;
	gap: 4px;
}

.breaking-text {
	font-size: 13.5px;
	font-weight: 500;
	color: #f1f5f9;
	margin: 0;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	flex-grow: 1;
}

.breaking-time {
	font-size: 11px;
	color: #94a3b8;
	white-space: nowrap;
}

/* 시장 지수 미니 티커 바 */
.market-ticker-row {
	display: grid;
	grid-template-columns: repeat(4, 1fr);
	gap: 14px;
	margin-bottom: 24px;
}

.market-ticker-card {
	background: var(--card-bg);
	border: 1px solid var(--border-color);
	border-radius: 10px;
	padding: 12px 16px;
	display: flex;
	justify-content: space-between;
	align-items: center;
	box-shadow: 0 1px 3px rgba(0, 0, 0, 0.03);
}

.ticker-name {
	font-size: 12px;
	font-weight: 600;
	color: var(--text-secondary);
}

.ticker-val {
	font-size: 15px;
	font-weight: 700;
	color: #0f172a;
}

.ticker-change {
	font-size: 12px;
	font-weight: 600;
}

.ticker-change.up {
	color: var(--color-up);
}

.ticker-change.down {
	color: var(--color-down);
}

/* 카테고리 탭 & 검색바 */
.filter-bar {
	background: var(--card-bg);
	border: 1px solid var(--border-color);
	border-radius: 12px;
	padding: 10px 16px;
	margin-bottom: 24px;
	display: flex;
	justify-content: space-between;
	align-items: center;
	gap: 16px;
	flex-wrap: wrap;
}

.category-tabs {
	display: flex;
	gap: 6px;
	list-style: none;
	margin: 0;
	padding: 0;
	flex-wrap: wrap;
}

.category-btn {
	border: none;
	background: transparent;
	padding: 6px 14px;
	border-radius: 20px;
	font-size: 13.5px;
	font-weight: 600;
	color: var(--text-secondary);
	cursor: pointer;
	transition: all 0.15s ease-in-out;
}

.category-btn:hover {
	background-color: #f1f5f9;
	color: #0f172a;
}

.category-btn.active {
	background-color: var(--color-primary);
	color: #ffffff;
}

.news-search-box {
	position: relative;
	width: 260px;
}

.news-search-box input {
	width: 100%;
	padding: 6px 36px 6px 14px;
	font-size: 13px;
	border: 1px solid var(--border-color);
	border-radius: 20px;
	outline: none;
	background-color: #f8fafc;
	transition: border-color 0.15s;
}

.news-search-box input:focus {
	border-color: var(--color-primary);
	background-color: #ffffff;
}

.news-search-box i {
	position: absolute;
	right: 12px;
	top: 50%;
	transform: translateY(-50%);
	color: var(--text-muted);
	font-size: 14px;
}

/* 주요 헤드라인 빅 카드 */
.featured-card {
	background: var(--card-bg);
	border: 1px solid var(--border-color);
	border-radius: var(--radius-card);
	overflow: hidden;
	margin-bottom: 24px;
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.04);
	transition: transform 0.15s, box-shadow 0.15s;
	cursor: pointer;
}

.featured-card:hover {
	transform: translateY(-2px);
	box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
}

.featured-img-box {
	position: relative;
	width: 100%;
	height: 260px;
	background: linear-gradient(135deg, #1e3a8a 0%, #0f172a 100%);
	display: flex;
	align-items: center;
	justify-content: center;
	color: #fff;
	overflow: hidden;
}

.featured-badge-top {
	position: absolute;
	top: 14px;
	left: 14px;
	background: rgba(37, 99, 235, 0.9);
	color: #fff;
	font-size: 11px;
	font-weight: 700;
	padding: 4px 10px;
	border-radius: 20px;
	backdrop-filter: blur(4px);
}

.featured-content {
	padding: 20px 24px;
}

.featured-title {
	font-size: 19px;
	font-weight: 700;
	color: #0f172a;
	margin: 0 0 10px;
	line-height: 1.45;
}

.featured-desc {
	font-size: 13.5px;
	color: var(--text-secondary);
	line-height: 1.6;
	margin-bottom: 14px;
}

.featured-meta {
	display: flex;
	align-items: center;
	justify-content: space-between;
	font-size: 12px;
	color: var(--text-muted);
}

.stock-tag {
	display: inline-block;
	background-color: #eff6ff;
	color: var(--color-primary);
	border: 1px solid #bfdbfe;
	font-size: 11px;
	font-weight: 600;
	padding: 2px 8px;
	border-radius: 4px;
	margin-right: 4px;
	text-decoration: none;
}

.stock-tag:hover {
	background-color: #dbeafe;
}

/* 일반 뉴스 리스트 */
.news-list-group {
	display: flex;
	flex-direction: column;
	gap: 14px;
	margin-bottom: 30px;
}

.news-item-card {
	background: var(--card-bg);
	border: 1px solid var(--border-color);
	border-radius: var(--radius-card);
	padding: 16px 20px;
	display: flex;
	gap: 18px;
	align-items: center;
	transition: all 0.15s ease-in-out;
	cursor: pointer;
	box-shadow: 0 1px 3px rgba(0, 0, 0, 0.02);
}

.news-item-card:hover {
	border-color: #cbd5e1;
	transform: translateX(4px);
	box-shadow: 0 4px 14px rgba(0, 0, 0, 0.05);
}

.news-item-body {
	flex: 1;
	min-width: 0;
}

.news-item-header {
	display: flex;
	align-items: center;
	gap: 8px;
	margin-bottom: 6px;
}

.news-category-badge {
	font-size: 11px;
	font-weight: 700;
	color: var(--color-primary);
	background-color: #eff6ff;
	padding: 2px 7px;
	border-radius: 4px;
}

.news-item-source {
	font-size: 11.5px;
	color: var(--text-muted);
	font-weight: 500;
}

.news-item-title {
	font-size: 15.5px;
	font-weight: 700;
	color: #0f172a;
	margin: 0 0 6px;
	line-height: 1.4;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.news-item-summary {
	font-size: 13px;
	color: var(--text-secondary);
	line-height: 1.5;
	margin: 0 0 10px;
	display: -webkit-box;
	-webkit-line-clamp: 2;
	-webkit-box-orient: vertical;
	overflow: hidden;
}

.news-item-footer {
	display: flex;
	align-items: center;
	justify-content: space-between;
	font-size: 11.5px;
	color: var(--text-muted);
}

.news-thumb-box {
	width: 120px;
	height: 86px;
	border-radius: 8px;
	background: #f1f5f9;
	flex-shrink: 0;
	overflow: hidden;
	display: flex;
	align-items: center;
	justify-content: center;
	color: #94a3b8;
	font-size: 28px;
}

/* 우측 사이드바 */
.sidebar-card {
	background: var(--card-bg);
	border: 1px solid var(--border-color);
	border-radius: var(--radius-card);
	padding: 18px 20px;
	margin-bottom: 20px;
	box-shadow: 0 2px 6px rgba(0, 0, 0, 0.03);
}

.sidebar-title {
	font-size: 15.5px;
	font-weight: 700;
	color: #0f172a;
	margin: 0 0 14px;
	padding-bottom: 10px;
	border-bottom: 1px solid #f1f5f9;
	display: flex;
	align-items: center;
	gap: 6px;
}

/* 실시간 많이 본 뉴스 랭킹 */
.top-news-list {
	list-style: none;
	margin: 0;
	padding: 0;
	display: flex;
	flex-direction: column;
	gap: 12px;
}

.top-news-item {
	display: flex;
	gap: 10px;
	align-items: flex-start;
	cursor: pointer;
}

.top-news-rank {
	font-size: 13.5px;
	font-weight: 800;
	width: 20px;
	color: #94a3b8;
	text-align: center;
	flex-shrink: 0;
}

.top-news-item:nth-child(1) .top-news-rank { color: #ef4444; }
.top-news-item:nth-child(2) .top-news-rank { color: #f97316; }
.top-news-item:nth-child(3) .top-news-rank { color: #eab308; }

.top-news-title {
	font-size: 13px;
	font-weight: 600;
	color: #334155;
	line-height: 1.35;
	margin: 0;
	display: -webkit-box;
	-webkit-line-clamp: 2;
	-webkit-box-orient: vertical;
	overflow: hidden;
}

.top-news-title:hover {
	color: var(--color-primary);
}

/* 실시간 핫 토픽 태그 */
.tag-cloud {
	display: flex;
	flex-wrap: wrap;
	gap: 7px;
}

.tag-btn {
	background: #f8fafc;
	border: 1px solid #e2e8f0;
	border-radius: 16px;
	padding: 5px 11px;
	font-size: 12px;
	font-weight: 500;
	color: #475569;
	text-decoration: none;
	cursor: pointer;
	transition: all 0.15s;
}

.tag-btn:hover {
	background-color: #eff6ff;
	border-color: #93c5fd;
	color: var(--color-primary);
}

/* 캘린더 일정 리스트 */
.calendar-list {
	list-style: none;
	padding: 0;
	margin: 0;
	display: flex;
	flex-direction: column;
	gap: 10px;
}

.calendar-item {
	display: flex;
	justify-content: space-between;
	align-items: center;
	font-size: 12.5px;
}

.calendar-date {
	font-weight: 700;
	color: var(--color-primary);
	background-color: #eff6ff;
	padding: 2px 6px;
	border-radius: 4px;
	font-size: 11px;
}

.calendar-desc {
	color: #334155;
	font-weight: 500;
	flex: 1;
	margin-left: 10px;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

/* 페이지네이션 */
.pagination-box {
	display: flex;
	justify-content: center;
	margin-top: 24px;
}

.pagination .page-link {
	color: #475569;
	border-color: #e2e8f0;
	font-size: 13px;
	padding: 6px 12px;
}

.pagination .page-item.active .page-link {
	background-color: var(--color-primary);
	border-color: var(--color-primary);
	color: #ffffff;
}

/* 모달 커스텀 */
.modal-content {
	border-radius: 14px;
	border: 1px solid var(--border-color);
}

.modal-header {
	border-bottom: 1px solid #f1f5f9;
	padding: 18px 24px;
}

.modal-body {
	padding: 24px;
}

.modal-article-title {
	font-size: 20px;
	font-weight: 800;
	color: #0f172a;
	line-height: 1.45;
	margin-bottom: 10px;
}

.modal-article-meta {
	font-size: 12px;
	color: var(--text-muted);
	padding-bottom: 14px;
	border-bottom: 1px solid #f1f5f9;
	margin-bottom: 18px;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.modal-article-content {
	font-size: 14px;
	line-height: 1.8;
	color: #334155;
}

.modal-article-content p {
	margin-bottom: 16px;
}

.notice-pill {
	background-color: #f1f5f9;
	color: #64748b;
	font-size: 11.5px;
	padding: 4px 10px;
	border-radius: 6px;
	display: inline-flex;
	align-items: center;
	gap: 5px;
}
</style>
</head>
<body>

	<!-- 상단 공통 네비게이션바 인클루드 -->
	<jsp:include page="../common/navbar.jsp"></jsp:include>

	<div class="news-container">
		<!-- 1. 페이지 헤더 타이틀 -->
		<div class="news-page-title-box">
			<div>
				<h1 class="news-page-title">
					<i class="bi bi-newspaper text-primary"></i> 실시간 시장 뉴스
				</h1>
				<p class="news-page-desc">국내외 주요 증시 시황, 상장기업 공시 및 거시경제 지표를 한눈에 파악하세요.</p>
			</div>
			<div>
				<span class="notice-pill">
					<i class="bi bi-broadcast text-danger"></i> 실시간 증시 피드 가동 중
				</span>
			</div>
		</div>

		<!-- 2. 실시간 속보 롤링 티커 배너 -->
		<div class="breaking-banner">
			<span class="breaking-badge"><i class="bi bi-lightning-fill"></i> 속보</span>
			<p class="breaking-text" id="breakingTicker">
				한국은행 금융통화위원회, 기준금리 3.50% 만장일치 동결... "물가 및 가계부채 안정 흐름 지속 관찰"
			</p>
			<span class="breaking-time">방금 전</span>
		</div>

		<!-- 3. 주요 시장 지표 퀵 바 -->
		<div class="market-ticker-row">
			<div class="market-ticker-card">
				<div>
					<div class="ticker-name">코스피 (KOSPI)</div>
					<div class="ticker-val">2,685.20</div>
				</div>
				<div class="ticker-change up">▲ +28.50 (+1.07%)</div>
			</div>
			<div class="market-ticker-card">
				<div>
					<div class="ticker-name">코스닥 (KOSDAQ)</div>
					<div class="ticker-val">872.15</div>
				</div>
				<div class="ticker-change up">▲ +6.80 (+0.79%)</div>
			</div>
			<div class="market-ticker-card">
				<div>
					<div class="ticker-name">원/달러 환율</div>
					<div class="ticker-val">1,332.50</div>
				</div>
				<div class="ticker-change down">▼ -4.20 (-0.31%)</div>
			</div>
			<div class="market-ticker-card">
				<div>
					<div class="ticker-name">나스닥 (NASDAQ)</div>
					<div class="ticker-val">16,832.40</div>
				</div>
				<div class="ticker-change up">▲ +134.80 (+0.81%)</div>
			</div>
		</div>

		<!-- 4. 카테고리 탭 및 검색 필터 -->
		<div class="filter-bar">
			<ul class="category-tabs" id="categoryTabs">
				<li><button type="button" class="category-btn active" data-category="all">전체</button></li>
				<li><button type="button" class="category-btn" data-category="market">증시·시황</button></li>
				<li><button type="button" class="category-btn" data-category="enterprise">기업·산업</button></li>
				<li><button type="button" class="category-btn" data-category="economy">거시경제</button></li>
				<li><button type="button" class="category-btn" data-category="global">글로벌</button></li>
				<li><button type="button" class="category-btn" data-category="crypto">가상자산</button></li>
			</ul>
			<div class="news-search-box">
				<input type="text" id="newsSearchInput" placeholder="종목명 또는 키워드 검색...">
				<i class="bi bi-search"></i>
			</div>
		</div>

		<!-- 5. 메인 본문 영역 (좌측 기사 목록 8 : 우측 사이드바 4) -->
		<div class="row">
			<!-- 좌측: 기사 피드 리스트 -->
			<div class="col-lg-8">
				<!-- 헤드라인 대표 뉴스 카드 -->
				<div class="featured-card" onclick="openNewsModal(0)" id="featuredCard">
					<div class="featured-img-box">
						<span class="featured-badge-top">오늘의 헤드라인</span>
						<div class="text-center px-4">
							<i class="bi bi-cpu" style="font-size: 54px; opacity: 0.9;"></i>
							<div class="mt-2 text-white-50" style="font-size: 13px;">AI 반도체 고대역폭 메모리(HBM) 생태계 확장 가속화</div>
						</div>
					</div>
					<div class="featured-content">
						<h2 class="featured-title">
							AI 반도체 훈풍 지속... 삼성전자·SK하이닉스 HBM 차세대 공급망 선점 경쟁 가열
						</h2>
						<p class="featured-desc">
							글로벌 빅테크의 차세대 AI 데이터센터 증설 경쟁으로 HBM3E 및 CXL 메모리 수요가 급증하는 가운데, 국내 반도체 양사가 5세대 고대역폭메모리 양산 체제를 본격 가동하며 연간 실적 턴어라운드 기대감이 높아지고 있다. 증권가에서는 목표주가를 일제히 상향 조정했다.
						</p>
						<div class="featured-meta">
							<div>
								<span class="stock-tag">삼성전자 005930</span>
								<span class="stock-tag">SK하이닉스 000660</span>
							</div>
							<div>
								<span>연합인포맥스</span> · <span>15분 전</span> · <span>조회 14,520</span>
							</div>
						</div>
					</div>
				</div>

				<!-- 일반 뉴스 목록 피드 -->
				<div class="news-list-group" id="newsListContainer">
					<!-- 뉴스 아이템 1 -->
					<div class="news-item-card" data-category="market" onclick="openNewsModal(1)">
						<div class="news-item-body">
							<div class="news-item-header">
								<span class="news-category-badge">증시·시황</span>
								<span class="news-item-source">한국경제 · 25분 전</span>
							</div>
							<h3 class="news-item-title">美 연준(Fed) 9월 빅컷 기대감 유효... 외국인 코스피 순매수 전환</h3>
							<p class="news-item-summary">
								미국 연방공개시장위원회(FOMC)를 앞두고 물가 지표가 둔화세를 보이면서 연준의 금리 인하 기대감이 증시를 견인하고 있다. 외국인은 장 초반 순매도에서 3,000억원대 순매수로 돌아서며 지수 상승을 주도했다.
							</p>
							<div class="news-item-footer">
								<div>
									<span class="stock-tag">KODEX 200</span>
									<span class="stock-tag">NAVER 035420</span>
								</div>
								<span>조회 8,432</span>
							</div>
						</div>
						<div class="news-thumb-box" style="background: #e0f2fe; color: #0284c7;">
							<i class="bi bi-graph-up-arrow"></i>
						</div>
					</div>

					<!-- 뉴스 아이템 2 -->
					<div class="news-item-card" data-category="enterprise" onclick="openNewsModal(2)">
						<div class="news-item-body">
							<div class="news-item-header">
								<span class="news-category-badge">기업·산업</span>
								<span class="news-item-source">매일경제 · 42분 전</span>
							</div>
							<h3 class="news-item-title">현대차·기아, 북미 친환경차 점유율 2위 굳히기... 조지아 공장 본격 가동</h3>
							<p class="news-item-summary">
								현대자동차그룹이 북미 시장에서 하이브리드 및 전기차 판매 호조를 이어가며 상반기 사상 최대 실적을 달성했다. 하반기 신규 메타플랜트 아메리카 가동에 따라 세제 혜택과 생산 효율이 극대화될 전망이다.
							</p>
							<div class="news-item-footer">
								<div>
									<span class="stock-tag">현대차 005380</span>
									<span class="stock-tag">기아 000270</span>
								</div>
								<span>조회 6,210</span>
							</div>
						</div>
						<div class="news-thumb-box" style="background: #fef3c7; color: #d97706;">
							<i class="bi bi-ev-front"></i>
						</div>
					</div>

					<!-- 뉴스 아이템 3 -->
					<div class="news-item-card" data-category="enterprise" onclick="openNewsModal(3)">
						<div class="news-item-body">
							<div class="news-item-header">
								<span class="news-category-badge">기업·산업</span>
								<span class="news-item-source">이데일리 · 1시간 전</span>
							</div>
							<h3 class="news-item-title">2차전지 반등 신호탄? 리튬 가격 바닥 통과 기대에 양극재주 일제히 강세</h3>
							<p class="news-item-summary">
								탄산리튬 선물 가격이 저점을 확인하고 완만한 반등세를 보이자 2차전지 소재 및 셀 기업들의 주가가 동반 강세를 기록 중이다. 증권가는 재고평가손실 축소로 3분기 턴어라운드를 조심스럽게 점치고 있다.
							</p>
							<div class="news-item-footer">
								<div>
									<span class="stock-tag">LG에너지솔루션 373220</span>
									<span class="stock-tag">에코프로비엠 247540</span>
								</div>
								<span>조회 9,120</span>
							</div>
						</div>
						<div class="news-thumb-box" style="background: #dcfce7; color: #16a34a;">
							<i class="bi bi-battery-charging"></i>
						</div>
					</div>

					<!-- 뉴스 아이템 4 -->
					<div class="news-item-card" data-category="economy" onclick="openNewsModal(4)">
						<div class="news-item-body">
							<div class="news-item-header">
								<span class="news-category-badge">거시경제</span>
								<span class="news-item-source">머니투데이 · 2시간 전</span>
							</div>
							<h3 class="news-item-title">정부, '기업 밸류업 프로그램' 세제 개편 가속... 주주환원 우수기업 인센티브</h3>
							<p class="news-item-summary">
								금융위원회와 기획재정부는 코리아 디스카운트 해소를 위한 밸류업 가이드라인 세부 세제지원책을 발표했다. 자사주 소각 및 배당 확대 기업에 대한 법인세 세액공제와 주주 배당소득 분리과세가 중점 추진된다.
							</p>
							<div class="news-item-footer">
								<div>
									<span class="stock-tag">KB금융 105560</span>
									<span class="stock-tag">신한지주 055550</span>
								</div>
								<span>조회 5,830</span>
							</div>
						</div>
						<div class="news-thumb-box" style="background: #f3e8ff; color: #9333ea;">
							<i class="bi bi-bank"></i>
						</div>
					</div>

					<!-- 뉴스 아이템 5 -->
					<div class="news-item-card" data-category="global" onclick="openNewsModal(5)">
						<div class="news-item-body">
							<div class="news-item-header">
								<span class="news-category-badge">글로벌</span>
								<span class="news-item-source">연합뉴스 · 3시간 전</span>
							</div>
							<h3 class="news-item-title">뉴욕증시, 기술주 랠리 속 혼조세 마감... 엔비디아·애플 신고가 행진</h3>
							<p class="news-item-summary">
								뉴욕증시는 주요 경제지표 발표를 앞두고 관망세가 짙은 가운데 빅테크 기업을 중심으로 상승폭을 키웠다. 엔비디아는 AI 칩 수요 지속 확인 속에 3%대 반등했고, 애플 역시 인텔리전스 공개 이후 긍정적 평가가 이어졌다.
							</p>
							<div class="news-item-footer">
								<div>
									<span class="stock-tag">NVDA</span>
									<span class="stock-tag">AAPL</span>
								</div>
								<span>조회 11,400</span>
							</div>
						</div>
						<div class="news-thumb-box" style="background: #fee2e2; color: #dc2626;">
							<i class="bi bi-globe2"></i>
						</div>
					</div>

					<!-- 뉴스 아이템 6 -->
					<div class="news-item-card" data-category="crypto" onclick="openNewsModal(6)">
						<div class="news-item-body">
							<div class="news-item-header">
								<span class="news-category-badge">가상자산</span>
								<span class="news-item-source">코인데스크코리아 · 4시간 전</span>
							</div>
							<h3 class="news-item-title">비트코인, 현물 ETF 순유입 재개에 6만 4천달러선 안착 시도</h3>
							<p class="news-item-summary">
								글로벌 자산운용사들의 비트코인 현물 ETF로 다시 기관 순유입이 지속되면서 가상자산 시장 전반의 투자심리가 회복세를 보이고 있다. 이더리움 및 주요 알트코인 역시 동반 상승세를 나타냈다.
							</p>
							<div class="news-item-footer">
								<div>
									<span class="stock-tag">BTC</span>
									<span class="stock-tag">ETH</span>
								</div>
								<span>조회 7,310</span>
							</div>
						</div>
						<div class="news-thumb-box" style="background: #ffedd5; color: #ea580c;">
							<i class="bi bi-currency-bitcoin"></i>
						</div>
					</div>

					<!-- 뉴스 아이템 7 -->
					<div class="news-item-card" data-category="enterprise" onclick="openNewsModal(7)">
						<div class="news-item-body">
							<div class="news-item-header">
								<span class="news-category-badge">기업·산업</span>
								<span class="news-item-source">아시아경제 · 5시간 전</span>
							</div>
							<h3 class="news-item-title">K-조선, 고부가가치 LNG선 추가 수주 릴레이... 3년 치 일감 조기 확보</h3>
							<p class="news-item-summary">
								국내 조선 3사가 중동 및 유럽 선사로부터 잇따라 고부가가치 LNG 운반선과 암모니아 추진선을 수주하며 선별 수주 전략의 성과를 거두고 있다. 선가 상승에 따른 수익성 개선이 가시화되고 있다.
							</p>
							<div class="news-item-footer">
								<div>
									<span class="stock-tag">HD한국조선해양 009540</span>
									<span class="stock-tag">삼성중공업 010140</span>
								</div>
								<span>조회 4,910</span>
							</div>
						</div>
						<div class="news-thumb-box" style="background: #e0e7ff; color: #4338ca;">
							<i class="bi bi-tsunami"></i>
						</div>
					</div>
				</div>

				<!-- 페이지네이션 (Dummy UI) -->
				<div class="pagination-box">
					<nav aria-label="News Page Navigation">
						<ul class="pagination">
							<li class="page-item disabled"><a class="page-link" href="#" tabindex="-1">이전</a></li>
							<li class="page-item active"><a class="page-link" href="#">1</a></li>
							<li class="page-item"><a class="page-link" href="#">2</a></li>
							<li class="page-item"><a class="page-link" href="#">3</a></li>
							<li class="page-item"><a class="page-link" href="#">4</a></li>
							<li class="page-item"><a class="page-link" href="#">5</a></li>
							<li class="page-item"><a class="page-link" href="#">다음</a></li>
						</ul>
					</nav>
				</div>
			</div>

			<!-- 우측: 사이드바 위젯 영역 -->
			<div class="col-lg-4">
				<!-- 사이드바 1: 실시간 가장 많이 본 뉴스 -->
				<div class="sidebar-card">
					<h3 class="sidebar-title">
						<i class="bi bi-fire text-danger"></i> 실시간 많이 본 뉴스
					</h3>
					<ul class="top-news-list">
						<li class="top-news-item" onclick="openNewsModal(0)">
							<span class="top-news-rank">1</span>
							<p class="top-news-title">AI 반도체 훈풍 지속... 삼성전자·SK하이닉스 HBM 공급망 선점 경쟁 가열</p>
						</li>
						<li class="top-news-item" onclick="openNewsModal(5)">
							<span class="top-news-rank">2</span>
							<p class="top-news-title">뉴욕증시, 기술주 랠리 속 혼조세 마감... 엔비디아·애플 신고가 행진</p>
						</li>
						<li class="top-news-item" onclick="openNewsModal(3)">
							<span class="top-news-rank">3</span>
							<p class="top-news-title">2차전지 반등 신호탄? 리튬 가격 바닥 통과 기대에 양극재주 일제히 강세</p>
						</li>
						<li class="top-news-item" onclick="openNewsModal(1)">
							<span class="top-news-rank">4</span>
							<p class="top-news-title">美 연준 9월 빅컷 기대감 유효... 외국인 코스피 순매수 전환</p>
						</li>
						<li class="top-news-item" onclick="openNewsModal(2)">
							<span class="top-news-rank">5</span>
							<p class="top-news-title">현대차·기아, 북미 친환경차 점유율 2위 굳히기 성공</p>
						</li>
					</ul>
				</div>

				<!-- 사이드바 2: 실시간 인기 토픽 태그 -->
				<div class="sidebar-card">
					<h3 class="sidebar-title">
						<i class="bi bi-hash text-primary"></i> 주요 증시 키워드
					</h3>
					<div class="tag-cloud">
						<button type="button" class="tag-btn" onclick="filterByTag('반도체')">#반도체</button>
						<button type="button" class="tag-btn" onclick="filterByTag('HBM')">#HBM</button>
						<button type="button" class="tag-btn" onclick="filterByTag('금리인하')">#금리인하</button>
						<button type="button" class="tag-btn" onclick="filterByTag('2차전지')">#2차전지</button>
						<button type="button" class="tag-btn" onclick="filterByTag('밸류업')">#기업밸류업</button>
						<button type="button" class="tag-btn" onclick="filterByTag('환율')">#원달러환율</button>
						<button type="button" class="tag-btn" onclick="filterByTag('현대차')">#현대차</button>
						<button type="button" class="tag-btn" onclick="filterByTag('비트코인')">#비트코인</button>
					</div>
				</div>

				<!-- 사이드바 3: 이번 주 주요 증시 캘린더 -->
				<div class="sidebar-card">
					<h3 class="sidebar-title">
						<i class="bi bi-calendar-check text-success"></i> 이번 주 주요 경제 일정
					</h3>
					<ul class="calendar-list">
						<li class="calendar-item">
							<span class="calendar-date">09.09 화</span>
							<span class="calendar-desc">미국 8월 소비자물가지수(CPI) 발표</span>
						</li>
						<li class="calendar-item">
							<span class="calendar-date">09.11 목</span>
							<span class="calendar-desc">한국 주가지수·선물옵션 동시 만기일</span>
						</li>
						<li class="calendar-item">
							<span class="calendar-date">09.12 금</span>
							<span class="calendar-desc">유럽중앙은행(ECB) 기준금리 통화정책회의</span>
						</li>
						<li class="calendar-item">
							<span class="calendar-date">09.17 수</span>
							<span class="calendar-desc">미국 FOMC 기준금리 결정 및 파월 기자회견</span>
						</li>
					</ul>
				</div>

				<!-- 사이드바 4: Frade 트레이딩 바로가기 배너 -->
				<div class="sidebar-card" style="background: linear-gradient(135deg, #1e293b 0%, #0f172a 100%); color: #fff;">
					<h4 style="font-size: 15px; font-weight: 700; margin: 0 0 6px;">Frade 모의투자 트레이딩</h4>
					<p style="font-size: 12px; color: #94a3b8; margin-bottom: 14px; line-height: 1.5;">
						실시간 시세 차트와 맞춤 주문 기능으로 성공적인 투자 감각을 익혀보세요.
					</p>
					<a href="${pageContext.request.contextPath}/stock" class="btn btn-sm btn-primary w-100" style="font-weight: 600; border-radius: 6px;">
						트레이딩 바로가기 <i class="bi bi-arrow-right"></i>
					</a>
				</div>
			</div>
		</div>
	</div>

	<!-- 뉴스 상세 모달 (기사 클릭 시 팝업으로 상세 내용 표시) -->
	<div class="modal fade" id="newsDetailModal" tabindex="-1" aria-labelledby="newsDetailModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<div>
						<span class="badge bg-primary mb-1" id="modalCategory">주요뉴스</span>
						<span class="text-muted small ms-2" id="modalSource">한국경제</span>
					</div>
					<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
				</div>
				<div class="modal-body">
					<h3 class="modal-article-title" id="modalTitle">기사 제목이 표시됩니다.</h3>
					<div class="modal-article-meta">
						<span id="modalDate">2026.09.06 18:00</span>
						<div>
							<span class="me-3" id="modalViews"><i class="bi bi-eye"></i> 조회 12,345</span>
							<span class="text-primary" id="modalStocks"></span>
						</div>
					</div>
					<div class="modal-article-content" id="modalContent">
						기사 본문이 로드됩니다.
					</div>
				</div>
				<div class="modal-footer justify-content-between">
					<div class="d-flex gap-2">
						<button type="button" class="btn btn-outline-secondary btn-sm" onclick="alert('기사를 북마크에 저장했습니다.')">
							<i class="bi bi-bookmark"></i> 스크랩
						</button>
						<button type="button" class="btn btn-outline-secondary btn-sm" onclick="copyArticleUrl()">
							<i class="bi bi-share"></i> 공유
						</button>
					</div>
					<button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">닫기</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 부트스트랩 JS 번들 -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
		crossorigin="anonymous"></script>

	<!-- 가짜 뉴스 데이터 및 인터랙션 스크립트 -->
	<script>
		// 기사 상세 더미 데이터 세트
		const dummyArticles = [
			{
				title: "AI 반도체 훈풍 지속... 삼성전자·SK하이닉스 HBM 차세대 공급망 선점 경쟁 가열",
				category: "주요뉴스",
				source: "연합인포맥스 | 금융증권부",
				date: "2026.09.06 17:45",
				views: "조회 14,520",
				stocks: "관련종목: 삼성전자(005930), SK하이닉스(000660)",
				content: `
					<p><strong>[서울=연합인포맥스]</strong> 글로벌 빅테크 기업들의 생성형 AI(인공지능) 인프라 구축 경쟁이 가속화되면서 핵심 부품인 고대역폭 메모리(HBM) 시장의 주도권을 잡기 위한 국내 반도체 기업들의 공급망 선점 경쟁이 한층 격화되고 있다.</p>
					<p>업계에 따르면 삼성전자와 SK하이닉스는 차세대 AI 가속기에 탑재될 5세대 고대역폭 메모리(HBM3E) 12단 제품의 양산 및 고객사 퀄(품질) 테스트 통과를 연이어 마무리하며 하반기 출하량을 전년 동기 대비 3배 이상 확대할 계획이다.</p>
					<p>증권가 한 연구원은 "단순한 메모리 단가 상승을 넘어 고부가 맞춤형 솔루션으로 패러다임이 전환되고 있다"며 "실적 개선 사이클이 내년 상반기까지 견고하게 지속될 가능성이 높다"고 분석했다. 이에 따라 외국인과 기관 투자자들의 동반 순매수가 이어지며 지수 방어의 핵심 축 역할을 하고 있다.</p>
				`
			},
			{
				title: "美 연준(Fed) 9월 빅컷 기대감 유효... 외국인 코스피 순매수 전환",
				category: "증시·시황",
				source: "한국경제 | 마켓인사이트",
				date: "2026.09.06 17:35",
				views: "조회 8,432",
				stocks: "관련종목: KODEX 200, NAVER(035420)",
				content: `
					<p><strong>[한국경제]</strong> 미국의 인플레이션 지표가 시장 예상치를 밑돌며 안정권에 진입함에 따라 연방준비제도(Fed)의 9월 기준금리 인하 폭에 대한 기대감이 재차 확산되고 있다.</p>
					<p>이날 국내 유가증권시장에서 코스피는 외국인의 순매수세 전환에 힘입어 전일 대비 1% 이상 상승하며 2,680선을 회복했다. 특히 대형 기술주와 금융지주사를 중심으로 매수세가 집중되었다.</p>
					<p>전문가들은 "통화 긴축 완화 기조는 신흥국 증시로의 글로벌 유동성 재유입을 촉진할 긍정적 요인"이라며 "단기 변동성보다는 금리 인하 수혜가 예상되는 소프트웨어, 바이오, 고배당 섹터에 주목할 필요가 있다"고 덧붙였다.</p>
				`
			},
			{
				title: "현대차·기아, 북미 친환경차 점유율 2위 굳히기... 조지아 공장 본격 가동",
				category: "기업·산업",
				source: "매일경제 | 산업부",
				date: "2026.09.06 17:18",
				views: "조회 6,210",
				stocks: "관련종목: 현대차(005380), 기아(000270)",
				content: `
					<p><strong>[매일경제]</strong> 현대자동차그룹이 미국 전기차 및 하이브리드 시장에서 두 자릿수 점유율을 견고하게 수성하며 포드와 GM을 제치고 친환경차 부문 2위 굳히기에 성공했다.</p>
					<p>미국 조지아주에 건설된 '현대차그룹 메타플랜트 아메리카(HMGMA)'가 본격적인 시험 가동에 돌입하면서 연방 정부의 인플레이션 감축법(IRA) 보조금 수혜 요건도 점진적으로 충족될 전망이다.</p>
					<p>현대차 관계자는 "소비자들의 선호도가 높은 하이브리드 라인업의 신속한 현지 공급과 더불어 신형 전기차 모델 출시를 통해 북미 내 입지를 더욱 공고히 할 것"이라고 전했다.</p>
				`
			},
			{
				title: "2차전지 반등 신호탄? 리튬 가격 바닥 통과 기대에 양극재주 일제히 강세",
				category: "기업·산업",
				source: "이데일리 | 증권부",
				date: "2026.09.06 17:00",
				views: "조회 9,120",
				stocks: "관련종목: LG에너지솔루션(373220), 에코프로비엠(247540)",
				content: `
					<p><strong>[이데일리]</strong> 장기간 하락세를 지속하던 탄산리튬 선물 가격이 톤당 일정 수준을 지지하며 저점을 다지는 신호가 포착되자 국내 2차전지 밸류체인 전반에 저가 매수세가 강하게 유입되었다.</p>
					<p>그동안 주요 배터리 셀 및 소재 업체들의 실적 발목을 잡았던 고가 원자재 재고평가손실이 상당 부분 해소되고, 4분기부터 점진적인 가동률 회복이 예상된다는 증권가 리포트가 호재로 작용했다.</p>
					<p>시장 관계자는 "전방 전기차 캐즘(일시적 수요 둔화) 우려가 주가에 충분히 선반영된 만큼, 에너지저장장치(ESS)용 배터리 수주 확대와 차세대 LFP 배터리 양산 성과가 주가 반등의 키가 될 것"이라고 진단했다.</p>
				`
			},
			{
				title: "정부, '기업 밸류업 프로그램' 세제 개편 가속... 주주환원 우수기업 인센티브",
				category: "거시경제",
				source: "머니투데이 | 경제정책부",
				date: "2026.09.06 16:00",
				views: "조회 5,830",
				stocks: "관련종목: KB금융(105560), 신한지주(055550)",
				content: `
					<p><strong>[머니투데이]</strong> 정부가 자본시장 선진화와 코리아 디스카운트 해소를 위해 추진 중인 '기업 밸류업 프로그램'의 구체적인 세제 혜택 패키지가 공개되었다.</p>
					<p>주주환원 확대 기업에 대해서는 전년 대비 증가한 배당 및 자사주 소각 금액에 대해 법인세 5% 세액공제를 적용하고, 해당 기업 주주에게 지급되는 배당소득에 대해서는 원천징수 세율 인하 및 금융소득 종합과세 분리과세 혜택이 부여된다.</p>
					<p>이에 따라 고배당을 실시해온 대형 금융지주사와 지주회사들의 주가가 강세를 보였으며, 외국인 투자자들의 중장기 배당 펀드 자금 유입이 가속화될 것으로 기대를 모으고 있다.</p>
				`
			},
			{
				title: "뉴욕증시, 기술주 랠리 속 혼조세 마감... 엔비디아·애플 신고가 행진",
				category: "글로벌",
				source: "연합뉴스 | 뉴욕특파원",
				date: "2026.09.06 15:00",
				views: "조회 11,400",
				stocks: "관련종목: NVDA, AAPL, MSFT",
				content: `
					<p><strong>[뉴욕=연합뉴스]</strong> 뉴욕증시 3대 지수가 거시경제 지표 경계감과 빅테크 실적 기대감 속에서 혼조세를 기록했다.</p>
					<p>엔비디아는 신규 블랙웰 아키텍처 기반 AI 칩 생산이 정상 궤도에 올랐다는 경영진의 코멘트가 전해지며 3% 이상 반등했고, 애플 또한 인공지능 기능이 탑재된 신제품 출시에 대한 기대감으로 시가총액 1위 자리를 굳건히 지켰다.</p>
					<p>월가 전문가들은 "단기 고점 부담에 따른 차익 실현 매물이 출회될 수 있으나, 빅테크의 독점적 시장 지배력과 현금 창출 능력을 고려할 때 강세장 기조는 여전히 유효하다"고 평가했다.</p>
				`
			},
			{
				title: "비트코인, 현물 ETF 순유입 재개에 6만 4천달러선 안착 시도",
				category: "가상자산",
				source: "코인데스크코리아 | 크립토마켓",
				date: "2026.09.06 14:00",
				views: "조회 7,310",
				stocks: "관련종목: BTC, ETH, SOL",
				content: `
					<p><strong>[코인데스크코리아]</strong> 미국 내 비트코인 현물 ETF로 다시 수억 달러 규모의 기관 순유입이 관측되면서 비트코인 가격이 6만 4,000달러 선을 시험하고 있다.</p>
					<p>온체인 분석 데이터에 따르면 장기 보유자들의 매도세가 둔화되고 고래 투자자들의 매집 정황이 늘어나면서 공급 부족에 따른 가격 상승 압력이 강화되는 모습이다.</p>
					<p>시장 전문가들은 "글로벌 유동성 공급 재개 및 주요국 금리 인하 사이클 도래가 가상자산 시장 전반의 모멘텀을 회복시키는 핵심 촉매제가 될 것"이라고 내다봤다.</p>
				`
			},
			{
				title: "K-조선, 고부가가치 LNG선 추가 수주 릴레이... 3년 치 일감 조기 확보",
				category: "기업·산업",
				source: "아시아경제 | 산업2부",
				date: "2026.09.06 13:00",
				views: "조회 4,910",
				stocks: "관련종목: HD한국조선해양(009540), 삼성중공업(010140)",
				content: `
					<p><strong>[아시아경제]</strong> 국내 조선사들이 유럽 선주로부터 척당 최고가를 경신하는 초대형 LNG 운반선 수주 계약을 잇따라 체결하며 선별 수주 전략의 결실을 맺고 있다.</p>
					<p>선박 건조 슬롯이 2028년까지 대부분 채워짐에 따라 가격 협상력이 더욱 높아졌으며, 친환경 고효율 추진선 기술력을 앞세워 글로벌 시장에서의 독보적 입지를 재확인했다.</p>
					<p>증권가 역시 조선 3사의 하반기 흑자 폭 확대를 예상하며 투자의견 '매수'를 일제히 유지했다.</p>
				`
			}
		];

		// 속보 티커 문구 순환
		const tickerNews = [
			"한국은행 금융통화위원회, 기준금리 3.50% 만장일치 동결... \"물가 및 가계부채 안정 흐름 지속 관찰\"",
			"美 8월 비농업 고용지표 발표 앞두고 뉴욕증시 기술주 중심 선반영 상승세",
			"기재부, 배당소득 분리과세 및 자사주 소각 세액공제 포함 밸류업 세제지원안 최종 확정",
			"엔비디아, 신형 블랙웰 AI 가속기 4분기 정상 출하 확인... 글로벌 반도체 공급망 안도"
		];
		let tickerIdx = 0;
		setInterval(() => {
			tickerIdx = (tickerIdx + 1) % tickerNews.length;
			const el = document.getElementById('breakingTicker');
			if (el) {
				el.style.opacity = 0;
				setTimeout(() => {
					el.innerText = tickerNews[tickerIdx];
					el.style.opacity = 1;
				}, 200);
			}
		}, 5000);

		// 뉴스 모달 열기
		function openNewsModal(index) {
			const article = dummyArticles[index];
			if (!article) return;

			document.getElementById('modalCategory').innerText = article.category;
			document.getElementById('modalSource').innerText = article.source;
			document.getElementById('modalTitle').innerText = article.title;
			document.getElementById('modalDate').innerText = article.date;
			document.getElementById('modalViews').innerHTML = '<i class="bi bi-eye"></i> ' + article.views;
			document.getElementById('modalStocks').innerText = article.stocks;
			document.getElementById('modalContent').innerHTML = article.content;

			const modal = new bootstrap.Modal(document.getElementById('newsDetailModal'));
			modal.show();
		}

		// URL 공유 복사 (가짜 알림)
		function copyArticleUrl() {
			navigator.clipboard.writeText(window.location.href).then(() => {
				alert('기사 주소가 클립보드에 복사되었습니다.');
			}).catch(() => {
				alert('기사 링크: ' + window.location.href);
			});
		}

		// 카테고리 탭 클릭 필터링
		document.querySelectorAll('.category-btn').forEach(btn => {
			btn.addEventListener('click', function() {
				document.querySelectorAll('.category-btn').forEach(b => b.classList.remove('active'));
				this.classList.add('active');

				const selectedCategory = this.getAttribute('data-category');
				filterNews(selectedCategory, document.getElementById('newsSearchInput').value.trim());
			});
		});

		// 검색 입력 필터링
		document.getElementById('newsSearchInput').addEventListener('input', function() {
			const activeBtn = document.querySelector('.category-btn.active');
			const selectedCategory = activeBtn ? activeBtn.getAttribute('data-category') : 'all';
			filterNews(selectedCategory, this.value.trim());
		});

		// 태그 클릭 시 검색
		function filterByTag(tagName) {
			document.getElementById('newsSearchInput').value = tagName;
			document.querySelectorAll('.category-btn').forEach(b => b.classList.remove('active'));
			document.querySelector('.category-btn[data-category="all"]').classList.add('active');
			filterNews('all', tagName);
		}

		// 공통 뉴스 필터링 함수
		function filterNews(category, keyword) {
			const cards = document.querySelectorAll('.news-item-card');
			const featured = document.getElementById('featuredCard');

			// 헤드라인 카드 처리
			if (category === 'all' || category === 'enterprise' || category === 'market') {
				if (!keyword || featured.innerText.toLowerCase().includes(keyword.toLowerCase())) {
					featured.style.display = 'block';
				} else {
					featured.style.display = 'none';
				}
			} else {
				featured.style.display = 'none';
			}

			// 목록 카드 처리
			cards.forEach(card => {
				const cardCat = card.getAttribute('data-category');
				const text = card.innerText.toLowerCase();
				const matchCategory = (category === 'all' || cardCat === category);
				const matchKeyword = (!keyword || text.includes(keyword.toLowerCase()));

				if (matchCategory && matchKeyword) {
					card.style.display = 'flex';
				} else {
					card.style.display = 'none';
				}
			});
		}
	</script>
</body>
</html>
