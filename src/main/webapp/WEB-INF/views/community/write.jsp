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

.custom-form-control {
	border: 1px solid #dee2e6;
	border-radius: 8px;
	font-size: 0.93rem;
	padding: 10px 14px;
}

.custom-form-control:focus {
	border-color: #adb5bd;
	box-shadow: none;
}

textarea.custom-form-control {
	resize: vertical;
	min-height: 240px;
	line-height: 1.6;
}

.form-label {
	font-weight: 600;
	font-size: 0.875rem;
	color: #495057;
	margin-bottom: 8px;
}

.btn-group .btn-outline-secondary {
	border-color: #dee2e6;
	color: #495057;
}

.btn-check:checked + .btn-outline-secondary {
	background-color: #212529;
	border-color: #212529;
	color: #ffffff;
}

btn-outline-secondary:hover {
	color:#f1f5f9;
}
</style>
</head>
<body>

	<!-- 네비게이션바 include -->
	<jsp:include page="../common/navbar.jsp"></jsp:include>

	<!-- 본문 영역 -->
	<div class="container my-5" style="max-width: 1080px;">
		
		<div class="mb-4">
			<a href="/community-lists" class="btn btn-outline-secondary btn-sm px-3 mb-3 border-0" style="border-radius: 8px;">← 커뮤니티로</a>
			<h3 class="fw-bold text-dark mb-1"> 게시글 작성</h3>
			<p class="text-secondary small mb-0">투자 아이디어, 질문, 정보를 자유롭게 나눠보세요.</p>
			<hr class="mt-3 mb-4 text-muted opacity-25">
		</div>

		<div class="custom-card p-4 p-md-5 shadow-sm">
			<form id="postForm" action="/community-lists/write" method="post" enctype="multipart/form-data" onsubmit="postValidate(event)">
				
				<div class="mb-4">
					<label class="form-label d-block">카테고리</label>
					<div class="btn-group btn-group-sm" role="group" aria-label="카테고리 선택">
						<input type="radio" class="btn-check" name="postCategoryNum" id="catQ" value="0">
						<label class="btn btn-outline-secondary px-3 py-2" for="catQ">질문</label>

						<input type="radio" class="btn-check" name="postCategoryNum" id="catI" value="1">
						<label class="btn btn-outline-secondary px-3 py-2" for="catI">정보</label>

						<input type="radio" class="btn-check" name="postCategoryNum" id="catF" value="2">
						<label class="btn btn-outline-secondary px-3 py-2" for="catF">자유</label>
					</div>
				</div>
				
				<div class="mb-4">
					<label for="postTitle" class="form-label">제목</label>
					<input type="text" class="form-control custom-form-control" placeholder="제목을 입력해주세요 (최대 한글 30자)" id="postTitle" name="postTitle">
				</div>
				
				<div class="mb-4">
					<label for="postContent" class="form-label">내용</label>
					<textarea class="form-control custom-form-control" placeholder="내용을 입력해주세요" id="postContent" name="postContent" rows="10"></textarea>
				</div>
				
				<div class="mb-4">
					<label for="uploadFiles" class="form-label">첨부파일</label>
					<input class="form-control custom-form-control" type="file" id="uploadFiles" name="uploadFiles" multiple>
					<div class="text-secondary small mt-2" style="font-size: 0.8rem;">
						* 파일은 최대 3개, 개당 10MB 이하로 첨부 가능합니다.
					</div>
				</div>
				
				<div class="d-flex align-items-center justify-content-end gap-2 pt-3 border-top">
					<a href="/community-lists" class="btn btn-outline-secondary btn-sm px-4 py-2" style="border-radius: 8px;">취소</a>
					<button type="submit" class="btn btn-dark btn-sm px-4 py-2 fw-medium" style="border-radius: 8px;">작성</button> 
				</div>
				
			</form>
		</div>
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