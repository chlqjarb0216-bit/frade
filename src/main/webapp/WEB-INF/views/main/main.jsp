<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Frade</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/main.css?v=2.0">
</head>
<body>
	<jsp:include page="../common/navbar.jsp"></jsp:include>
	<div class="main-container">
		<div class="main-page-title-box">
			<div>
				<h1 class="main-page-title">오늘의 시장</h1>
				<p class="main-page-desc">실시간 인기 주식과 커뮤니티 주요 토픽을 한눈에 확인하세요.</p>
			</div>
		</div>

		<div class="main-section-row" style="margin-bottom: 24px;">
			<div class="dashboard-card">
				<div class="card-header-row">
					<h2 class="card-title">🔥 실시간 인기 종목 TOP 10</h2>
					<a href="${pageContext.request.contextPath}/stock"
						class="card-more-link">더보기</a>
				</div>
				<div class="stock-top10-grid">
					<div class="stock-sub-col">
						<c:forEach var="stock" items="${topStocks}" varStatus="status"
							begin="0" end="4">
							<a
								href="${pageContext.request.contextPath}/stock/${stock.stockCode}"
								class="stock-item">
								<div class="stock-left">
									<span class="rank-badge">${status.index + 1}</span>
									<div class="stock-name-box">
										<span class="stock-name">${stock.stockName}</span> <span
											class="stock-code-sector">${stock.stockCode} ·
											${stock.sectorName}</span>
									</div>
								</div>
								<div class="stock-right">
									<span class="stock-price"> <fmt:formatNumber
											value="${stock.price}" pattern="#,##0" />원
									</span>
									<c:choose>
										<c:when test="${stock.dailyPriceChange > 0}">
											<span class="stock-rate up">▲
												${Math.abs(stock.dailyPriceChangeRoundedPercent)}%</span>
										</c:when>
										<c:when test="${stock.dailyPriceChange < 0}">
											<span class="stock-rate down">▼
												${Math.abs(stock.dailyPriceChangeRoundedPercent)}%</span>
										</c:when>
										<c:otherwise>
											<span class="stock-rate even">0.00%</span>
										</c:otherwise>
									</c:choose>
								</div>
							</a>
						</c:forEach>
					</div>

					<div class="stock-sub-col">
						<c:forEach var="stock" items="${topStocks}" varStatus="status"
							begin="5" end="9">
							<a
								href="${pageContext.request.contextPath}/stock/${stock.stockCode}"
								class="stock-item">
								<div class="stock-left">
									<span class="rank-badge">${status.index + 1}</span>
									<div class="stock-name-box">
										<span class="stock-name">${stock.stockName}</span> <span
											class="stock-code-sector">${stock.stockCode} ·
											${stock.sectorName}</span>
									</div>
								</div>
								<div class="stock-right">
									<span class="stock-price"> <fmt:formatNumber
											value="${stock.price}" pattern="#,##0" />원
									</span>
									<c:choose>
										<c:when test="${stock.dailyPriceChange > 0}">
											<span class="stock-rate up">▲
												${Math.abs(stock.dailyPriceChangeRoundedPercent)}%</span>
										</c:when>
										<c:when test="${stock.dailyPriceChange < 0}">
											<span class="stock-rate down">▼
												${Math.abs(stock.dailyPriceChangeRoundedPercent)}%</span>
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
			</div>
		</div>

		<div class="main-section-row" style="margin-bottom: 24px;">
			<div class="dashboard-card">
				<div class="card-header-row">
					<h2 class="card-title">KOSPI 종합지수</h2>
				</div>
				<div class="placeholder-section">
					
					<div
						style="width: 1050px; margin: 0 auto; background: #161a1e; padding: 20px; border-radius: 8px;">
						<div id="stock-chart"></div>
					</div>
				</div>
			</div>
		</div>

		<div class="main-bottom-box">
			<div class="dashboard-card">
				<div class="card-header-row">
					<h2 class="card-title">📰 주식뉴스 TOP 5</h2>
					<a href="${pageContext.request.contextPath}/news"
						class="card-more-link">더보기</a>
				</div>
				<div class="post-list">
					<a href="${pageContext.request.contextPath}/news" class="post-item">
						<div class="post-left">
							<span class="post-rank-badge" style="color: #ef4444;">1</span>
							<div class="post-info-box">
								<span class="post-title"
									title="AI 반도체 훈풍 지속... 삼성전자·SK하이닉스 HBM 공급망 선점 경쟁 가열">AI
									반도체 훈풍 지속... 삼성전자·SK하이닉스 HBM 공급망 선점 경쟁 가열</span> <span
									class="post-meta-sub">연합인포맥스 · 15분 전</span>
							</div>
						</div>
						<div class="post-right">
							<span
								style="background-color: #eff6ff; color: #2563eb; font-size: 11px; font-weight: 600; padding: 2px 8px; border-radius: 4px;">반도체</span>
						</div>
					</a> <a href="${pageContext.request.contextPath}/news"
						class="post-item">
						<div class="post-left">
							<span class="post-rank-badge" style="color: #f97316;">2</span>
							<div class="post-info-box">
								<span class="post-title"
									title="美 연준(Fed) 9월 빅컷 기대감 유효... 외국인 코스피 순매수 전환">美
									연준(Fed) 9월 빅컷 기대감 유효... 외국인 코스피 순매수 전환</span> <span
									class="post-meta-sub">한국경제 · 25분 전</span>
							</div>
						</div>
						<div class="post-right">
							<span
								style="background-color: #eff6ff; color: #2563eb; font-size: 11px; font-weight: 600; padding: 2px 8px; border-radius: 4px;">시황</span>
						</div>
					</a> <a href="${pageContext.request.contextPath}/news"
						class="post-item">
						<div class="post-left">
							<span class="post-rank-badge" style="color: #eab308;">3</span>
							<div class="post-info-box">
								<span class="post-title" title="현대차·기아, 북미 친환경차 점유율 2위 굳히기 성공">현대차·기아,
									북미 친환경차 점유율 2위 굳히기 성공</span> <span class="post-meta-sub">매일경제 ·
									42분 전</span>
							</div>
						</div>
						<div class="post-right">
							<span
								style="background-color: #eff6ff; color: #2563eb; font-size: 11px; font-weight: 600; padding: 2px 8px; border-radius: 4px;">자동차</span>
						</div>
					</a> <a href="${pageContext.request.contextPath}/news"
						class="post-item">
						<div class="post-left">
							<span class="post-rank-badge">4</span>
							<div class="post-info-box">
								<span class="post-title"
									title="2차전지 반등 신호탄? 리튬 가격 바닥 통과 기대에 양극재주 강세">2차전지 반등
									신호탄? 리튬 가격 바닥 통과 기대에 양극재주 강세</span> <span class="post-meta-sub">이데일리
									· 1시간 전</span>
							</div>
						</div>
						<div class="post-right">
							<span
								style="background-color: #eff6ff; color: #2563eb; font-size: 11px; font-weight: 600; padding: 2px 8px; border-radius: 4px;">2차전지</span>
						</div>
					</a> <a href="${pageContext.request.contextPath}/news"
						class="post-item">
						<div class="post-left">
							<span class="post-rank-badge">5</span>
							<div class="post-info-box">
								<span class="post-title"
									title="정부, '기업 밸류업 프로그램' 세제 개편안 발표... 배당 분리과세 추진">정부,
									'기업 밸류업 프로그램' 세제 개편안 발표... 배당 분리과세 추진</span> <span
									class="post-meta-sub">머니투데이 · 2시간 전</span>
							</div>
						</div>
						<div class="post-right">
							<span
								style="background-color: #eff6ff; color: #2563eb; font-size: 11px; font-weight: 600; padding: 2px 8px; border-radius: 4px;">정책</span>
						</div>
					</a>
				</div>
			</div>

			<div class="dashboard-card">
				<div class="card-header-row">
					<h2 class="card-title">💬 커뮤니티 인기글</h2>
					<a href="${pageContext.request.contextPath}/community-lists"
						class="card-more-link">더보기</a>
				</div>
				<div class="post-list">
					<c:forEach var="post" items="${topPosts}" varStatus="status">
						<a
							href="${pageContext.request.contextPath}/community-lists/detail?postNum=${post.postNum}"
							class="post-item">
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

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
		crossorigin="anonymous"></script>
	<script src="https://cdn.jsdelivr.net/npm/apexcharts"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/apexstock/dist/apexstock.min.js"></script>

	<script>
		window.SERVER_CHART_DATA = ${chartDataJson};
	</script>
	<script src="/resources/js/draw_chart_KOSPI.js"></script>
</body>
</html>