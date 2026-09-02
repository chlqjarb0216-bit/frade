<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

	<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
	crossorigin="anonymous">
	
	<style>
        .order-form {
            margin: 5%;
            padding: 5%;
            border: 1px solid black;
            border-radius: 15px;
            width: 40%;
            display: flex;
            flex-direction: column;
            align-items: center;
            align-content: center;

        }

        .trOption,
        .prOption {
            display: flex;
            flex-direction: row;
            flex-wrap: nowrap;
            justify-content: space-around;
        }

        .custom-radio {
            width: 185px;
            margin: 3px;
            border-radius: 15px;
            /* border: 0.1px solid gray; */
        }

        #orderPrice,
        #orderCount {
            width: 300px;
            height: 30px;
            border: 1px solid lightgray;
        }

        .custom-submit {
            width: 400px;
        }

        .caculPrice {
            width: 400px;
            text-align: left;
            margin: 20px;
            padding: 5px;
            border: 1px solid lightgray;
            border-radius: 10px;
        }

        .caculPrice p {
            margin: 10px;
        }

        .caculPrice span {
            margin-left: 130px;
        }


        .ctlBtn {
            width: 35px;
        }

        input::-webkit-outer-spin-button,
        input::-webkit-inner-spin-button {
            -webkit-appearance: none;
            margin: 0;
        }

        .order-modal {
            display: none;
            position: fixed;
            z-index: 1000;
            width: 500px;
            height: 500px;
            margin: 50px;
            padding: 50px;
            border: 2px solid lightgray;
            border-radius: 25px;
            background-color: #0e0f37;
            color: white;
        }

        .order-modal.modalOpen {
            display: block;
        }

        .text-box {
            border: 1px solid gray;
            border-radius: 10px;
            padding: 20px;
            display: flex;
            flex-direction: row;
            justify-content: space-between;
            align-items: center;
        }

        .custom-btn {
            width: 195px;
        }
    </style>
</head>


<body>

    <form action="" method="post" class="order-form">

        <!--============더미데이터==============-->
        <input type="hidden" id="stockName" value="삼성전자">
        <input type="hidden" id="stockCode" name="stockCode" value="005930">
        <!--============더미데이터==============-->

        <div class="trOption">
            <input type="radio" class="btn-check" name="tradeOption" id="success-outlined" autocomplete="off" checked
                value="BUY">
            <label class="btn btn-outline-primary custom-radio" for="success-outlined">
                <h3>매수</h3>
            </label>

            <input type="radio" class="btn-check" name="tradeOption" id="danger-outlined" autocomplete="off"
                value="SELL">
            <label class="btn btn-outline-danger custom-radio" for="danger-outlined">
                <h3>매도</h3>
            </label>
        </div>



        <div class="prOption">
            <input type="radio" class="btn-check" name="priceOption" id="marketPriceOpt" autocomplete="off" checked
                value="MARKETPRICE">
            <label class="btn btn-secondary custom-radio" for="marketPriceOpt">
                <h3>시장가</h3>
            </label>

            <input type="radio" class="btn-check" name="priceOption" id="limitPriceOpt" autocomplete="off"
                value="LIMITPRICE">
            <label class="btn btn-secondary custom-radio" for="limitPriceOpt">
                <h3>지정가</h3>
            </label>

        </div>

        <div style="margin-top: 20px;">
            <p>주문 가격</p>
            <input type="number" id="orderPrice" name="orderPrice" min="0" value="100" />
            <!--시장가 선택시 입력 닫고 지정가 선택시 기본 value에 시장가 넣을 예정-->

            <button type="button" class="btn btn-outline-primary ctlBtn"
                onclick="changeValue('orderPrice', 100)">+</button>
            <button type="button" class="btn btn-outline-danger ctlBtn"
                onclick="changeValue('orderPrice', -100)">-</button>
        </div>

        <div>
            <p>주문 수량</p>
            <input type="number" id="orderCount" name="orderCount" min="1" step="1" value="1" /> <!--max값에 보유수량 넣을 예정-->

            <button type="button" class="btn btn-outline-primary ctlBtn"
                onclick="changeValue('orderCount', 1)">+</button>
            <button type="button" class="btn btn-outline-danger ctlBtn"
                onclick="changeValue('orderCount', -1)">-</button>


        </div>




        <div class="caculPrice">
            <p>주문 가능 금액 <span>100,000,000 원</span></p> <!--usercash 불러올 예정-->
            <p>예상 주문 금액 <span id="expectedOrderAmount">0원</span></p>
        </div>

        <!-- <button type="submit">주문하기</button> -->
        <button type="button" class="btn btn-primary btn-lg custom-submit" id="openModal">주문하기</button>



        <div class="order-modal">

            <h3 style="font-weight: bold">주문 확인</h3><br>



            <div class="text-box">
                <div style="opacity: 0.5;">
                    <p>종목</p>
                    <p>구분</p>
                    <p>주문 가격</p>
                    <p>수량</p>
                </div>

                <div> <!--입력값대로 넣을 예정-->
                    <p id="cfStockName">삼성전자 (005930)</p>
                    <p id="cfTradeOption">매수 (지정가)</p>
                    <p id="cfOrderPrice">271,000원</p>
                    <p id="cfOrderCount">1주</p>
                </div>
            </div><br>
            <h4 style="text-align: right; font-weight: bold;">총 주문 금액 <span id="cfTotalPrice">271,000원</span></h5><br>

                <button type="button" class="btn btn-secondary custom-btn" id="cancleOrderBtn">
                    <h3>취소</h3>
                </button>
                <button type="submit" class="btn btn-primary custom-btn" id="confirmOrderBtn">
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


        function changeValue(inputId, changeAmount) {
            const input = document.getElementById(inputId);
            const currentValue = Number(input.value) || 0;

            // 가격과 수량이 음수가 되지 않도록 제한
            input.value = Math.max(0, currentValue + changeAmount);

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
            const stockName = document.getElementById("stockName").value;
            const stockCode = document.getElementById("stockCode").value;

            const orderPrice = Number(orderPriceInput.value) || 0;
            const orderCount = Number(orderCountInput.value) || 0;

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




    </script>
</body>

</html>