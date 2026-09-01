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
    max-height: 80vh;
    overflow-y: auto;

    margin: 50px auto;
    padding: 20px;

    background-color: white;
    border: 1px solid #ccc;
}

.profile-modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}


.profile-photo-edit {
    display: flex;
    align-items: center;
    gap: 20px;

    padding: 20px 0;
    border-bottom: 1px solid #ccc;
}

.profile-photo-preview img {
    border-radius: 50%;
}

.profile-photo-buttons p {
    margin: 5px 0;
}



/* 닉네임 수정 */
.profile-nick-edit {
    padding: 20px 0;
    border-bottom: 1px solid #ccc;
}

.profile-nick-edit p {
    margin: 5px 0;
}

/* 비밀번호 변경 */
.profile-pw-edit {
    padding: 20px 0;
    border-bottom: 1px solid #ccc;
}

#btnPwToggle {
    width: 100%;
    text-align: left;
}


/* 포트폴리오 */
.profile-public-edit {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 0;
    border-bottom: 1px solid #ccc;
}

.profile-public-edit p {
    margin: 5px 0;
}

.switch {
    position: relative;
    display: inline-block;
    width: 50px;
    height: 26px;
}

.switch input {
    display: none;
}

.slider {
    position: absolute;
    cursor: pointer;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: gray;
    border-radius: 26px;
}

.slider:before {
    position: absolute;
    content: "";
    height: 20px;
    width: 20px;
    left: 3px;
    bottom: 3px;
    background-color: white;
    border-radius: 50%;
    transition: 0.2s;
}

.switch input:checked + .slider {
    background-color: black;
}

.switch input:checked + .slider:before {
    transform: translateX(24px);
}

.profile-delete {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 0;
    border-bottom: 1px solid black;
}

.profile-delete p {
    margin: 5px 0;
}

#btnUserDelete {
    color: red;
}

