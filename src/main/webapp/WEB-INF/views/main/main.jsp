<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!-- 스마트폰 등의 환경에서 원래 크기로 보이도록 -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Frade - 메인 페이지</title>
<!-- 부트스트랩 CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
	crossorigin="anonymous">
<!-- 메인 대시보드 커스텀 CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
</head>
<body>
	<!-- 네비게이션바 include -->
	<jsp:include page="../common/navbar.jsp"></jsp:include>
	<div class="main-container">
		<!-- 상단 타이틀 영역 -->
		<div class="main-page-title-box">
			<div>
				<h1 class="main-page-title">📊 오늘의 시장</h1>
				<p class="main-page-desc">실시간 인기 주식과 커뮤니티 주요 토픽을 한눈에 확인하세요.</p>
				
				</div>
		</div>
		<!-- 상단 박스: [좌] 실시간 인기 종목 상위 5개 / [우] 주식뉴스 -->
		<div class="main-top-box">
			<!-- 1. 실시간 인기 종목 (StockController 연결) -->
			<div class="dashboard-card">
				<div class="card-header-row">
					<h2 class="card-title">🔥 실시간 인기 종목</h2>
					<a href="${pageContext.request.contextPath}/stock" class="card-more-link">더보기 &gt;</a>
				</div>
				<div class="stock-list">
					<c:forEach var="stock" items="${topStocks}" varStatus="status">
						<a href="${pageContext.request.contextPath}/stock/${stock.stockCode}" class="stock-item">
							<div class="stock-left">
								<span class="rank-badge">${status.count}</span>
								<div class="stock-name-box">
									<span class="stock-name">${stock.stockName}</span>
									<span class="stock-code-sector">${stock.stockCode} · ${stock.sectorName}</span>
									
									</div>
							</div>
							<div class="stock-right">
								<span class="stock-price">
									<fmt:formatNumber value="${stock.price}" pattern="#,##0" />원
								</span>
								<c:choose>
									<c:when test="${stock.dailyPriceChange > 0}">
										<span class="stock-rate up">▲ +${stock.dailyPriceChangeRoundedPercent}%</span>
									</c:when>
									<c:when test="${stock.dailyPriceChange < 0}">
										<span class="stock-rate down">▼ ${stock.dailyPriceChangeRoundedPercent}%</span>
									</c:when>
									<c:otherwise>
										<span class="stock-rate even">0.00%</span>
									</c:otherwise>
								</c:choose>
							</div>
						</a>
					</c:forEach>
				</div>
			</div>
			
			<!-- 2. 주식뉴스 (컨트롤러 미연동: 제외 및 안내 처리) -->
			<div class="dashboard-card">
				<div class="card-header-row">
					<h2 class="card-title">📰 주식뉴스</h2>
					<span class="card-more-link" style="color: #94a3b8; cursor: default;">준비 중</span>
				</div>
				<div class="placeholder-section">
					<span class="badge-preparing">뉴스 컨트롤러 준비 중</span>
					<p class="placeholder-text">실시간 주요 증시 및 기업 뉴스 피드 연동 예정입니다.</p>
					<ul class="placeholder-list">
						<li>· 반도체 대형주 중심 외국인 프로그램 순매수 지속</li>
						<li>· 하반기 글로벌 금리 인하 기대감에 증시 강세</li>
						<li>· 글로벌 친환경차 누적 판매 500만대 조기 돌파</li>
					</ul>
				</div>
			</div>
		</div>
		<!-- 하단 박스: [좌] kospi / [우] 커뮤니티 인기글 상위 5개 -->
		<div class="main-bottom-box">
			<!-- 3. KOSPI 지수 (컨트롤러 미연동: 제외 및 안내 처리) -->
			<div class="dashboard-card">
				<div class="card-header-row">
					<h2 class="card-title">📈 KOSPI 종합지수</h2>
					<span class="card-more-link" style="color: #94a3b8; cursor: default;">준비 중</span>
					
					</div>
				<div class="placeholder-section">
					<span class="badge-preparing">지수 컨트롤러 준비 중</span>
					<p class="placeholder-text">한국거래소(KRX) 실시간 코스피 시세 API 연동 예정입니다.</p>
					<div class="kospi-preview-box">
						<div class="kospi-val">2,685.20</div>
						<div class="kospi-change up">▲ +28.50 (+1.07%)</div>
					</div>
				</div>
			</div>
			<!-- 4. 커뮤니티 인기글 (CommunityController 연결) -->
			<div class="dashboard-card">
				<div class="card-header-row">
					<h2 class="card-title">💬 커뮤니티 인기글</h2>
					<a href="${pageContext.request.contextPath}/community-lists" class="card-more-link">더보기 &gt;</a>
				</div>
				<div class="post-list">
					<c:forEach var="post" items="${topPosts}" varStatus="status">
						<a href="${pageContext.request.contextPath}/community-lists/detail?postNum=${post.postNum}" class="post-item">
							<div class="post-left">
								<span class="post-rank-badge">${status.count}</span>
								<div class="post-info-box">
								
								<span class="post-title" title="${post.postTitle}">${post.postTitle}</span>
									<span class="post-meta-sub">${post.userName} · ${post.postedDateString}</span>
								</div>
							</div>
							<div class="post-right">
								<span>조회 <fmt:formatNumber value="${post.postViewCnt}" pattern="#,##0" /></span>
								<span class="post-like-tag">추천 <fmt:formatNumber value="${post.postLikeCnt}" pattern="#,##0" /></span>
							</div>
						</a>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
