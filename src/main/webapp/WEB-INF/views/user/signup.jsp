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

<title>회원가입 페이지</title>

</head>
<body>
	<h1>회원가입</h1>

	<form action="" method="post" id="signupForm">

		아이디 : <input type="text" name="uId" id="inputId" value="${uId}" required >
		<button type="button" id="btn_checkDupId">중복확인</button>

		<p id="checkDupIdMsg"></p>
		<br>비밀번호 : <input type="password" name="uPw" id="inputPw"required>
		<br> 비밀번호 확인 : <input type="password" name="uPwCheck" id="inputPwCheck" required> 
		<br>

		<p id="checkPwMsg"></p>

		닉네임 : <input type="text" name="uNick" id="inputNick" value="${uNick}" required>
		<button type="button" id="btn_checkDupNick">중복확인</button>

		<p id="checkDupNickMsg"></p>

		<br> 이메일 : <input type="email" name="uEmail" id="inputEmail" value="${uEmail}" required> 
		<button type="button" id="btn_checkDupEmail">중복확인</button>
		
		<p id="checkDupEmailMsg"></p>
		
		<br> <br>

		<button type="submit">회원가입</button>

	</form>
	
	<c:if test="${not empty signupFail}">
		<p class="error-msg">${signupFail}</p>
	</c:if>

	<c:if test="${not empty emptyFail}">
		<p class="error-msg">${emptyFail}</p>
	</c:if>





	<br>
	<a href="/user/login">돌아가기</a>

	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/jquery/4.0.0/jquery.min.js"
		integrity="sha512-8LENNbXmzI/Gbj+OwXmqR6V4QaUAw0/porPzy1+dQoJqC0JPHedWoe0DDOTL2uHA5XXJyIsPtiMHH86pVlay6A=="
		crossorigin="anonymous" referrerpolicy="no-referrer">
