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

.container {
    max-width: 900px;
}

form {
    display: flex;
    gap: 8px;
    margin-bottom: 20px;
}

form input {
    flex: 1;
    height: 44px;
    padding: 0 16px;
    border: 1px solid #e5e8eb;
    border-radius: 10px;
    outline: none;
}

form button {
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
</style>
</head>

<body>

    <jsp:include page="../common/navbar.jsp"></jsp:include>

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
                <div id="stock-chart"></div>
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

    <script src="/resources/js/stock_search_preview.js"></script>
    <script src="/resources/js/draw_chart.js"></script>
</body>

</html>