.profile-modal-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
    padding-top: 20px;
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

			<strong>개미승민</strong><!-- 나중에 없애기  -->
			
			<div>가입일 : 2026.09.01</div>

			<button type="button" id="btnProfileEdit">프로필 수정</button>

		</div>

	</div>
	
	
	<!-- 프로필 수정 모달 -->
	<div id="profileModal" class="profile-modal">

		<div class="profile-modal-content">

			<div class="profile-modal-header">
				<h2>프로필 수정</h2>

				<button type="button" id="btnProfileClose">★</button>
			</div>

			<div class="profile-modal-body">

				<!-- 프로필 사진 수정 -->
				<div class="profile-photo-edit">

					<div class="profile-photo-preview">
						<img src="/resources/images/logo.png" alt="프로필 사진" id="profilePreview" width="80" height="80">
					</div>

					<div class="profile-photo-buttons">

						<p>
							<strong>프로필 사진</strong>
						</p>

						<p>JPG, PNG JPEG의 사진만 선택이 가능합니다.</p>

						<input type="file" id="profilePhotoInput" accept=".png,.jpg,.jpeg" style="display: none;">

						<button type="button" id="btnPhotoChange">사진 변경</button>

						<button type="button" id="btnDefaultPhoto">기본 이미지</button>

					</div>

				</div>
				<!-- 닉네임 변경 -->
				<div class="profile-nick-edit">

					<p>
						<strong>닉네임</strong>
					</p>

					<input type="text" name="userNick" id="inputProfileNick"
						value="개미승민"><%-- 	value="${userProfile.userNick}" --%>

					<button type="button" id="btnProfileNickCheck">중복확인</button>

					<p id="profileNickCheckMsg"></p>

				</div>

				<!-- 비밀번호 변경 -->
				<div class="profile-pw-edit">

					<button type="button" id="btnPwToggle">비밀번호 변경 ▼</button>

					<div id="pwChangeArea" style="display: none;">

						<p>
							<strong>현재 비밀번호</strong>
						</p>
						<input type="password" name="currentPw" id="currentPw">

						<p>
							<strong>새 비밀번호</strong>
						</p>
						<input type="password" name="newPw" id="newPw">

						<p>
							<strong>새 비밀번호 확인</strong>
						</p>
						<input type="password" name="newPwCheck" id="newPwCheck">

					</div>


				</div>

				<!-- 포트폴리오 공개 여부 -->
				<div class="profile-public-edit">

					<div>
						<strong>포트폴리오 공개</strong>
						<p>다른 사용자에게 내 포트폴리오를 공개합니다.</p>
					</div>

					<label class="switch"> <input type="checkbox"
						id="portfolioPublic" name="userPortfolioIsPublic" value="1">
						<span class="slider"></span>
					</label>

				</div>

				<!-- 회원 탈퇴 -->
				<div class="profile-delete">

					<div>
						<strong>회원 탈퇴</strong>
						<p>탈퇴 시 계정 정보를 복구할 수 없습니다.</p>
					</div>

					<button type="button" id="btnUserDelete">탈퇴하기</button>

				</div>
				
				<!-- 모달 하단 버튼(변경사항 저장 or 취소) -->
				<div class="profile-modal-footer">

					<button type="button" id="btnProfileCancel">취소</button>

					<button type="button" id="btnProfileSave">변경사항 저장</button>

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
    
    
    const btnPhotoChange = document.getElementById("btnPhotoChange");
    const profilePhotoInput = document.getElementById("profilePhotoInput");
    const profilePreview = document.getElementById("profilePreview");
    
    // 사진 변경 버튼 클릭
    btnPhotoChange.addEventListener("click", ()=>{

        profilePhotoInput.click();

    });
    
    

    profilePhotoInput.addEventListener("change", ()=>{

        const file = profilePhotoInput.files[0];

        if(file == null){
            return;
        }
        
        // 프로필 사진 확장자 확인(png, jpg, jpeg만 가능)
        const fileName = file.name.toLowerCase();

        if(!fileName.endsWith(".png")
                && !fileName.endsWith(".jpg")
                && !fileName.endsWith(".jpeg")){

            alert("PNG, JPG, JPEG 파일만 사용할 수 있습니다.");

            profilePhotoInput.value = "";

            return;
        }

        
		//선택한 프로필 사진 미리보기
        const reader = new FileReader();

        reader.onload = (e)=>{
            profilePreview.src = e.target.result;
        };

        reader.readAsDataURL(file);

    });
    
    
 // 닉네임 중복 확인
    let profileNickCheck = false;

    const inputProfileNick = document.getElementById("inputProfileNick");
    const btnProfileNickCheck = document.getElementById("btnProfileNickCheck");
    const profileNickCheckMsg = document.getElementById("profileNickCheckMsg");

    btnProfileNickCheck.addEventListener("click", ()=>{

        const userNick = inputProfileNick.value;

        if(userNick.trim() == ""){
            profileNickCheckMsg.innerText = "닉네임을 입력해주세요.";
            return;
        }

        fetch("/api/checkNick", {
            method: "POST",
            headers: {
                "Content-Type": "text/plain"
            },
            body: userNick
        })
        .then(response => response.json())
        .then(result => {

            if(result.code == "rej_102"){

                profileNickCheck = false;
                profileNickCheckMsg.innerText = result.message;

            }else if(result.code == "suc_001"){

                profileNickCheck = true;
                profileNickCheckMsg.innerText = "사용 가능한 닉네임입니다.";

            }

        });

    });

    // 닉네임 수정 시 중복확인 초기화 + 공백 제거
    inputProfileNick.addEventListener("input", ()=>{

        inputProfileNick.value = inputProfileNick.value.replace(/\s/g, "");

        profileNickCheck = false;
        profileNickCheckMsg.innerText = "";

    });
	
	//비밀번호 변경
	const currentPw = document.getElementById("currentPw");
	const newPw = document.getElementById("newPw");
	const newPwCheck = document.getElementById("newPwCheck");

	
	const btnPwToggle = document.getElementById("btnPwToggle");
	const pwChangeArea = document.getElementById("pwChangeArea");
	
	// 현재 비밀번호 공백 입력 방지
	currentPw.addEventListener("input", ()=>{
	    currentPw.value = currentPw.value.replace(/\s/g, "");
	});
	
	// 새 비밀번호 공백 입력 방지
	newPw.addEventListener("input", ()=>{
	    newPw.value = newPw.value.replace(/\s/g, "");
	});
	
	// 새 비밀번호 확인 공백 입력 방지
	newPwCheck.addEventListener("input", ()=>{
	    newPwCheck.value = newPwCheck.value.replace(/\s/g, "");
	});
	
	
	//비밀번호 영역 열기/닫기
	btnPwToggle.addEventListener("click", ()=>{

	    if(pwChangeArea.style.display == "none"){

	        pwChangeArea.style.display = "block";
	        btnPwToggle.innerText = "비밀번호 변경 ▲";

	    }else{

	        pwChangeArea.style.display = "none";
	        btnPwToggle.innerText = "비밀번호 변경 ▼";

	    }

	});
	
	
		// 비밀번호 검증
		const btnProfileSave = document.getElementById("btnProfileSave");
		
		btnProfileSave.addEventListener("click", ()=>{
		
		    // 비밀번호 중 하나라도 입력했을 경우
		    if(currentPw.value != ""
		            || newPw.value != ""
		            || newPwCheck.value != ""){
		
		        if(currentPw.value == ""){
		            alert("현재 비밀번호를 입력해주세요.");
		            return;
		        }
		
		        if(newPw.value == ""){
		            alert("새 비밀번호를 입력해주세요.");
		            return;
		        }
		
		        if(newPwCheck.value == ""){
		            alert("새 비밀번호 확인을 입력해주세요.");
		            return;
		        }
		
		        if(newPw.value != newPwCheck.value){
		            alert("새 비밀번호가 일치하지 않습니다.");
		            return;
		        }
		
		    }
		
		    alert("검증 통과");
		
		});
			
	
	//취소
	const btnProfileCancel = document.getElementById("btnProfileCancel");

	btnProfileCancel.addEventListener("click", ()=>{

	    profileModal.style.display = "none";

	});
    

</script>
</body>

</html>