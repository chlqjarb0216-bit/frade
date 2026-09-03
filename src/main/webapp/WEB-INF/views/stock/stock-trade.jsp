<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">

<!-- 스마트폰 등의 환경에서 원래 크기로 보이도록 -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>주식</title>

<!-- 부트스트랩 CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
	crossorigin="anonymous">
<!-- 추가 커스텀 CSS 필요하면 여기에 -->

<style>
.chart-body {
	background-color: #0b0e11;
	/* 가상거래소 느낌의 어두운 배경 */
	color: #ffffff;
	font-family: 'Malgun Gothic', sans-serif;
	margin: 0;
	padding: 20px;
}

.chart-box {
	max-width: 900px;
	margin: 0 auto;
	background: #161a1e;
	padding: 20px;
	border-radius: 8px;
	box-shadow: 0 4px 15px rgba(0, 0, 0, 0.5);
}

.chart-h2 {
	margin-top: 0;
	font-size: 20px;
	color: #f0b90b;
	/* 바이낸스 스타일 포인트 컬러 */
}

.active-page {
	color: black;
	cursor: not-allowed;
}

.active-stock {
	background-color: lightblue;
	cursor: not-allowed;
}
</style>
</head>

<body>

	<!-- 네비게이션바 include -->
	<jsp:include page="../common/navbar.jsp"></jsp:include>

	<!-- 본문 영역 (부트스트랩 container 클래스로 감싸 통일해두면 좋을듯) -->
	<div class="container mt-5">
		<form action="/stock/search">
			<input id="searchKeyword" name="searchKeyword" type="text"
				placeholder="${ stockPreview.stockName }" />
			<ul id="previewList" style="display: none;"></ul>
			<button>검색</button>
		</form>
		<div class="stock">
			<div style="display: flex; justify-content: space-between">
				<p>
					<strong>${ stockPreview.stockName }</strong>
				</p>
				<p>${ stockPreview.stockCode }・${ stockPreview.sectorName }</p>
			</div>
			<div style="display: flex; justify-content: space-between">
				<h2>${ stockPreview.price }</h2>
				<c:choose>
					<c:when test="${ stockPreview.dailyPriceChangeRoundedPercent>0 }">
						<h2 style="color: red">▲${ stockPreview.dailyPriceChangeRoundedPercent }%</h2>
						<h2 style="color: red">${ stockPreview.prevDayClosePrice }</h2>
					</c:when>
					<c:when test="${ stockPreview.dailyPriceChangeRoundedPercent<0 }">
						<h2 style="color: blue">▼${ stockPreview.dailyPriceChangeRoundedPercent }%</h2>
						<h2 style="color: blue">${ stockPreview.prevDayClosePrice }</h2>
					</c:when>
					<c:otherwise>
						<h2>${ stockPreview.dailyPriceChangeRoundedPercent }%</h2>
						<h2>${ stockPreview.prevDayClosePrice }</h2>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="chart-body">
			<div class="chart-box">
				<h2>📈 가상 거래 차트 (BTC/USDT 시뮬레이터 예시)</h2>
				<!-- 차트가 그려질 영역 -->
				<div id="stock-chart"></div>
			</div>
		</div>
	</div>

	<!-- 부트스트랩 JS -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
		crossorigin="anonymous"></script>
	<!-- ApexChart -->
	<script src="https://cdn.jsdelivr.net/npm/apexcharts"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/apexstock/dist/apexstock.min.js"></script>

	<!-- 추가 커스텀 JS 필요하면 여기에 -->
	<!-- 검색 미리보기 -->
	<script src="/resources/js/stock_search_preview.js"></script>
	<script src="/resources/js/draw_chart.js"></script>
</body>

</html>