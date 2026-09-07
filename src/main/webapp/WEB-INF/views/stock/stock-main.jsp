<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />

<meta name="viewport" content="width=device-width, initial-scale=1.0" />

<title>주식</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
	crossorigin="anonymous" />

<style>
body {
	background-color: #f8f9fa;
	color: #191f28;
	font-family: -apple-system, BlinkMacSystemFont, "Apple SD Gothic Neo",
		"Pretendard", Roboto, "Noto Sans KR", sans-serif;
	letter-spacing: -0.02em;
}

.stock-page-container {
	max-width: 1200px;
	margin: 40px auto 80px auto;
	padding: 0 20px;
}

.stock-header-area {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 24px;
}

.stock-header-area h1 {
	font-size: 24px;
	font-weight: 700;
	color: #191f28;
	margin: 0;
}

.search-form-wrapper {
	position: relative;
	display: flex;
	align-items: center;
	gap: 8px;
}

.search-form-wrapper input[type="text"] {
	width: 240px;
	height: 44px;
	padding: 0 16px;
	background-color: #ffffff;
	border: 1px solid #e5e8eb;
	border-radius: 10px;
	font-size: 14px;
	color: #191f28;
	outline: none;
	transition: border-color 0.15s ease, box-shadow 0.15s ease;
}

.search-form-wrapper input[type="text"]:focus {
	border-color: #3182f6;
	box-shadow: 0 0 0 3px rgba(49, 130, 246, 0.12);
}

.search-form-wrapper input[type="text"]::placeholder {
	color: #8b95a1;
}

.search-form-wrapper button[type="submit"] {
	height: 44px;
	padding: 0 20px;
	background-color: #3182f6;
	color: #ffffff;
	border: none;
	border-radius: 10px;
	font-size: 14px;
	font-weight: 600;
	cursor: pointer;
	transition: background-color 0.15s ease;
}

.search-form-wrapper button[type="submit"]:hover {
	background-color: #1b64da;
}

.main-content-layout {
	display: flex;
	gap: 24px;
	align-items: flex-start;
}

.stock-list-panel {
	flex: 1.1;
	background: #ffffff;
	border-radius: 16px;
	padding: 20px;
	border: 1px solid #e5e8eb;
	box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

#stock-table-body {
	display: flex;
	flex-direction: column;
	gap: 4px;
}

.stock {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 14px 18px;
	border-radius: 12px;
	cursor: pointer;
	transition: background-color 0.15s ease;
}

.stock:hover {
	background-color: #f2f4f6;
}

.stock>div:nth-child(1) {
	width: 32px;
	flex-shrink: 0;
}

.stock>div:nth-child(1) h3 {
	margin: 0;
	font-size: 16px;
	font-weight: 700;
	color: #8b95a1;
}

.stock>div:nth-child(2) {
	flex: 1;
	margin-left: 12px;
}

.stock>div:nth-child(2) p {
	margin: 0;
}

.stock>div:nth-child(2) strong {
	font-size: 15px;
	font-weight: 600;
	color: #191f28;
}

.stock-code {
	font-size: 12px;
	color: #8b95a1;
	margin-top: 2px;
}

.stock>div:nth-child(3) {
	text-align: right;
	margin-right: 20px;
}

.stock>div:nth-child(3) h2 {
	margin: 0;
	font-size: 15px;
	font-weight: 600;
	color: #191f28;
}

.stock>div:nth-child(4) {
	width: 74px;
	text-align: right;
	flex-shrink: 0;
}

.stock>div:nth-child(4) h4 {
	margin: 0;
	font-size: 14px;
	font-weight: 600;
	color: #f04452;
}

.active-stock {
	background-color: #e8f3ff !important;
}

.active-stock>div:nth-child(1) h3 {
	color: #3182f6;
}

.chart-panel-wrapper {
	flex: 1.3;
	display: flex;
	flex-direction: column;
	gap: 16px;
}

.chart-body {
	background-color: #ffffff;
	color: #191f28;
	border-radius: 16px;
	padding: 24px;
	border: 1px solid #e5e8eb;
	box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.chart-box {
	width: 100%;
	margin: 0;
	background: transparent;
	padding: 0;
	border-radius: 0;
	box-shadow: none;
}

.chart-box h2 {
	font-size: 18px;
	font-weight: 700;
	color: #191f28;
	margin-bottom: 20px;
}

.btn-move-trade {
	width: 100%;
	height: 48px;
	background-color: #3182f6;
	color: #ffffff;
	border: none;
	border-radius: 12px;
	font-size: 15px;
	font-weight: 600;
	cursor: pointer;
	margin-top: 20px;
	transition: background-color 0.15s ease;
}

.btn-move-trade:hover {
	background-color: #1b64da;
}

#paging {
	display: flex;
	justify-content: center;
	align-items: center;
	gap: 6px;
	margin-top: 24px;
}

