<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<c:if test="${param.embedded ne 'true'}">
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>주식 주문</title>

<link
    href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
    rel="stylesheet"
    integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
    crossorigin="anonymous">
</head>
<body>
</c:if>

<style>
.order-form {
    color: #191f28;
    font-family: -apple-system, BlinkMacSystemFont, "Apple SD Gothic Neo", "Pretendard", Roboto, "Noto Sans KR", sans-serif;
    letter-spacing: -0.02em;
}
.order-form {
    width: 100%;
    max-width: 100%;
    margin: 0 auto;
    padding: 28px 24px;
    background: #ffffff;
    border: 1px solid #e5e8eb;
    border-radius: 20px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.04);
    display: flex;
    flex-direction: column;
    gap: 18px;
}
.order-form .trOption, .order-form .prOption {
    display: flex;
    gap: 8px;
    width: 100%;
}
.order-form .custom-radio {
    flex: 1;
    min-width: 0;
    height: 46px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 12px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.15s ease;
}
.order-form .custom-radio h3 {
    margin: 0;
    font-size: 16px;
    font-weight: 700;
}
.order-form .trOption .btn-outline-primary {
    border-color: #f04452;
    color: #f04452;
}
.order-form .trOption .btn-check:checked + .btn-outline-primary {
    background-color: #f04452;
    border-color: #f04452;
    color: #ffffff;
}
.order-form .trOption .btn-outline-danger {
    border-color: #3182f6;
    color: #3182f6;
}
.order-form .trOption .btn-check:checked + .btn-outline-danger {
    background-color: #3182f6;
    border-color: #3182f6;
    color: #ffffff;
}
.order-form .prOption .btn-secondary {
    background-color: #f2f4f6;
    border-color: transparent;
    color: #8b95a1;
}
.order-form .prOption .btn-check:checked + .btn-secondary {
    background-color: #333d4b;
    border-color: transparent;
    color: #ffffff;
}
.order-form > div:not(.trOption):not(.prOption):not(.caculPrice):not(.order-modal) {
    width: 100%;
}
.order-form > div:not(.trOption):not(.prOption):not(.caculPrice):not(.order-modal) p {
    margin-bottom: 8px;
    font-size: 14px;
    font-weight: 600;
    color: #4e5968;
}
.order-form > div:not(.trOption):not(.prOption):not(.caculPrice):not(.order-modal) {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
}
.order-form > div:not(.trOption):not(.prOption):not(.caculPrice):not(.order-modal) p {
    width: 100%;
}
.order-form #orderPrice, .order-form #orderCount {
    flex: 1;
    min-width: 0;
    height: 44px;
    border: 1px solid #e5e8eb;
    border-radius: 10px;
    padding: 0 14px;
    font-size: 15px;
    font-weight: 600;
    color: #191f28;
    outline: none;
    margin-right: 6px;
    transition: border-color 0.15s ease, box-shadow 0.15s ease;
}
.order-form #orderPrice:focus, .order-form #orderCount:focus {
    border-color: #3182f6;
    box-shadow: 0 0 0 3px rgba(49, 130, 246, 0.12);
}
.order-form .ctlBtn {
    width: 44px;
    height: 44px;
    border-radius: 10px;
    font-size: 18px;
    font-weight: 600;
    margin-left: 4px;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    border: 1px solid #e5e8eb;
    background-color: #ffffff;
    color: #4e5968;
}
.order-form .ctlBtn:hover {
    background-color: #f2f4f6;
    color: #191f28;
    border-color: #e5e8eb;
}
.order-form .caculPrice {
    width: 100%;
    margin: 4px 0 0 0;
    padding: 16px 18px;
    border: none;
    background-color: #f8f9fa;
    border-radius: 14px;
    display: flex;
    flex-direction: column;
    gap: 10px;
}
.order-form .caculPrice p {
    margin: 0;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 14px;
    color: #8b95a1;
    font-weight: 500;
}
.order-form .caculPrice span {
    margin-left: 0 !important;
    font-size: 14px;
    font-weight: 600;
    color: #333d4b;
}
.order-form .caculPrice p:last-child {
    margin-top: 4px;
    padding-top: 10px;
    border-top: 1px dashed #e5e8eb;
}
.order-form .caculPrice p:last-child span {
    font-size: 16px;
    font-weight: 700;
    color: #3182f6;
}
.order-form .custom-submit {
    width: 100%;
    height: 52px;
    background-color: #3182f6;
    border: none;
    border-radius: 14px;
    font-size: 16px;
    font-weight: 700;
    color: #ffffff;
    cursor: pointer;
    margin-top: 6px;
    transition: background-color 0.15s ease;
}
.order-form .custom-submit:hover {
    background-color: #1b64da;
}
.order-form .order-modal {
    display: none;
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    z-index: 1050;
    width: 90%;
    max-width: 400px;
    height: auto;
    max-height: calc(100vh - 32px);
    overflow-y: auto;
    margin: 0;
    padding: 28px 24px;
    border: none;
    border-radius: 20px;
    background-color: #ffffff;
    color: #191f28;
    box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}
