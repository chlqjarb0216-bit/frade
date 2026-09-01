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

<title>샘플 JSP 페이지 양식</title>
	<style>
.profile-box {
	display: flex;
	align-items: center;
	gap: 20px;
	width: 450px;
	padding: 25px;
	border: 1px solid #ccc;
}

.profile-photo img {
	border-radius: 50%;
}

.profile-info {
	display: flex;
	flex-direction: column;
	gap: 10px;
}



/* 프로필 수정 모달 */
.profile-modal {
    display: none;

    position: fixed;
    top: 0;
    left: 0;

    width: 100%;
    height: 100%;

    background-color: rgba(0, 0, 0, 0.4);
}

.profile-modal-content {
    width: 500px;

    margin: 100px auto;
    padding: 20px;

    background-color: white;
    border: 1px solid #ccc;
}

.profile-modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}




</style>

</head>

<body>

	<div class="profile-box">

		<div class="profile-photo">
			<img src="/resources/images/logo.png" alt="프로필 사진" width="70" height="70">
		</div>

		<div class="profile-info">

			<div>
				<strong>${userProfile.userNick}</strong>
			</div>

			<strong>개?승민 (아이언)</strong>
			
			<div>가입일 : 2026.09.01</div>

			<button type="button" id="btnProfileEdit">프로필 수정</button>

		</div>

	</div>
	
	
	<!-- 프로필 수정 모달 -->
	<div id="profileModal" class="profile-modal">

		<div class="profile-modal-content">

			<div class="profile-modal-header">
				<h2>프로필 수정</h2>

				<button type="button" id="btnProfileClose">X</button>
			</div>

			<div class="profile-modal-body">프로필 수정 내용 들어갈 자리</div>

		</div>

	</div>



	<script>

    const btnProfileEdit = document.getElementById("btnProfileEdit");
    const btnProfileClose = document.getElementById("btnProfileClose");
    const profileModal = document.getElementById("profileModal");

    // 프로필 수정 모달 열기
    btnProfileEdit.addEventListener("click", ()=>{

        profileModal.style.display = "block";

    });

    // 프로필 수정 모달 닫기
    btnProfileClose.addEventListener("click", ()=>{

        profileModal.style.display = "none";

    });

</script>




</body>

</html>