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
	<form action="/community-lists/write" method="post" onsubmit="postValidate(event)">
		<div>게시글카테고리 영역</div>
		<h4>제목</h4>
		<input type="text" placeholder="제목을 입력해주세요" id="title"name="title">
		<h4>내용</h4>
		<input type="text" placeholder="내용을 입력해주세요" id="text" name="text">
		<button type="submit">작성</button> 
		<a href="/community-lists"><button type="button">취소</button></a>
		
	</form>
	
	<script>
		
		//제목 및 내용 빈값 검증
		function postValidate(event) {	
		const titleInput = document.getElementById('title');
		const textInput = document.getElementById('text');
		
		const titleValue = titleInput.value.trim();
		const textValue = textInput.value.trim();
		
		
			if(!titleValue){
				event.preventDefault();
				alert("제목을 입력해주세요");
				return false;
			}
			
			if(!textValue){
				event.preventDefault();
				alert("내용을 입력해주세요");
				return false;
			}
		}
	</script>
</body>


</html>