#paging div a {
	display: inline-flex;
	justify-content: center;
	align-items: center;
	width: 34px;
	height: 34px;
	border-radius: 8px;
	font-size: 14px;
	color: #4e5968;
	text-decoration: none;
	transition: background-color 0.15s ease, color 0.15s ease;
}

#paging div a:hover {
	background-color: #e5e8eb;
	color: #191f28;
}

.active-page {
	background-color: #3182f6 !important;
	color: #ffffff !important;
	font-weight: 600;
	cursor: default;
}


.search-form-wrapper {
	position: relative;
	display: inline-block; 
	width: 260px; 
}


.search-form-wrapper #searchKeyword {
	width: 100%;
	box-sizing: border-box;
}


#previewList {
	position: absolute;
	top: calc(100% + 4px); 
	left: 0;
	width: 100%;
	box-sizing: border-box;
	margin: 0;
	padding: 6px 0;
	list-style: none;
	background-color: #ffffff;
	border: 1px solid #dcdfe6;
	border-radius: 8px;
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
	max-height: 240px; 
	overflow-y: auto;
	z-index: 1000; 
}


#previewList li {
	padding: 9px 14px;
	font-size: 14px;
	color: #333333;
	cursor: pointer;
	text-align: left;
	transition: background-color 0.15s ease;
}


#previewList li:hover {
	background-color: #f2f7ff;
	color: #2b7fff;
}
</style>
</head>

<body>
	<jsp:include page="../common/navbar.jsp"></jsp:include>

	<div class="container stock-page-container">
		<div class="stock-header-area">
			<h1>실시간 인기종목</h1>
			<form action="/stock/search" class="search-form-wrapper">
				<input id="searchKeyword" name="searchKeyword" type="text"
					placeholder="주식종목 입력" />
				<ul id="previewList" style="display: none"></ul>
			</form>
		</div>

		<div class="main-content-layout">
			<div class="stock-list-panel">
				<div id="stock-table-body"></div>
				<div id="paging"></div>
			</div>

			<div class="chart-panel-wrapper">
				<div class="chart-body">
					<div class="chart-box">
						<h2>📈 가상 거래 차트</h2>
						<div id="stock-chart"></div>
					</div>
					<button class="btn-move-trade" onclick="moveToTrade()">
						거래소 이동→</button>
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
	<script src="/resources/js/stock_list_chart.js"></script>

	<script src="/resources/js/stock_search_preview.js"></script>
	<script>
      window.onload = function () {
        loadStockList(1);
      };

      function loadStockList(page) {
        fetch(`/api/stock/stock-list?page=\${page}`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
          body: null,
        })
          .then((res) => res.json())
          .then((result) => {
            renderTable(result.data);
            renderPaging(page);
          });
      }

      function renderTable(stockList, activeStock = 0) {
        const stockTableBody = document.getElementById("stock-table-body");
        let html = "";

        stockTableBody.addEventListener("click", (event) => {
          const targetChild = event.target.closest(".stock");
          if (!targetChild) return;
          const currentActive = stockTableBody.querySelector(".active-stock");
          if (currentActive) {
            currentActive.classList.remove("active-stock");
          }
          targetChild.classList.add("active-stock");
          initChartFromDOM();
        });

        stockList.forEach((stock, index) => {
          let active = index == activeStock ? "active-stock" : "";
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
        
     // 2. ⭐ 이제 화면에 태그들이 확실히 존재하므로, 안전하게 호출합니다.
        if (typeof initChartFromDOM === "function") {
            initChartFromDOM();
        }
      }

      function renderPaging(page) {
        const paging = document.getElementById("paging");
        let html = "";

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

        for (
          let i = Math.floor((page - 1) / 5) * 5 + 1;
          i <= Math.floor((page - 1) / 5) * 5 + 5;
          i++
        ) {
          let active = i === page ? "active-page" : "";
          html += `
                    <div>
                        <a href="#" class="\${active}" onclick="loadStockList(\${i}); return false;">\${i}</a>
                    </div>
                `;
        }

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
        const activeCode = document.querySelector(".active-stock .stock-code");
        if (activeCode) {
          const value = activeCode.textContent;
          location.href = "/stock/" + value;
        } else {
          alert("이동 오류");
        }
      }
    </script>
</body>
</html>
