<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>주식</title>

<link
    href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
    rel="stylesheet"
    integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
    crossorigin="anonymous">

<style>
body {
    background-color: #f8f9fa;
    color: #191f28;
}

.stock-trade-container {
    max-width: 1280px;
}

.stock-search-form {
    display: flex;
    gap: 8px;
    margin-bottom: 20px;
}

.stock-search-form input {
    flex: 1;
    min-width: 0;
    height: 44px;
    padding: 0 16px;
    border: 1px solid #e5e8eb;
    border-radius: 10px;
    outline: none;
}

.stock-search-form button {
    padding: 0 20px;
    background-color: #3182f6;
    color: #ffffff;
    border: none;
    border-radius: 10px;
    font-weight: 600;
}

.stock, .chart-body {
    background: #ffffff;
    border: 1px solid #e5e8eb;
    border-radius: 16px;
    padding: 24px;
    margin-bottom: 20px;
}

.stock h2 {
    font-size: 20px;
    margin: 0;
}

.chart-box h2 {
    font-size: 18px;
    color: #191f28;
    margin-bottom: 16px;
}


.stock-search-form {
  position: relative;
  display: block; 
  width: 100%;
  box-sizing: border-box;        
}


.stock-search-form #searchKeyword {
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

.stock-trade-layout { display: flex; align-items: flex-start; gap: 24px; }
.stock-trade-main { flex: 1; min-width: 0; }
.stock-order-panel { flex: 0 0 430px; min-width: 0; }
.stock-trade-main .chart-box { max-width: none; }
@media (max-width: 991px) {
    .stock-trade-layout { flex-direction: column; }
    .stock-trade-main, .stock-order-panel { width: 100%; }
    .stock-order-panel { flex: none; }
}
</style>
</head>

<body>

    <jsp:include page="../common/navbar.jsp"></jsp:include>

    <div class="container stock-trade-container mt-5">
        <div class="stock-trade-layout">
        <div class="stock-trade-main">
        <form action="/stock/search" class="stock-search-form">
            <input id="searchKeyword" name="searchKeyword" type="text"
                placeholder="${ stockPreview.stockName }" />
            <ul id="previewList" style="display: none;"></ul>
            
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
				<h2>📈 가상 거래 차트</h2>
				<!-- 차트가 그려질 영역 -->
				<div id="stock-chart"></div>
			</div>
		</div>
		</div>
		<aside class="stock-order-panel">
			<jsp:include page="./order.jsp"><jsp:param name="embedded" value="true" /></jsp:include>
		</aside>
		</div>
    </div>

    <script
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
        crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/apexcharts"></script>
    <script
        src="https://cdn.jsdelivr.net/npm/apexstock/dist/apexstock.min.js"></script>

    <script src="/resources/js/stock_search_preview.js"></script>
    <script>
		// 💡 핵심: 백엔드 데이터를 브라우저 전역 변수에 먼저 심어줍니다.
		window.SERVER_CHART_DATA = ${chartDataJson};
	</script>
    <script src="/resources/js/draw_stock_chart.js"></script>
</body>

</html>