.order-form .order-modal.modalOpen {
    display: block;
}
.order-form .order-modal h3 {
    font-size: 20px;
    font-weight: 700;
    color: #191f28;
    margin-bottom: 20px;
}
.order-form .text-box {
    border: 1px solid #f2f4f6;
    background-color: #f8f9fa;
    border-radius: 12px;
    padding: 16px;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin-bottom: 20px;
}
.order-form .text-box p {
    margin: 6px 0;
    font-size: 14px;
}
.order-form .text-box > div:first-child p {
    color: #8b95a1;
    font-weight: 500;
}
.order-form .text-box > div:last-child p {
    color: #191f28;
    font-weight: 600;
    text-align: right;
}
.order-form .order-modal h4 {
    font-size: 16px;
    font-weight: 600;
    color: #4e5968;
    text-align: right;
    margin-bottom: 24px;
}
.order-form .order-modal h4 span {
    font-size: 20px;
    font-weight: 700;
    color: #3182f6;
    margin-left: 8px;
}
.order-form .order-modal .custom-btn {
    width: calc(50% - 4px);
    height: 48px;
    border-radius: 12px;
    font-weight: 600;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    border: none;
}
.order-form .order-modal .custom-btn h3 {
    margin: 0;
    font-size: 15px;
    font-weight: 600;
}
.order-form #cancleOrderBtn {
    background-color: #f2f4f6;
    color: #4e5968;
}
.order-form #cancleOrderBtn:hover {
    background-color: #e5e8eb;
}
.order-form #confirmOrderBtn {
    background-color: #3182f6;
    color: #ffffff;
}
.order-form #confirmOrderBtn:hover {
    background-color: #1b64da;
}
.order-form input::-webkit-outer-spin-button, .order-form input::-webkit-inner-spin-button {
    -webkit-appearance: none;
    margin: 0;
}
</style>

	<form action="${pageContext.request.contextPath}/stock/trade" method="post" class="order-form">



        <input type="hidden" id="stockName" value="<c:out value="${stockPreview.stockName}"/>"> <input
            type="hidden" id="stockCode" name="stockCode" value="<c:out value="${stockPreview.stockCode}"/>">

        <div class="trOption">
            <input type="radio" class="btn-check" name="tradeOption"
                id="success-outlined" autocomplete="off" checked value="BUY">
            <label class="btn btn-outline-primary custom-radio"
                for="success-outlined">
                <h3>매수</h3>
            </label> <input type="radio" class="btn-check" name="tradeOption"
                id="danger-outlined" autocomplete="off" value="SELL"> <label
                class="btn btn-outline-danger custom-radio" for="danger-outlined">
                <h3>매도</h3>
            </label>
        </div>



        <div class="prOption">
            <input type="radio" class="btn-check" name="priceOption"
                id="limitPriceOpt" autocomplete="off" value="LIMITPRICE" checked> <label
                class="btn btn-secondary custom-radio" for="limitPriceOpt">
                <h3>지정가</h3>
                
            </label> <input type="radio" class="btn-check" name="priceOption"
                id="marketPriceOpt" autocomplete="off" value="MARKETPRICE">
            <label class="btn btn-secondary custom-radio" for="marketPriceOpt">
                <h3>시장가</h3>
            </label>



        </div>

        <div style="margin-top: 20px;">
            <p>주문 가격</p>
            <input type="number" id="orderPrice" name="orderPrice" min="0"
                value="${stockPreview.price}" />


            <button type="button" class="btn btn-outline-primary ctlBtn pricePtn"
                onclick="changeValue('orderPrice', 100)">+</button>
            <button type="button" class="btn btn-outline-danger ctlBtn pricePtn"
                onclick="changeValue('orderPrice', -100)">-</button>
        </div>

        <div>
            <p>주문 수량</p>
            <input type="number" id="orderCount" name="orderCount" min="1"
                step="1" value="1" />

                <button type="button" class="btn btn-outline-primary ctlBtn"
                onclick="changeValue('orderCount', 1)">+</button>
                <button type="button" class="btn btn-outline-danger ctlBtn"
                onclick="changeValue('orderCount', -1)">-</button>
        </div>




        <div class="caculPrice">
            <p>
                주문 가능 금액 <span>${userCash} 원</span>
            </p>
            <p>
                보유 수량 <span>${stockCnt} 주</span>
            </p>
            <p>
                예상 주문 금액 <span id="expectedOrderAmount">0원</span>
            </p>
        </div>

        <button type="button" class="btn btn-primary btn-lg custom-submit"
            id="openModal">주문하기</button>



        <div class="order-modal">

            <h3 style="font-weight: bold">주문 확인</h3>
            <br>



            <div class="text-box">
                <div style="opacity: 0.5;">
                    <p>종목</p>
                    <p>구분</p>
                    <p>주문 가격</p>
                    <p>수량</p>
                </div>

                <div>
                    <p id="cfStockName"></p>
                    <p id="cfTradeOption"></p>
                    <p id="cfOrderPrice"></p>
                    <p id="cfOrderCount"></p>
                </div>
            </div>
            <br>
            <h4 style="text-align: right; font-weight: bold;">
                총 주문 금액 <span id="cfTotalPrice"></span>
                </h4>
                <br>

                <button type="button" class="btn btn-secondary custom-btn"
                    id="cancleOrderBtn">
                    <h3>취소</h3>
                </button>
                <button type="submit" class="btn btn-primary custom-btn"
                    id="confirmOrderBtn">
                    <h3>주문 확정</h3>
                </button>
        </div>

    </form>

    <script>

        const orderPriceInput = document.getElementById("orderPrice");
        const orderCountInput = document.getElementById("orderCount");
        const expectedAmount = document.getElementById("expectedOrderAmount");
        const openModalBtn = document.getElementById("openModal");
        const closeBtn = document.getElementById("cancleOrderBtn");
        const modalContainer = document.querySelector(".order-modal");

        const cfStockName = document.getElementById("cfStockName");
        const cfTradeOption = document.getElementById("cfTradeOption");
        const cfOrderPrice = document.getElementById("cfOrderPrice");
        const cfOrderCount = document.getElementById("cfOrderCount");
        const cfTotalPrice = document.getElementById("cfTotalPrice");

        const priceOption = document.querySelectorAll('input[name="priceOption"]');

        const pricePtn = document.querySelectorAll(".pricePtn");
        
        
        
        function changeValue(inputId, changeAmount) {
            const input = document.getElementById(inputId);
            const currentValue = Number(input.value) || 0;

            input.value = Math.max(Number(input.min), currentValue + changeAmount);

            updateExpectedAmount();
        }
        function updateExpectedAmount() {
            const price = Number(orderPriceInput.value) || 0;
            const count = Number(orderCountInput.value) || 0;

            const totalAmount = price * count;

            expectedAmount.textContent =
                totalAmount.toLocaleString("ko-KR") + "원";
        }

        function openOrderModal() {
            if ('${empty sessionScope.loginUser}' === 'true') {
                window.location.href = '${pageContext.request.contextPath}/user/login';
                return;
            }
            const stockName = document.getElementById("stockName").value;
            const stockCode = document.getElementById("stockCode").value;

            const orderPrice = Number(orderPriceInput.value) || 0;
            const orderCount = Number(orderCountInput.value) || 0;

            if (orderPrice <= 0) {
                alert("가격이 0보다 커야 합니다.");
                return;
            }

            if (orderCount <= 0) {
                alert("수량이 0보다 커야 합니다.");
                return;
            }

            const tradeOption = document.querySelector('input[name="tradeOption"]:checked').value;
            const priceOption = document.querySelector('input[name="priceOption"]:checked').value;

            const totalPrice = orderPrice * orderCount;

            const tradeOptionText = tradeOption === "BUY" ? "매수" : "매도";

            const priceOptionText = priceOption === "LIMITPRICE" ? "지정가" : "시장가";

            cfStockName.textContent = stockName + " (" + stockCode + ")";

            cfTradeOption.textContent = tradeOptionText + " (" + priceOptionText + ")";

            cfOrderPrice.textContent = orderPrice.toLocaleString("ko-KR") + "원";

            cfOrderCount.textContent = orderCount.toLocaleString("ko-KR") + "주";

            cfTotalPrice.textContent = totalPrice.toLocaleString("ko-KR") + "원";

            modalContainer.classList.add("modalOpen");
        }

        function closeOrderModal() {
            modalContainer.classList.remove("modalOpen");
        }


        orderPriceInput.addEventListener(
            "input",
            updateExpectedAmount
        );

        orderCountInput.addEventListener(
            "input",
            updateExpectedAmount
        );

        openModalBtn.addEventListener(
            "click",
            openOrderModal
        );

        closeBtn.addEventListener(
            "click",
            closeOrderModal
        );
        
        
        priceOption.forEach(radio => {
            radio.addEventListener('change', (e) => {
                if (e.target.value === "MARKETPRICE") {
                    orderPriceInput.value = 210000;
                    orderPriceInput.readOnly = true;
                    
                    orderPriceInput.style.backgroundColor = "#e9ecef";

                    pricePtn.forEach(btn => {
                    btn.disabled = true;
            });

                } else {
                    orderPriceInput.value = orderPriceInput.value;
                    orderPriceInput.readOnly = false;
                    
                    orderPriceInput.style.backgroundColor = "";
                    orderPriceInput.style.cursor = "";
                    
                    pricePtn.forEach(btn => {
                    btn.disabled = false;
            });
                }
            });
        });

        updateExpectedAmount();
    </script>
<c:if test="${param.embedded ne 'true'}">
</body>
</html>
</c:if>
