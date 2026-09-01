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
		<a href="/community-lists" class="btn btn-outline-secondary"><-
			커뮤니티로</a>

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

		<!-- 댓글작성영역 -->
		<div>
			<form  method="post">
				<input type="text">
				<button type="button" onclick="sendComment()" >작성</button>
			</form>
		</div>
		
		<!-- 댓글리스트영역 -->
		<!-- 헤더 -->
		<div>
			<h5>
				댓글 (<span id="commentCount">0</span>)개
			</h5>
		</div>
		<!-- 본문 -->
		<div id="commentList">
			<!-- script에서 그려줌  -->
		</div>
		
		<!-- 페이징(Pagination) 영역 -->
		<nav aria-label="Page navigation">
			<ul id="paging">
				<!-- script 영역에서 그려줌 -->
			</ul>
		</nav>
	</div>

	<!-- 부트스트랩 JS -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
		crossorigin="anonymous"></script>

	<!-- 추가 커스텀 JS 필요하면 여기에 -->

	<script>
		
		window.onload = function(){
			loadComments(1)
		}
		
		function loadComments(page){

			const postNum = "${post.postNum}";
			
			//게시글 번호에 맞는 댓글 정보 요청
			fetch(`/community-lists/api/comment-list?page=\${page}&postNum=\${postNum}`,{
				method: 'GET',
				headers:{
					'Content-Type' : 'application/json'
				},
				body:null
			})
			.then(response => response.json())
			.then(commentList=>{
				
				console.log("받아온 댓글 리스트:" , commentList);
				
				//댓글개수
				document.getElementById('commentCount').innerText = commentList.totalCount;
				
				renderCommentList(commentList.list);
				renderPaging(commentList);
			})

		}
		
		function renderCommentList(commentList){
			const commentTable = document.getElementById('commentList');
			let html = '';
			
			commentList.forEach(comment =>{
				html += `
					<div>
						<p>\${comment.userName} \${comment.commentContent}</p>
					</div>
				
				`;
			});
			
			commentTable.innerHTML = html;
		}
		
		function renderPaging(pageInfo){
			const paging = document.getElementById('paging');
	        let html = '';

	        // [이전] 버튼 (시작 페이지가 1보다 클 때만 활성화)
	        if (pageInfo.startPage > 1) {
	            html += `
		            <li>
		            	<a href="#" onclick="loadComments(\${pageInfo.startPage - 1}); return false;">이전</a>
		            </li>
		        `;
	        }

	        // 숫자 버튼 (1~5, 6~10)
	        for (let i = pageInfo.startPage; i <= pageInfo.endPage; i++) {
	            // 현재 페이지면 active 클래스 추가
	            let active = (i === pageInfo.currentPage) ? "active" : "";
	            html += `
		            <li>
		            	<a href="#" onclick="loadComments(\${i}); return false;">\${i}</a>
		            </li>
	            `;
	        }

	        // [다음] 버튼 (끝 페이지가 총 페이지수보다 작을 때만 활성화)
	        if (pageInfo.endPage < pageInfo.totalPages) {
	            html += `
		            <li>
		           		<a href="#" onclick="loadComments(\${pageInfo.endPage + 1}); return false;">다음</a>
		            </li>
	            `;
	        }

	        paging.innerHTML = html;
		}
		
	</script>
</body>

</html>