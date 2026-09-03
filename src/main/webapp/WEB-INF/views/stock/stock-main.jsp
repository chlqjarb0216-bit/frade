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
		<div style="display: flex; justify-content: space-between">
			<h1>실시간 인기종목</h1>
			<form action="/stock/search">
				<input id="searchKeyword" name="searchKeyword" type="text"
					placeholder="주식종목 입력" />
				<ul id="previewList" style="display: none;"></ul>
				<button type="submit">검색</button>
			</form>
		</div>
		<div style="display: flex; justify-content: space-between">
			<div id="stock-table-body"></div>
			<div class="chart-body">
				<div class="chart-box">
					<h2>📈 가상 거래 차트 (BTC/USDT 시뮬레이터 예시)</h2>
					<!-- 차트가 그려질 영역 -->
					<div id="stock-chart"></div>
				</div>
				<button onclick="moveToTrade()">거래소 이동→</button>
			</div>
		</div>
		<div id="paging" style="display: flex"></div>
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
	<!-- 팀장님 스크립트 돚거 -->
	<script>
					//페이지가 처음 열리면 1페이지 데이터를 불러옵니다.
					window.onload = function () {
						loadStockList(1)
					}

					//서버에 비동기(fetch)로 데이터 요청 함수
					function loadStockList(page) {
						//contrller에 페이지 데이터 요청 경로
						fetch(`/stock/api/stock-list?page=\${page}`, {
							method: 'GET',
							headers: {
								'Content-Type': 'application/json'
							},
							body: null
						})
							.then(res => res.json())
							.then(result => {
								renderTable(result.data);
								renderPaging(page);
							});
					}

					//게시글 테이블 바디영역 그리기
					function renderTable(stockList, activeStock = 0) {
						const stockTableBody = document.getElementById('stock-table-body');
						let html = '';

						stockTableBody.addEventListener('click', (event) => {
							const targetChild = event.target.closest('.stock');
							if (!targetChild) return;
							const currentActive = stockTableBody.querySelector('.active-stock');
							if (currentActive) {
								currentActive.classList.remove('active-stock');
							}
							targetChild.classList.add('active-stock')
						});

						stockList.forEach((stock, index) => {
							let active = index == activeStock ? "active-stock" : ""
							html += `
							<div class="\${active} stock" style="display:flex; justify-content:space-between">
								<div>
									<h3>\${ index+1 }</h3>
								</div>
								<div>
									<p>
										<strong>\${ stock.stockName }</strong>
									</p>
									<p class="stock-code">\${ stock.stockCode }</p>
								</div>
								<div>
									<h2>\${ stock.price }</h2>
								</div>
								<div>
									<h4>\${ stock.dailyPriceChangeRoundedPercent }</h4>
								</div>
							</div>
	            `;
						});
						stockTableBody.innerHTML = html;
					}

					//하단 번호 그리기 함수
					function renderPaging(page) {
						const paging = document.getElementById('paging');
						let html = '';

						// [이전] 버튼 (시작 페이지가 1보다 클 때만 활성화)
						if (page > 1) {
							html += `
					<div>
						<a href="#" onclick="loadStockList(1); return false;">≪</a>
					</div>
		            <div>
		            	<a href="#" onclick="loadStockList(\${page - 1}); return false;">&lt;</a>
		            </div>
		        `;
						}

						// 숫자 버튼 (1~5, 6~10)
						for (let i = Math.floor((page - 1) / 5) * 5 + 1; i <= Math.floor((page - 1) / 5) * 5 + 5; i++) {
							// 현재 페이지면 active 클래스 추가
							let active = (i === page) ? "active-page" : "";
							html += `
		            <div>
		            	<a href="#" class="\${active}" onclick="loadStockList(\${i}); return false;">\${i}</a>
		            </div>
	            `;
						}

						// [다음] 버튼 (끝 페이지가 총 페이지수보다 작을 때만 활성화)
						if (page < 10) {
							html += `
		            <div>
		           		<a href="#" onclick="loadStockList(\${page + 1}); return false;">&gt;</a>
		            </div>
					<div>
						<a href="#" onclick="loadStockList(10); return false;">≫</a>
					</div>
	            `;
						}

						paging.innerHTML = html;
					}

					function moveToTrade() {
						const activeCode = document.querySelector('.active-stock .stock-code');
						if (activeCode) {
							const value = activeCode.textContent;
							location.href = "/stock/" + value;
						} else {
							alert("이동 오류")
						}
					}

				</script>

	<script src="/resources/js/draw_chart.js"></script>
</body>

</html>