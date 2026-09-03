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
			<!-- <details> 태그로 감싸면 기본적으로 접혀있는 상태가 됩니다. -->
			<details>

				<!-- <summary> 태그 안의 내용이 클릭할 수 있는 버튼(제목) 역할이 됩니다. -->
				<summary style="width: max-content;">첨부파일 보기</summary>

				<!-- 버튼을 클릭해 펼쳐졌을 때 보여질 내용 -->
				<div>
					<c:if test="${not empty post.fileList}">
						<ul>
							<c:forEach var="fileName" items="${post.fileList}">
								<li><a href="${path}/${fileName}">${fileName}</a></li>
							</c:forEach>
						</ul>
					</c:if>

					<c:if test="${empty post.fileList}">
						<p>첨부된 파일이 없습니다.</p>
					</c:if>
				</div>

			</details>
		</div>
		<div>
			<p>${post.postContent}</p>
		</div>

		<!-- 댓글작성영역 -->
		<div>
			<div>
				<textarea id="commentContent" placeholder="댓글을 입력하세요"></textarea>
				<button type="button" onclick="submitComment()">작성</button>
			</div>
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
			loadComments(1);
		}
		
		function loadComments(page){
			const postNum = "${post.postNum}";
			
			//게시글 번호에 맞는 댓글 정보 요청
			fetch(`/community-lists/api/comment-list?page=\${page}&postNum=\${postNum}`,{
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
				
				//댓글개수
				document.getElementById('commentCount').innerText = commentList.data.totalCount;
				
				renderCommentList(commentList.data.list);
				renderPaging(commentList.data);
			});
		}
		
		function renderCommentList(commentList){
			const commentTable = document.getElementById('commentList');
			let html = '';
			
			if(!commentList || commentList.length === 0){
				commentTable.innerHTML = '<p class="text-muted">등록된 댓글이 없습니다.</p>';
				return;
			}
			
			commentList.forEach(comment =>{
				const dateStr = comment.commentedDateString || '';
				html += `
					<div class="mb-2 p-2 border-bottom">
						<p class="mb-1"><strong>\${comment.userName}</strong> <small class="text-muted">\${dateStr}</small></p>
						<p class="mb-0">\${comment.commentContent}</p>
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
		
		function submitComment(){
			const contentInput = document.getElementById('commentContent');
			const content = contentInput.value.trim();
			
		    if (content.length > 100) {
		        alert("댓글은 최대 100자까지만 작성할 수 있습니다.");
		        commentInput.focus();
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
			
			fetch(`/community-lists/api/comment-write`,{
				method : 'POST',
				headers:{
					'Content-Type' : 'application/json'
				},
				body: JSON.stringify(requestData)
			})
			.then(response =>response.json())
			.then(writeInfo=>{
				if(writeInfo.code === "suc_001"){
					//입력창 비우기
					contentInput.value = '';
					
					//1번 페이지 새로고침
					loadComments(1);
				} else{
					alert(writeInfo.message);
				}
			});
		}
		
	</script>
</body>

</html>