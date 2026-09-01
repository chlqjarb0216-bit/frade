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
		<div >
			
			<!-- 검색 폼 -->
			<form onsubmit="searchPosts(event)" method="get" class="d-flex align-items-center">
				<!-- 라디오 버튼 그룹 -->
				<div >
					<div >
						<input  type="radio" name="type" id="postT" value="0" checked>
						<label  for="postT">제목</label>
					</div>
					<div>
						<input type="radio" name="type" id="postW" value="1">
						<label for="postW">작성자</label>
					</div>
				</div>
				<!-- 검색어 입력 및 버튼 -->
				<div style="width: 300px;">
					<input type="text" name="keyword" id="keyword" placeholder="검색어를 입력하세요">
					<button type="submit">검색</button>
				</div>
			</form>

			<!-- 글작성 버튼 -->
			<div>
				<a href="/community-lists/write">글작성</a>
			</div>
			
		</div>

		<!-- 게시글 목록 테이블 영역 -->
		<table>
			<thead >
				<tr>
					<th scope="col" width="10%">번호</th>
					<th scope="col" width="15%">카테고리</th>
					<th scope="col" width="40%">제목</th>
					<th scope="col" width="15%">작성자</th>
					<th scope="col" width="10%">조회수</th>
					<th scope="col" width="10%">작성일</th>
				</tr>
			</thead>
			<tbody id="postTableBody">
				<!-- 	script 영역 에서 그려줌 -->
			</tbody>
		</table>

		<!-- 페이징(Pagination) 영역 -->
		<nav aria-label="Page navigation">
			<ul id="paging">
				<!-- script 영역에서 그려줌 -->
			</ul>
		</nav>

	</div>

	<!-- 부트스트랩 JS -->
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous">
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
					
					//서버오류 조건처리 필요(예시)
// 					if(result.code !== 200) {
//                     alert("데이터를 불러오지 못했습니다.");
//                     return;
//                 }
					
					renderTable(result.list);
					renderPaging(result);
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
	                    <td>\${post.postNum}</td>
	                    <td>\${category}</td>
	                    <td>
	                        <a href="/community-lists/detail?postNum=\${post.postNum}">\${post.postTitle}</a>
	                    </td>
	                    <td>\${post.userNum}</td>
	                    <td>\${post.postViewCnt}</td>
	                    <td>\${post.postedDateString}</td>
	                </tr>
	            `;
	        });
	        postTableBody.innerHTML = html;
		}
		
		//하단 번호 그리기 함수
		function renderPaging(pageInfo){
			const paging = document.getElementById('paging');
	        let html = '';

	        // [이전] 버튼 (시작 페이지가 1보다 클 때만 활성화)
	        if (pageInfo.startPage > 1) {
	            html += `
		            <li>
		            	<a href="#" onclick="loadPosts(\${pageInfo.startPage - 1}); return false;">이전</a>
		            </li>
		        `;
	        }

	        // 숫자 버튼 (1~5, 6~10)
	        for (let i = pageInfo.startPage; i <= pageInfo.endPage; i++) {
	            // 현재 페이지면 active 클래스 추가
	            let active = (i === pageInfo.currentPage) ? "active" : "";
	            html += `
		            <li>
		            	<a href="#" onclick="loadPosts(\${i}); return false;">\${i}</a>
		            </li>
	            `;
	        }

	        // [다음] 버튼 (끝 페이지가 총 페이지수보다 작을 때만 활성화)
	        if (pageInfo.endPage < pageInfo.totalPages) {
	            html += `
		            <li>
		           		<a href="#" onclick="loadPosts(\${pageInfo.endPage + 1}); return false;">다음</a>
		            </li>
	            `;
	        }

	        paging.innerHTML = html;
		}
	
	</script>

</body>
</html>