</script>

	<script>
	
	let idCheck = false;
	let nickCheck = false;
	let emailCheck = false;
	
	    // =========================
	    // 아이디 중복 확인
	    // =========================
	
	   const btn_checkDupId = document.getElementById("btn_checkDupId");
		const p_checkDupIdMsg = document.getElementById("checkDupIdMsg");
		const inputId = document.getElementById("inputId");
		
		
		// 아이디 공백 입력 방지
		inputId.addEventListener("input", ()=>{
		
		    inputId.value = inputId.value.replace(/\s/g, "");
		
		});
		
		
		// 아이디 중복 확인
		btn_checkDupId.addEventListener("click", ()=>{
		
		    let inputIdValue = inputId.value;
		    
		    if(inputIdValue == ""){

		        p_checkDupIdMsg.textContent = "아이디를 입력해주세요.";
		        p_checkDupIdMsg.style.color = "red";

		        return;
		    }
		
		    $.ajax({
		
		        type: "POST",
		
		        url: "/user/checkId",
		
		        headers : {
		            "Content-type":"application/json"
		        },
		
		        data: inputIdValue,
		
		        dataType: "text",
		
		        success: function(result){
		
		            console.log("아이디 중복확인 성공");
		            console.log(result);
		
		            if(result == "Y"){
		
		                p_checkDupIdMsg.textContent = "중복된 아이디 입니다.";
		                p_checkDupIdMsg.style.color = "red";
		
		            }else{
		
		                p_checkDupIdMsg.textContent = "사용 가능한 아이디 입니다.";
		                p_checkDupIdMsg.style.color = "green";
		
		                inputId.readOnly = true;
		
		                idCheck = true;
		
		            }
		
		        },
		
		        error: function(error){
		
		            console.log(error);
		
		        }
		
		    });
		
		});
	
	
	
	    // =========================
	    // 닉네임 중복 확인
	    // =========================
	
	    const btn_checkDupNick = document.getElementById("btn_checkDupNick");
	    const p_checkDupNickMsg = document.getElementById("checkDupNickMsg");
	    const inputNick = document.getElementById("inputNick");

	 	// 닉네임 공백 입력 방지
		 inputNick.addEventListener("input", ()=>{

	     inputNick.value = inputNick.value.replace(/\s/g, "");

	 	});
	
	    btn_checkDupNick.addEventListener("click", ()=>{
	
	    	let inputNickValue = inputNick.value;
	    	
	    	if(inputNickValue == ""){

	            p_checkDupNickMsg.textContent = "닉네임을 입력해주세요.";
	            p_checkDupNickMsg.style.color = "red";

	            return;
	        }
	
	        // 닉네임에 공백이 있으면 중복확인 요청 안 보냄
	        if(/\s/.test(inputNickValue)){
	
	            p_checkDupNickMsg.textContent = "닉네임에는 공백을 사용할 수 없습니다.";
	            p_checkDupNickMsg.style.color = "red";
	
	            return;
	        }
	
	        $.ajax({
	
	            type: "POST",
	
	            url: "/user/checkNick",
	
	            headers : {
	                "Content-type":"application/json"
	            },
	
	            data: inputNickValue,
	
	            dataType: "text",
	
	            success: function(result){
	
	                console.log("닉네임 중복확인 성공");
	                console.log(result);
	
	                if(result == "Y"){
	
	                    p_checkDupNickMsg.textContent = "중복된 닉네임 입니다.";
	                    p_checkDupNickMsg.style.color = "red";
	
	                }else{
	
	                    p_checkDupNickMsg.textContent = "사용 가능한 닉네임 입니다.";
	                    p_checkDupNickMsg.style.color = "green";
	                    
	                    document.getElementById("inputNick").readOnly = true;
	                    
	                    nickCheck = true;
	
	                }
	
	            },
	
	            error: function(error){
	
	                console.log(error);
	
	            }
	
	        });
	
	    });
	    
	    
	    // =========================
	    // 이메일 중복 확인
	    // =========================
	

		const btn_checkDupEmail = document.getElementById("btn_checkDupEmail");
		const p_checkDupEmailMsg = document.getElementById("checkDupEmailMsg");
		const inputEmail = document.getElementById("inputEmail");
		
		
		// 이메일 공백 입력 방지
		inputEmail.addEventListener("input", ()=>{
		
		    inputEmail.value = inputEmail.value.replace(/\s/g, "");
		
		});
		
		
		btn_checkDupEmail.addEventListener("click", ()=>{
		
		    let inputEmailValue = inputEmail.value;
		
		    // 이메일 형식 확인
		    if(!inputEmail.checkValidity()){
		
		        p_checkDupEmailMsg.textContent = "올바른 이메일 형식으로 입력해주세요.";
		        p_checkDupEmailMsg.style.color = "red";
		
		        return;
		    }
		
		    // 공백 확인
		    if(/\s/.test(inputEmailValue)){
		
		        p_checkDupEmailMsg.textContent = "이메일에는 공백을 사용할 수 없습니다.";
		        p_checkDupEmailMsg.style.color = "red";
		
		        return;
		    }
		
		    $.ajax({
		
		        type: "POST",
		
		        url: "/user/checkEmail",
		
		        headers : {
		            "Content-type":"application/json"
		        },
		
		        data: inputEmailValue,
		
		        dataType: "text",
		
		        success: function(result){
		
		            if(result == "Y"){
		
		                p_checkDupEmailMsg.textContent = "중복된 이메일 입니다.";
		                p_checkDupEmailMsg.style.color = "red";
		
		            }else{
		
		                p_checkDupEmailMsg.textContent = "사용 가능한 이메일 입니다.";
		                p_checkDupEmailMsg.style.color = "green";
		
		                inputEmail.readOnly = true;
		                emailCheck = true;
		
		            }
		
		        },
		
		        error: function(error){
		
		            console.log(error);
		
		        }
		
		    });
		
		});
	
	
	
	    // =========================
	    // 비밀번호 일치 확인
	    // =========================
	    const inputPw = document.getElementById("inputPw");
		const inputPwCheck = document.getElementById("inputPwCheck");
		const p_checkPwMsg = document.getElementById("checkPwMsg");
		
		
		// 비밀번호 공백 입력 방지
		inputPw.addEventListener("input", ()=>{
		
		    inputPw.value = inputPw.value.replace(/\s/g, "");
		
		});
		
		
		// 비밀번호 확인 공백 입력 방지
		inputPwCheck.addEventListener("input", ()=>{
		
		    inputPwCheck.value = inputPwCheck.value.replace(/\s/g, "");
		
		});
		
		
		// 비밀번호 일치 확인
		inputPwCheck.addEventListener("keyup", ()=>{
		
		    if(inputPwCheck.value == ""){
		
		        p_checkPwMsg.textContent = "";
		
		        return;
		    }
		
		    if(inputPw.value == inputPwCheck.value){
		
		        p_checkPwMsg.textContent = "비밀번호가 일치합니다.";
		        p_checkPwMsg.style.color = "green";
		
		    }else{
		
		        p_checkPwMsg.textContent = "비밀번호가 일치하지 않습니다.";
		        p_checkPwMsg.style.color = "red";
		
		    }
		
		});
	    
	    
	    //회원가입 버튼을 눌렀을때 세개 다 중복 확인을 했는지 검사
	    
	    const signupForm = document.getElementById("signupForm");

	    signupForm.addEventListener("submit", (e)=>{

	        if(idCheck == false){

	            e.preventDefault();

	            alert("아이디 중복확인을 해주세요.");

	            return;
	        }

	        if(nickCheck == false){

	            e.preventDefault();

	            alert("닉네임 중복확인을 해주세요.");

	            return;
	        }

	        if(emailCheck == false){

	            e.preventDefault();

	            alert("이메일 중복확인을 해주세요.");

	            return;
	        }

	    });
	    
	
	</script>
	
	




</body>
</html>