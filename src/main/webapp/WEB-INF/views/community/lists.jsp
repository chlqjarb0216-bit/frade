<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>질문 게시판 - 목록</title>

<!-- 부트스트랩 CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
	crossorigin="anonymous">

<!-- 대시보드 톤앤매너 커스텀 스타일 -->
<style>
body {
	background-color: #f8f9fa;
	color: #212529;
	font-family: -apple-system, BlinkMacSystemFont, "Apple SD Gothic Neo", "Pretendard", Roboto, "Noto Sans KR", sans-serif;
}


.custom-card {
	background-color: #ffffff;
	border: 1px solid #e9ecef;
	border-radius: 1rem; 
}


.custom-table thead th {
	background-color: #ffffff;
	color: #6c757d;
	font-weight: 600;
	font-size: 0.875rem;
	border-bottom: 1px solid #edf0f2;
	padding: 14px 12px;
}

.custom-table tbody td {
	padding: 16px 12px;
	border-bottom: 1px solid #f1f3f5;
	font-size: 0.93rem;
	vertical-align: middle;
}

.custom-table tbody tr:hover {
	background-color: #fbfcfd;
}


.post-title-link {
	text-decoration: none;
	color: #212529;
	font-weight: 500;
	transition: color 0.15s ease-in-out;
}

.post-title-link:hover {
	color: #0d6efd;
	text-decoration: none;
}


.category-badge {
	font-size: 0.78rem;
	font-weight: 500;
	padding: 4px 8px;
	border-radius: 6px;
	background-color: #f1f3f5;
	color: #495057;
}


.form-check-input:checked {
	background-color: #212529;
	border-color: #212529;
}

.search-input {
	border: 1px solid #dee2e6;
	border-radius: 8px;
	font-size: 0.9rem;
	padding: 7px 12px;
}

.search-input:focus {
	border-color: #adb5bd;
	box-shadow: none;
}


.pagination .page-link {
	color: #495057;
	border: 1px solid #e9ecef;
	margin: 0 3px;
	border-radius: 8px !important;
	font-size: 0.875rem;
	padding: 6px 12px;
}

.pagination .page-item.active .page-link {
	background-color: #212529;
	border-color: #212529;
	color: #ffffff;
}

.pagination .page-link:hover {
	background-color: #e9ecef;
}
</style>
</head>

