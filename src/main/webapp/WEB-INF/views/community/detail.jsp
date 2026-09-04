<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>${post.postTitle} - 상세조회</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
	crossorigin="anonymous">

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

.post-content {
	font-size: 1rem;
	line-height: 1.7;
	min-height: 180px;
	white-space: pre-wrap;
	word-break: break-all;
}

.custom-textarea {
	border: 1px solid #dee2e6;
	border-radius: 8px;
	font-size: 0.92rem;
	resize: none;
}

.custom-textarea:focus {
	border-color: #adb5bd;
	box-shadow: none;
}

.pagination .page-link {
	color: #495057;
	border: 1px solid #e9ecef;
	margin: 0 3px;
	border-radius: 8px !important;
	font-size: 0.85rem;
	padding: 5px 10px;
}

.pagination .page-item.active .page-link {
	background-color: #212529;
	border-color: #212529;
	color: #ffffff;
}

.pagination .page-link:hover {
	background-color: #e9ecef;
}

details summary {
	cursor: pointer;
	user-select: none;
}

</style>
</head>

<body>

	<jsp:include page="../common/navbar.jsp"></jsp:include>

	<div class="container my-5" style="max-width: 1080px;">
		
		<div class="mb-4">
			<a href="/community-lists" class="btn btn-outline-secondary btn-sm px-3 border-0" style="border-radius: 8px;">
				← 목록으로
			</a>
		</div>

		<div class="custom-card p-4 p-md-5 mb-4 shadow-sm">
			
			<div class="border-bottom pb-3 mb-4">
				<h3 class="fw-bold text-dark mb-3">${post.postTitle}</h3>
				<div class="d-flex flex-wrap align-items-center justify-content-between text-secondary small gap-2">
					<div class="d-flex align-items-center gap-2">
						<span class="fw-semibold text-dark">${post.userName}</span>
						<span>•</span>
						<span>${post.postedDateString}</span>
					</div>
					<div class="d-flex align-items-center gap-3">
						<span>조회 ${post.postViewCnt}</span>
						<span>추천 ${post.postLikeCnt}</span>
					</div>
				</div>
			</div>

			<details class="bg-light p-3 rounded-3 mb-4 border">
				<summary class="fw-semibold text-secondary small">첨부파일 보기</summary>
				<div class="mt-3 pt-2 border-top">
					<c:if test="${not empty post.fileList}">
						<div class="d-flex flex-column gap-3">
							<c:forEach var="fileName" items="${post.fileList}">
								<img src="/file-storage/post_uploadfile/${fileName}" class="img-fluid rounded border" style="max-width: 100%; height: auto;"/>
							</c:forEach>
						</div>
					</c:if>
					<c:if test="${empty post.fileList}">
						<p class="text-secondary small mb-0">첨부된 파일이 없습니다.</p>
					</c:if>
				</div>
			</details>

			<div class="post-content text-dark mb-4">
				${post.postContent}
			</div>

		</div>

		<div class="custom-card p-4 shadow-sm mb-4">
			
			<div class="border-bottom pb-2 mb-4">
				<h6 class="fw-bold text-dark mb-0">
					💬 댓글 <span class="text-secondary" id="commentCount">0</span>
				</h6>
			</div>

			<div class="mb-4">
				<div class="d-flex flex-column gap-2">
					<textarea id="commentContent" class="form-control custom-textarea" rows="3" placeholder="댓글을 입력하세요 (최대 100자)"></textarea>
					<div class="text-end">
						<button type="button" class="btn btn-dark btn-sm px-4 fw-medium" style="border-radius: 8px;" onclick="submitComment()">작성</button>
					</div>
				</div>
			</div>

			<div id="commentList" class="d-flex flex-column">
			</div>

			<nav aria-label="Page navigation" class="mt-4">
				<ul id="paging" class="pagination justify-content-center mb-0">
				</ul>
			</nav>

		</div>

	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
		crossorigin="anonymous"></script>

	<script>
		window.onload = function(){
			loadComments(1);
		}
		
		function loadComments(page){
			const postNum = "${post.postNum}";
			
			//게시글 번호에 맞는 댓글 정보 요청
			fetch(`/api/community-lists/comment-list?page=\${page}&postNum=\${postNum}`,{
				method: 'GET',
				headers:{
					'Content-Type' : 'application/json'
				}
			})
			.then(response => response.json())
			.then(commentList=>{
				if(commentList.code === "suc_002"){
					return;
				}
				if(commentList.code !== "suc_001") {
					alert("댓글 데이터를 불러오지 못했습니다.");
					return;
				}
				
				console.log("받아온 댓글 리스트:" , commentList.data);
				
				document.getElementById('commentCount').innerText = commentList.data.totalCount;
				
				renderCommentList(commentList.data.list);
				renderPaging(commentList.data);
			});
		}
		
		function renderCommentList(commentList){
			const commentTable = document.getElementById('commentList');
			let html = '';
			
			if(!commentList || commentList.length === 0){
				commentTable.innerHTML = '<div class="text-center py-4 text-secondary small">등록된 댓글이 없습니다.</div>';
				return;
			}
			
			commentList.forEach(comment =>{
				const dateStr = comment.commentedDateString || '';
				html += `
					<div class="py-3 border-bottom">
						<div class="d-flex align-items-center justify-content-between mb-1">
							<span class="fw-semibold text-dark small">\${comment.userName}</span>
							<span class="text-secondary small" style="font-size: 0.8rem;">\${dateStr}</span>
						</div>
						<p class="mb-0 text-dark small" style="white-space: pre-wrap; line-height: 1.5;">\${comment.commentContent}</p>
					</div>
				`;
			});
			
			commentTable.innerHTML = html;
		}
		
		function renderPaging(pageInfo){
			const paging = document.getElementById('paging');
	        let html = '';

	        if (pageInfo.startPage > 1) {
	            html += `
		            <li class="page-item">
		            	<a class="page-link" href="#" onclick="loadComments(\${pageInfo.startPage - 1}); return false;">‹</a>
		            </li>
		        `;
	        }

	        for (let i = pageInfo.startPage; i <= pageInfo.endPage; i++) {
	            let active = (i === pageInfo.currentPage) ? "active" : "";
	            html += `
		            <li class="page-item \${active}">
		            	<a class="page-link" href="#" onclick="loadComments(\${i}); return false;">\${i}</a>
		            </li>
	            `;
	        }

	        if (pageInfo.endPage < pageInfo.totalPages) {
	            html += `
		            <li class="page-item">
		           		<a class="page-link" href="#" onclick="loadComments(\${pageInfo.endPage + 1}); return false;">›</a>
		            </li>
	            `;
	        }

	        paging.innerHTML = html;
		}
		
		function submitComment(){
			const contentInput = document.getElementById('commentContent');
			const content = contentInput.value.trim();
			
		    if (content.length > 100) {
		        alert("댓글은 최대 100자까지만 작성할 수 있습니다.");
		        contentInput.focus();
		        return;
		    }
		    
			if(content == ""){
				alert("댓글 내용을 입력해주세요.");
				contentInput.focus();
				return;
			}
			
			const postNum = "${post.postNum}";
			const requestData = {
					postNum: postNum,
					commentContent: content
			};
			
			fetch(`/api/community-lists/comment-write`,{
				method : 'POST',
				headers:{
					'Content-Type' : 'application/json'
				},
				body: JSON.stringify(requestData)
			})
			.then(response =>response.json())
			.then(writeInfo=>{
				if(writeInfo.code === "suc_001"){
					contentInput.value = '';
					loadComments(1);
				} else{
					alert(writeInfo.message);
				}
			});
		}
	</script>
</body>

</html>