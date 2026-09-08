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

<title>샘플 JSP 페이지 양식-부트스트랩 첨가</title>

<!-- 부트스트랩 CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
	crossorigin="anonymous">
<!-- 추가 커스텀 CSS 필요하면 여기에 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/navbar.css">
<style>
    body {
        background-color: #f2f4f6;
        color: #333d4b;
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
        min-height: 100vh;
    }
    .error-card {
        background-color: #ffffff;
        border: 1px solid #e5e8eb;
        border-radius: 20px;
        padding: 60px 40px;
        text-align: center;
        max-width: 540px;
        margin: 80px auto;
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
    }
    .error-code {
        font-size: 72px;
        font-weight: 800;
        color: #3182f6;
        line-height: 1;
        margin-bottom: 16px;
    }
    .error-title {
        font-size: 22px;
        font-weight: 700;
        color: #191f28;
        margin-bottom: 12px;
    }
    .error-desc {
        font-size: 15px;
        color: #8b95a1;
        margin-bottom: 28px;
        line-height: 1.6;
    }
    .btn-home {
        display: inline-block;
        background-color: #3182f6;
        color: #ffffff;
        font-weight: 600;
        font-size: 15px;
        padding: 12px 28px;
        border-radius: 12px;
        text-decoration: none;
        transition: background-color 0.2s;
    }
    .btn-home:hover {
        background-color: #1b64da;
        color: #ffffff;
    }
</style>
</head>

<body>

	<!-- 네비게이션바 include -->
	<jsp:include page="../common/navbar.jsp"></jsp:include>

	<!-- 본문 영역 (부트스트랩 container 클래스로 감싸 통일해두면 좋을듯) -->
	<div class="container">
        <div class="error-card">
            <div class="error-code">404</div>
            <h1 class="error-title">페이지를 찾을 수 없습니다</h1>
            <p class="error-desc">${ errorMessage != null ? errorMessage : "요청하신 페이지가 존재하지 않거나 주소가 변경되었습니다." }</p>
            <a href="${pageContext.request.contextPath}/main" class="btn-home">홈으로 이동</a>
        </div>
	</div>

	<!-- 부트스트랩 JS -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
		crossorigin="anonymous"></script>

	<!-- 추가 커스텀 JS 필요하면 여기에 -->

</body>

</html>