<body>

	<!-- 네비게이션바 include -->
	<jsp:include page="../common/navbar.jsp"></jsp:include>

	<!-- 본문 영역 -->
	<div class="container my-5" style="max-width: 1080px;">

		
		<div class="mb-4">
			<h3 class="fw-bold mb-1"> 질문 게시판</h3>
			<p class="text-secondary small mb-0">투자 아이디어와 궁금한 점을 한눈에 확인하고 자유롭게 공유하세요.</p>
			<hr class="mt-3 mb-4 text-muted opacity-25">
		</div>

		
		<div class="custom-card p-3 mb-4">
			<div class="d-flex flex-wrap align-items-center justify-content-between gap-3">
				
				<!-- 검색 폼 -->
				<form onsubmit="searchPosts(event)" method="get" class="d-flex align-items-center gap-3 m-0">
					<!-- 라디오 버튼 그룹 -->
					<div class="d-flex align-items-center gap-3 text-secondary small">
						<div class="form-check m-0">
							<input class="form-check-input" type="radio" name="type" id="postT" value="0" checked>
							<label class="form-check-label text-dark" for="postT">제목</label>
						</div>
						<div class="form-check m-0">
							<input class="form-check-input" type="radio" name="type" id="postW" value="1">
							<label class="form-check-label text-dark" for="postW">작성자</label>
						</div>
					</div>

					<!-- 검색어 입력 및 버튼 -->
					<div class="input-group" style="width: 270px;">
						<input type="text" class="form-control search-input" name="keyword" id="keyword" placeholder="검색어를 입력하세요">
						<button class="btn btn-dark btn-sm px-3 rounded-end" type="submit">검색</button>
					</div>
				</form>

				<!-- 글작성 버튼 -->
				<div>
					<a href="/community-lists/write" class="btn btn-dark btn-sm px-3 py-1 fw-medium" style="border-radius: 8px;">
						+ 글작성
					</a>
				</div>

			</div>
		</div>

		<!-- 게시글 목록 테이블 영역 -->
		<div class="custom-card overflow-hidden mb-4">
			<div class="table-responsive">
				<table class="table custom-table mb-0 text-center">
					<thead>
						<tr>
							<th scope="col" width="10%">번호</th>
							<th scope="col" width="14%">카테고리</th>
							<th scope="col" width="46%" class="text-start ps-4">제목</th>
							<th scope="col" width="12%">작성자</th>
							<th scope="col" width="8%">조회수</th>
							<th scope="col" width="10%">작성일</th>
						</tr>
					</thead>
					<tbody id="postTableBody">
						<!-- script 영역 에서 그려줌 -->
					</tbody>
				</table>
			</div>
		</div>

		<!-- 페이징(Pagination) 영역 -->
		<nav aria-label="Page navigation" class="mt-4">
			<ul id="paging" class="pagination justify-content-center">
				<!-- script 영역에서 그려줌 -->
			</ul>
		</nav>

	</div>

	<!-- 부트스트랩 JS -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
		crossorigin="anonymous">
	</script>

	<script>
		//게시글 목록 페이지가 처음 열리면 1페이지 데이터를 불러옵니다.
		window.onload = function(){
			loadPosts(1)
		}
		
		// 검색 버튼을 눌렀을 때 실행되는 함수
		function searchPosts(event) {
		    event.preventDefault(); // 기본 새로고침 폼 전송을 멈춤
		    loadPosts(1); // 검색 시 무조건 1페이지부터 다시 보여줌
		}
		
		//서버에 비동기(fetch)로 데이터 요청 함수
		function loadPosts(page){
			const keyword = document.getElementById('keyword').value;
			const type = document.querySelector('input[name="type"]:checked').value
			
			//contrller에 게시글 데이터 요청 경로
			fetch(`/community-lists/api/post-list?page=\${page}&keyword=\${keyword}&type=\${type}`,{
				method: 'GET',
				headers:{
					'Content-Type' : 'application/json'
				},
				body:null
			})
				.then(response => response.json())
				.then(postList=>{

						console.log(postList.code);
					if(postList.code == "suc_002"){
						renderTable([]);
						clearPaging();
						return;
					}
					//서버오류 조건처리 필요
					if(postList.code !== "suc_001" ) {
                    	alert("데이터를 불러오지 못했습니다.");
                    return;
                }
					
					renderTable(postList.data.list);
					renderPaging(postList.data);
				});
		}
		
		//게시글 테이블 바디영역 그리기
		function renderTable(postList){
			const postTableBody = document.getElementById('postTableBody');
	        let html = '';
	        
	        postList.forEach(post => {
	            let category = post.postCategoryNum == 0 ? "질문" : (post.postCategoryNum == 1 ? "정보" : "자유");
	            html += `
	                <tr>
	                    <td class="text-secondary small">\${post.postNum}</td>
	                    <td><span class="category-badge">\${category}</span></td>
	                    <td class="text-start ps-4">
	                        <a class="post-title-link" href="/community-lists/detail?postNum=\${post.postNum}">\${post.postTitle}</a>
	                    </td>
	                    <td class="text-secondary small">\${post.userNum}</td>
	                    <td class="text-secondary small">\${post.postViewCnt}</td>
	                    <td class="text-secondary small">\${post.postedDateString}</td>
	                </tr>
	            `;
	        });
	        postTableBody.innerHTML = html;
		}
		
		//하단 번호 그리기 함수
		function clearPaging(){
			const paging = document.getElementById('paging');
			let html = '';
			
			html += `
				<div class="text-center py-5 text-secondary">
					<p class="mb-0 small">조회된 게시글이 없습니다.</p>
				</div>
			`;
			paging.innerHTML = html;
		}
		
		function renderPaging(pageInfo){
			const paging = document.getElementById('paging');
	        let html = '';

	        // [이전] 버튼 (시작 페이지가 1보다 클 때만 활성화)
	        if (pageInfo.startPage > 1) {
	            html += `
		            <li class="page-item">
		            	<a class="page-link" href="#" onclick="loadPosts(\${pageInfo.startPage - 1}); return false;">‹</a>
		            </li>
		        `;
	        }

	        // 숫자 버튼 (1~5, 6~10)
	        for (let i = pageInfo.startPage; i <= pageInfo.endPage; i++) {
	            // 현재 페이지면 active 클래스 추가
	            let active = (i === pageInfo.currentPage) ? "active" : "";
	            html += `
		            <li class="page-item \${active}">
		            	<a class="page-link" href="#" onclick="loadPosts(\${i}); return false;">\${i}</a>
		            </li>
	            `;
	        }

	        // [다음] 버튼 (끝 페이지가 총 페이지수보다 작을 때만 활성화)
	        if (pageInfo.endPage < pageInfo.totalPages) {
	            html += `
		            <li class="page-item">
		           		<a class="page-link" href="#" onclick="loadPosts(\${pageInfo.endPage + 1}); return false;">›</a>
		            </li>
	            `;
	        }

	        paging.innerHTML = html;
		}
	
	</script>

</body>
</html>