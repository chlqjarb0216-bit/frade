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
		
		.orderContainer{
			border: 1px solid black;
		}
	</style>
</head>

<body>
	
	<div class="orderContainer">
		<form action="" method="post">
	
			<div>
				<label for="sellOpt"><input type="radio" name="tradeOption" id="sellOpt" class="hidden" value="BUY">매수</lavel>
				<label for="buyOpt"><input type="radio" name="tradeOption" id="buyOpt" class="hidden" value="SELL">매도</lavel>
			</div>
			
			<div>
				<label for="limitPriceOpt"><input type="radio" name="priceOption" id="limitPriceOpt" class="hidden" value="limitPriceOpt">지정가</label>
				<label for="marketPriceOpt"><input type="radio" name="priceOption" id="marketPriceOpt" class="hidden" value="marketPriceOpt">시장가</label>
			</div>
			
			<p>주문 가격</p>
			<input type="text" id="orderPrice" name="orderPrice"/>
			
			<p>주문 수량</p>
			<input type="text" id="orderCount" name="orderCount"/>
			
			<div>
				<p>주문 가능 금액</p>
			</div>
			
			<div>
				<p>예상 주문 금액</p>
			</div>
			
			<button type="submit">주문하기</button>
			
		</form>
	</div>

</body>
</html>