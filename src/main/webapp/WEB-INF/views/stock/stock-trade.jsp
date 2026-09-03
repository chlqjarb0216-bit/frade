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
	<script>
					// 일단 띄우기용 더미 데이터
					// 2. 가상의 주식/코인 데이터 생성 (타임스탬프, [시가, 고가, 저가, 종가])
					// 데이터 포맷: [Timestamp, [Open, High, Low, Close]]
					const chartData = [
						[1791244800000, [42000, 42300, 41800, 42100]], // 1일차
						[1791331200000, [42100, 42600, 42000, 42450]], // 2일차
						[1791417600000, [42450, 42500, 41500, 41700]], // 3일차
						[1791504000000, [41700, 42200, 41600, 42000]], // 4일차
						[1791590400000, [42000, 43100, 41900, 42900]], // 5일차
						[1791676800000, [42900, 43500, 42800, 43200]], // 6일차
						[1791763200000, [43200, 43800, 43000, 43600]], // 7일차
						[1791849600000, [43600, 44200, 43500, 43900]], // 8일차
						[1791936000000, [43900, 44000, 42100, 42500]], // 9일차
						[1792022400000, [42500, 43000, 42300, 42800]]  // 10일차
					];

					// 3. 차트 설정 옵션 정의
					const options = {
						series: [{
							name: 'candle',
							data: chartData.map(item => ({
								x: new Date(item[0]),
								y: item[1]
							}))
						}],
						chart: {
							type: 'candlestick',
							height: 450,
							background: '#161a1e',
							foreColor: '#90a4ae', // 글자 색상
							toolbar: {
								show: true, // 우측 상단 드로잉/다운로드 툴바 활성화
								tools: {
									download: true,
									selection: true,
									zoom: true,
									zoomin: true,
									zoomout: true,
									pan: true,
									reset: true
								}
							}
						},
						theme: {
							mode: 'dark' // 💡 다크모드 적용
						},
						title: {
							text: '실시간 시세 현황',
							align: 'left'
						},
						xaxis: {
							type: 'datetime',
							labels: {
								style: {colors: '#90a4ae'}
							}
						},
						yaxis: {
							tooltip: {
								enabled: true // 마우스 올렸을 때 가격 표시
							},
							labels: {
								formatter: function (val) {
									return '$' + val.toLocaleString(); // 달러 표기 포맷팅
								}
							}
						},
						plotOptions: {
							candlestick: {
								colors: {
									upward: '#00b4d8',  // 상승 봉 색상 (초록/파랑 계열)
									downward: '#ff4d4d' // 하락 봉 색상 (빨강 계열)
								}
							}
						}
					};

					// 4. 차트 생성 및 렌더링
					const chart = new ApexCharts(document.querySelector("#stock-chart"), options);
					chart.render();
				</script>
</body>

</html>