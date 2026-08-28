<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>글쓰기</h1>
	<h2>게시글 작성</h2>
	<a href="/community-lists"><button type="button"><-커뮤니티로</button></a>
	
	<form action="/community-lists/write" method="post" enctype="multipart/form-data" onsubmit="postValidate(event)">
		<div>
			<label for="pCategoryNum">카테고리 선택:</label><br>
		    <label>
		        <input type="radio" name="pCategoryNum" value="0" > 질문
		    </label>
		    <label>
		        <input type="radio" name="pCategoryNum" value="1"> 정보
		    </label>
		    <label>
		        <input type="radio" name="pCategoryNum" value="2"> 자유
		    </label>
		    <br>
		</div>
		
		<h4>제목</h4>
		<input type="text" placeholder="제목을 입력해주세요" id="pTitle"name="pTitle">
		<h4>내용</h4>
		<input type="text" placeholder="내용을 입력해주세요" id="pContent" name="pContent">
		<h4>첨부파일</h4>
		<input type="file" id="pFiles" name="pFiles" multiple><br>
		
		<button type="submit">작성</button> 
		<a href="/community-lists"><button type="button">취소</button></a>
		
	</form>
	
	<script>
		
		// 카테고리,제목,내용 공백 및 파일크기 검증
		function postValidate(event) {	
			
		const titleInput = document.getElementById('pTitle');
		const contentInput = document.getElementById('pContent');
		const categoryNumInput = document.querySelector('input[name="pCategoryNum"]:checked');
		
		const fileInput = document.getElementById('pFiles');
		const files = fileInput.files;
		
		const titleValue = titleInput.value.trim();
		const contentValue = contentInput.value.trim();
		
			if(!categoryNumInput){
				event.preventDefault();
				alert("카테고리를 선택해주세요");
				return false;
			}
		
			if(!titleValue){
				event.preventDefault();
				alert("제목을 입력해주세요");
				return false;
			}
			
			if(!contentValue){
				event.preventDefault();
				alert("내용을 입력해주세요");
				return false;
			}
			
			// 1. 파일 개수 제한 (최대 3개)
			if (files.length > 3) {
			    event.preventDefault();
			    alert("첨부파일은 최대 3개까지만 업로드할 수 있습니다.");
			    return false;
			}

			// 2. 개별 파일 용량 제한 (각 10MB)
			const maxSize = 10 * 1024 * 1024; // 10MB를 Byte 단위로 변환

			for (let i = 0; i < files.length; i++) {
			    if (files[i].size > maxSize) {
			        event.preventDefault();
			        alert(`[${files[i].name}] 파일의 크기가 10MB를 초과합니다.`);
			        return false;
			    }
			}
		}
	</script>
</body>


</html>