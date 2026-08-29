<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>질문 게시판 - 목록</title>

<!-- 부트스트랩 CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">

<!-- 커스텀 CSS (테이블 제목 링크 밑줄 제거 및 색상 유지) -->
<style>
	.post-title-link {
		text-decoration: none;
		color: #212529;
	}
	.post-title-link:hover {
		text-decoration: underline;
		color: #0d6efd;
	}
</style>
</head>

<body>

	<!-- 네비게이션바 include -->
	<jsp:include page="../common/navbar.jsp"></jsp:include>

	<!-- 본문 영역 -->
	<div class="container mt-5">
		
		<!-- 헤더 영역 -->
		<div class="mb-4">
			<h2 class="fw-bold">질문 게시판</h2>
			<p class="text-muted">투자 아이디어와 궁금한 점을 자유롭게 공유하세요.</p>
		</div>

		<!-- 상단 컨트롤 영역 (검색 & 글작성 버튼) -->
		<div class="d-flex justify-content-between align-items-end mb-3">
			
			<!-- 검색 폼 -->
			<form action="/community-lists" method="get" class="d-flex align-items-center">
				<!-- 라디오 버튼 그룹 -->
				<div class="me-3">
					<div class="form-check form-check-inline">
						<input class="form-check-input" type="radio" name="type" id="postT" value="0" checked>
						<label class="form-check-label" for="postT">제목</label>
					</div>
					<div class="form-check form-check-inline">
						<input class="form-check-input" type="radio" name="type" id="postW" value="1">
						<label class="form-check-label" for="postW">작성자</label>
					</div>
				</div>
				<!-- 검색어 입력 및 버튼 -->
				<div class="input-group" style="width: 300px;">
					<input type="text" class="form-control" name="keyword" placeholder="검색어를 입력하세요">
					<button class="btn btn-secondary" type="submit">검색</button>
				</div>
			</form>

			<!-- 글작성 버튼 -->
			<div>
				<a href="/community-lists/write" class="btn btn-primary px-4">글작성</a>
			</div>
			
		</div>

		<!-- 게시글 목록 테이블 영역 -->
		<table class="table table-hover text-center align-middle">
			<thead class="table-light">
				<tr>
					<th scope="col" width="10%">번호</th>
					<th scope="col" width="15%">카테고리</th>
					<th scope="col" width="40%">제목</th>
					<th scope="col" width="15%">작성자</th>
					<th scope="col" width="10%">조회수</th>
					<th scope="col" width="10%">작성일</th>
				</tr>
			</thead>
			<tbody>
	<c:if test="${empty postList}">
		<tr>
			<td colspan="6" class="py-5 text-muted">등록된 게시글이 없습니다.</td>
		</tr>
	</c:if>

	<c:forEach items="${postList}" var="post">
		<tr>
			<td>${post.PNum}</td>
			<td>
				<c:choose>
					<c:when test="${post.PCategoryNum == 0}">
						<span class="badge bg-secondary">질문</span>
					</c:when>
					<c:when test="${post.PCategoryNum == 1}">
						<span class="badge bg-success">정보</span>
					</c:when>
					<c:otherwise>
						<span class="badge bg-info text-dark">자유</span>
					</c:otherwise>
				</c:choose>
			</td>
			<td class="text-start">
				<a href="/community-lists/detail?pNum=${post.PNum}" class="post-title-link">${post.PTitle}</a>
			</td>
			<td>${post.UNum}</td>
			<td>${post.PViewCnt}</td>
			<td>${post.PPostedDate}</td>
		</tr>
	</c:forEach>
</tbody>
		</table>

		<!-- 페이징(Pagination) 영역 -->
		<nav aria-label="Page navigation" class="mt-4 mb-5">
			<ul class="pagination justify-content-center">
				<li class="page-item disabled">
					<a class="page-link" href="#" tabindex="-1" aria-disabled="true">이전</a>
				</li>
				<li class="page-item active"><a class="page-link" href="#">1</a></li>
				<li class="page-item"><a class="page-link" href="#">2</a></li>
				<li class="page-item"><a class="page-link" href="#">3</a></li>
				<li class="page-item">
					<a class="page-link" href="#">다음</a>
				</li>
			</ul>
		</nav>

	</div>

	<!-- 부트스트랩 JS -->
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>

</body>
</html>