<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>커뮤니티 게시글 작성</title>

<!-- 부트스트랩 CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">

</head>
<body>

	<!-- 네비게이션바 include -->
	<jsp:include page="../common/navbar.jsp"></jsp:include>

	<!-- 본문 영역 -->
	<div class="container mt-5">
		
		<div>
			<h2>게시글 작성</h2>
			<a href="/community-lists" class="btn btn-outline-secondary"><- 커뮤니티로</a>
		</div>

		<form id="postForm" action="/community-lists/write" method="post" enctype="multipart/form-data" onsubmit="postValidate(event)">
			
			<div>
				<label >카테고리 선택:</label><br>
				<div >
					<input type="radio" name="postCategoryNum" id="catQ" value="0">
					<label for="catQ">질문</label>
				</div>
				<div >
					<input type="radio" name="postCategoryNum" id="catI" value="1">
					<label  for="catI">정보</label>
				</div>
				<div>
					<input type="radio" name="postCategoryNum" id="catF" value="2">
					<label for="catF">자유</label>
				</div>
			</div>
			
			<div >
				<label for="postTitle" >제목</label>
				<input type="text"  placeholder="제목을 입력해주세요" id="postTitle" name="postTitle">
			</div>
			
			<div>
				<label for="postContent" >내용</label>
				<textarea placeholder="내용을 입력해주세요" id="postContent" name="postContent" rows="10"></textarea>
			</div>
			
			<div>
				<label for="uploadFiles" >첨부파일</label>
				<input type="file" id="uploadFiles" name="uploadFiles" multiple>
			</div>
			
			<div >
				<button type="submit">작성</button> 
				<a href="/community-lists">취소</a>
			</div>
			
		</form>
	</div>

	<!-- 부트스트랩 JS -->
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>

	<!-- 폼 유효성 검증 JS -->
	<script>
	
		//서버 오류로 인해 게시글이 저장되지 않았을때 안내문
		const serverMsg = "${msg}";
		
	    if (serverMsg !== "") {
	        alert(serverMsg);
	    }
		
		// 카테고리,제목,내용 공백 및 파일크기 검증
		function postValidate(event) {	
			event.preventDefault();
			
			const titleInput = document.getElementById('postTitle');
			const contentInput = document.getElementById('postContent');
			const categoryNumInput = document.querySelector('input[name="postCategoryNum"]:checked');
			const fileInput = document.getElementById('uploadFiles');
			
			
			const files = fileInput.files;
			const titleValue = titleInput.value.trim();
			const contentValue = contentInput.value.trim();
			
			if(!categoryNumInput){
				alert("카테고리를 선택해주세요");
				return false;
			}
		
			if(!titleValue){
				alert("제목을 입력해주세요");
				return false;
			}
			
			if(!contentValue){
				alert("내용을 입력해주세요");
				return false;
			}
			
			// TextEncoder를 이용해 실제 UTF-8 바이트 계산
		    const encoder = new TextEncoder();
		    const titleByteSize = encoder.encode(titleValue).length;
		    const contentByteSize = encoder.encode(contentValue).length;

		    // 제목 바이트 검증 (90 Byte 제한: 한글 약 30자)
		    if (titleByteSize > 90) {
		        alert('제목이 너무 깁니다. 한글 기준 약 30자 이내로 작성해주세요. (최대 90바이트 / 현재'+ titleByteSize +'바이트)');
		        titleInput.focus();
		        return;
		    }

		    // 내용 바이트 검증 (4000 Byte 제한: 한글 약 1333자)
		    if (contentByteSize > 4000) {
		        alert('내용이 너무 깁니다. 한글 기준 약 1333자 이내로 작성해주세요 (최대 4000바이트 / 현재'+contentByteSize+'바이트)');
		        contentInput.focus();
		        return;
		    }
			
			// 파일 개수 제한 (최대 3개)
			if (files.length > 3) {
			    alert('첨부파일은 최대 3개까지만 업로드할 수 있습니다.');
			    return false;
			}

			// 개별 파일 용량 제한 (각 10MB)
			const maxSize = 10 * 1024 * 1024; // 10MB를 Byte 단위로 변환

			for (let i = 0; i < files.length; i++) {
			    if (files[i].size > maxSize) {
			        alert('[' + files[i].name + '] 파일의 크기가 10MB를 초과합니다.');
			        return false;
			    }
			}
			//  검증 완료 => 폼 전송.
		    document.getElementById('postForm').submit();
		}
	</script>
</body>
</html>