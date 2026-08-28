<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	
	<!-- 스마트폰 등의 환경에서 원래 크기로 보이도록 -->
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	
	<title>로그인 페이지</title>
	
	<style>
	.error-msg {
		color:red;
	}
</style>
</head>
<body>

	<form action="" method="post">

    아이디 : <input type="text" name="uId"><br>

    비번 : <input type="password" name="uPw"><br>

    <br>

    <button type="submit">로그인</button>
    <c:if test="${loginfail == 'true'}">
    	<p class="error-msg">로그인 실패</p>
    </c:if>
    
    
    
     <div>
        <a href="/user/signup">회원가입</a>
    </div>

</form>

</body>
</html>