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

</head>

<body>

	<!-- 네비게이션바 include -->
	<jsp:include page="../common/navbar.jsp"></jsp:include>

	<!-- 본문 영역 (부트스트랩 container 클래스로 감싸 통일해두면 좋을듯) -->
	<div class="container mt-5">
		<a href="/community-lists" class="btn btn-outline-secondary"><- 커뮤니티로</a>
		
		<div>
			
			<p>${post.postTitle}</p>
			<p>${post.userName}</p>
			<p>${post.postedDateString}</p>
			<p>${post.postViewCnt}</p>
			<p>${post.postLikeCnt}</p>
			
		</div>
		<div>
			<p>${post.postContent}</p>
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