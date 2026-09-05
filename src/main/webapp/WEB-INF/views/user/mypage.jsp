<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>샘플 JSP 페이지 양식</title>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<style>
body {
	padding: 20px;
}

.mypageHeader {
	display: flex;
}

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

.switch input:checked+.slider {
	background-color: black;
}

.switch input:checked+.slider:before {
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

.totalAsset-box {
	border: 1px solid lightgray;
	display: flex;
	flex-direction: row;
	flex-wrap: wrap;
	width: 75vh;
	height: 169px;
	padding: 20px;
	margin-left: 20px;
}

.assetInfo-box-top {
	display: flex;
	flex-direction: row;
	width: 70%;
	justify-content: space-around;
	height: 100px;
}

.assetInfo-box-top div {
	height: 100px;
	margin-left: 50px;
}

.assetInfo-box-bottom {
	width: 300px;
	display: flex;
	justify-content: space-around;
	margin-left: 190px;
}

.mypageMiddle {
	display: flex;
	margin-top: 50px;
}

.portfolio-circle {
	width: 300px;
	height: 300px;
	border: 1px solid lightgray;
}

.portfolio-Structure-box {
	margin-left: 20px;
	border: 1px solid lightgray;
	width: 300px;
	padding: 10px;
}

.history-box {
    margin-left: 20px;
    width: 600px;
    height: 300px;
    overflow-y: auto;
    border: 1px solid lightgray;
    padding: 10px;
    box-sizing: border-box;
}

.history-box > table {
    width: 100%;
    border-collapse: collapse;
}

.stock-table {
	width: 100%;
	table-layout: fixed;
	border-collapse: collapse;
}

.stock-table th, .stock-table td {
	padding: 12px 0;
	text-align: center;
}

.stock-table th:first-child, .stock-table td:first-child {
	text-align: left;
}

/* 마지막 열만 오른쪽 정렬 */
.stock-table th:last-child, .stock-table td:last-child {
	text-align: right;
}
</style>

</head>

<body>
	<div class="mypageHeader">
		<div class="profile-box">

			<div class="profile-photo">
				<c:choose>
					<c:when test="${empty userProfile.userPhoto}">
						<img src="/resources/images/logo.png" alt="프로필 사진" width="70"
							height="70">
					</c:when>

					<c:otherwise>
						<img src="/fileStorage/user_profile/${userProfile.userPhoto}"
							alt="프로필 사진" width="70" height="70">
					</c:otherwise>
				</c:choose>
			</div>

			<div class="profile-info">

				<div>
					<strong>${userProfile.userNick}</strong>
				</div>


				<div>가입일 : ${userProfile.userRegistedDateText}</div>

				<button type="button" id="btnProfileEdit">프로필 수정</button>

			</div>

		</div>


		<!-- 프로필 수정 모달 -->
		<div id="profileModal" class="profile-modal">

			<div class="profile-modal-content">

				<div class="profile-modal-header">
					<h2>프로필 수정</h2>

				</div>


				<form id="profileForm" enctype="multipart/form-data">

					<div class="profile-modal-body">

						<!-- 프로필 사진 수정 -->
						<div class="profile-photo-edit">

							<div class="profile-photo-preview">
								<img src="/resources/images/logo.png" alt="프로필 사진"
									id="profilePreview" width="80" height="80">
							</div>

							<div class="profile-photo-buttons">
								<p>
									<strong>프로필 사진</strong>
								</p>

								<p>JPG, PNG JPEG의 사진만 선택이 가능합니다.</p>

								<input type="file" id="profilePhotoInput" name="profilePhoto"
									accept=".png,.jpg,.jpeg" style="display: none;"> <input
									type="hidden" id="defaultPhoto" name="defaultPhoto"
									value="false">

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
								value="${userProfile.userNick}">

							<button type="button" id="btnProfileNickCheck">중복확인</button>

							<p id="profileNickCheckMsg"></p>

						</div>


						<!-- 비밀번호 변경 -->
						<div class="profile-pw-edit">
							<input type="hidden" id="passwordChange" name="passwordChange"
								value="false">

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
								id="portfolioPublic" name="userPortfolioIsPublic" value="1"
								<c:if
                                    test="${userProfile.userPortfolioIsPublic == 1}">checked</c:if>>


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


						<!-- 모달 하단 버튼 -->
						<div class="profile-modal-footer">

							<button type="button" id="btnProfileCancel">취소</button>

							<button type="submit" id="btnProfileSave">변경사항 저장</button>

						</div>

					</div>

				</form>
				<!-- form 끝 -->
				<form action="/user/withdraw" method="post" id="userDeleteForm"></form>

			</div>

		</div>

		<div class="totalAsset-box">
			<div style="height: 100px;">
				<p>총 자산</p>
				<h2>${assetsInfo.totalAsset}원</h2>
				<p>${assetsInfo.totalRevenue}원<span>${assetsInfo.revenuePercent}%</span>
				</p>
			</div>

			<div class="assetInfo-box-top">
				<div>
					<p>초기 투자금</p>
					<p>10000000원</p>
				</div>
				<div>
					<p>주식 평가금</p>
					<p>${assetsInfo.totalValuation}원</p>
				</div>
				<div>
					<p>예수금</p>
					<p>${assetsInfo.cash}원</p>
				</div>

			</div>

			<div class="assetInfo-box-bottom">
				<div>
					<p>보유 종목수</p>
					<p>${assetsInfo.stockCnt}원</p>
				</div>
				<div>
					<p>총 매수 횟수</p>
					<p>${assetsInfo.tradeCnt}원</p>
				</div>
			</div>
		</div>
	</div>

	<div class="mypageMiddle">

		<div class="portfolio-circle">
			<canvas id="portfolioChart"></canvas>
		</div>




		<table class="portfolio-Structure-box">
			<c:forEach var="portfolioInfo" items="${portfolioInfoList}">
				<tr>
					<td>${portfolioInfo.stockName}</td>
					<td>${portfolioInfo.stockCnt}주</td>
					<td>${portfolioInfo.valuationAmount}원</td>
				</tr>
			</c:forEach>
		</table>



		<div class="history-box">
			<table>
				<thead>
					<tr>
						<th colspan="4">거래기록</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="history" items="${historyList}">
						<tr>
							<td>${history.stockName}</td>
							<td>${history.tradePrice}원</td>
							<td>${history.tradeCnt}주</td>
							<td>${history.tradeDate}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>




	<p style="margin-top: 20px">보유 종목</p>
	<table class="stock-table">
		<thead>
			<tr>
				<th>종목명</th>
				<th>보유 수량</th>
				<th>매수 평단가</th>
				<th>현재가</th>
				<th>평가 금액</th>
				<th>평가 손익</th>
				<th>수익률</th>
				<th>비율</th>
			</tr>
		</thead>


		<c:forEach var="portfolioInfo" items="${portfolioInfoList}">
			<tr>
				<td>${portfolioInfo.stockName}</td>
				<td>${portfolioInfo.stockCnt}주</td>
				<td>${portfolioInfo.avgStockBuyCost}원</td>
				<td>${portfolioInfo.stockNowPrice}원</td>
				<td>${portfolioInfo.valuationAmount}원</td>
				<td>${portfolioInfo.pnl}원</td>
				<td>${portfolioInfo.profitPercent}%</td>
				<td>${portfolioInfo.weightPercent}%</td>
			</tr>
		</c:forEach>


		</tbody>
	</table>









	</div>







	<script>

        const btnProfileEdit = document.getElementById("btnProfileEdit");
        const profileModal = document.getElementById("profileModal");
        let originalPortfolioPublic;
        let originalProfilePhoto;

        // 프로필 수정 모달 열기 (열었을때 기준으로  당시의 닉네임이 저장됨)
        btnProfileEdit.addEventListener("click", () => {

            originalNick = inputProfileNick.value;

            profileNickCheck = false;
            profileNickCheckMsg.innerText = "";

            profileModal.style.display = "block";

            originalPortfolioPublic = portfolioPublic.checked;

            originalProfilePhoto = profilePreview.src;

        });


        const btnPhotoChange = document.getElementById("btnPhotoChange");
        const profilePhotoInput = document.getElementById("profilePhotoInput");
        const profilePreview = document.getElementById("profilePreview");

        const btnDefaultPhoto = document.getElementById("btnDefaultPhoto");
        const defaultPhoto = document.getElementById("defaultPhoto");

        // 사진 변경 버튼 클릭
        btnPhotoChange.addEventListener("click", () => {

            profilePhotoInput.click();

        });



        profilePhotoInput.addEventListener("change", () => {

            const file = profilePhotoInput.files[0];

            if (file == null) {
                return;
            }

            //새사진 선택 -> 기본 이미지 변경 취소
            defaultPhoto.value = "false";

            // 프로필 사진 확장자 확인(png, jpg, jpeg만 가능)
            const fileName = file.name.toLowerCase();

            if (!fileName.endsWith(".png")
                && !fileName.endsWith(".jpg")
                && !fileName.endsWith(".jpeg")) {

                alert("PNG, JPG, JPEG 파일만 사용할 수 있습니다.");

                profilePhotoInput.value = "";

                return;
            }


            //선택한 프로필 사진 미리보기
            const reader = new FileReader();

            reader.onload = (e) => {
                profilePreview.src = e.target.result;
            };

            reader.readAsDataURL(file);

        });


        // 기본 이미지 버튼 클릭
        btnDefaultPhoto.addEventListener("click", () => {

            // 선택한 사진 초기화
            profilePhotoInput.value = "";

            // 기본 이미지 미리보기
            profilePreview.src = "/resources/images/logo.png";

            // 기본 이미지로 변경한다는 값
            defaultPhoto.value = "true";

        });


        // 닉네임 중복 확인
        let profileNickCheck = false;

        const inputProfileNick = document.getElementById("inputProfileNick");
        const btnProfileNickCheck = document.getElementById("btnProfileNickCheck");
        const profileNickCheckMsg = document.getElementById("profileNickCheckMsg");

        let originalNick = "";

        btnProfileNickCheck.addEventListener("click", () => {

            const userNick = inputProfileNick.value;

            if (userNick.trim() == "") {
                profileNickCheckMsg.innerText = "닉네임을 입력해주세요.";
                return;
            }

            fetch("/user/api/checkNick", {
                method: "POST",
                headers: {
                    "Content-Type": "text/plain"
                },
                body: userNick
            })
                .then(response => response.json())
                .then(result => {

                    if (result.code == "rej_102") {

                        profileNickCheck = false;
                        profileNickCheckMsg.innerText = result.message;

                    } else if (result.code == "suc_001") {

                        profileNickCheck = true;
                        profileNickCheckMsg.innerText = "사용 가능한 닉네임입니다.";

                    }

                });


        });

        // 닉네임 수정 시 중복확인 초기화 + 공백 제거
        inputProfileNick.addEventListener("input", () => {

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
        const passwordChange = document.getElementById("passwordChange");


        // 현재 비밀번호 공백 입력 방지
        currentPw.addEventListener("input", () => {
            currentPw.value = currentPw.value.replace(/\s/g, "");
            passwordChange.value =
                currentPw.value != ""
                || newPw.value != ""
                || newPwCheck.value != "";
        });

        // 새 비밀번호 공백 입력 방지
        newPw.addEventListener("input", () => {
            newPw.value = newPw.value.replace(/\s/g, "");
            passwordChange.value =
                currentPw.value != ""
                || newPw.value != ""
                || newPwCheck.value != "";
        });

        // 새 비밀번호 확인 공백 입력 방지
        newPwCheck.addEventListener("input", () => {
            newPwCheck.value = newPwCheck.value.replace(/\s/g, "");
            passwordChange.value =
                currentPw.value != ""
                || newPw.value != ""
                || newPwCheck.value != "";
        });


        //비밀번호 영역 열기/닫기
        btnPwToggle.addEventListener("click", () => {

            // 비밀번호 변경 영역 열기
            if (pwChangeArea.style.display == "none") {
                pwChangeArea.style.display = "block";
                btnPwToggle.innerText = "비밀번호 변경 ▲";

            } else {

                // 입력된 비밀번호가 있으면 닫기 불가
                if (currentPw.value != ""
                    || newPw.value != ""
                    || newPwCheck.value != "") {

                    alert("입력한 비밀번호 정보가 있습니다.");
                    return;
                }

                // 비밀번호 변경 영역 닫기
                pwChangeArea.style.display = "none";
                btnPwToggle.innerText = "비밀번호 변경 ▼";
                passwordChange.value = "false";
            }

        });

        // 포트폴리오 공개 여부
        const portfolioPublic = document.getElementById("portfolioPublic");


        // 변경사항 저장
        const profileForm = document.getElementById("profileForm");

        profileForm.addEventListener("submit", (e) => {

            e.preventDefault();

            // 닉네임 검증
            if (inputProfileNick.value != originalNick) {

                if (profileNickCheck == false) {
                    alert("변경할 닉네임의 중복확인을 해주세요.");
                    return;
                }

            }

            // 비밀번호 검증
            if (currentPw.value != ""
                || newPw.value != ""
                || newPwCheck.value != "") {

                if (currentPw.value == "") {
                    alert("현재 비밀번호를 입력해주세요.");
                    return;
                }

                if (newPw.value == "") {
                    alert("새 비밀번호를 입력해주세요.");
                    return;
                }

                if (newPwCheck.value == "") {
                    alert("새 비밀번호 확인을 입력해주세요.");
                    return;
                }

                if (newPw.value != newPwCheck.value) {
                    alert("새 비밀번호가 일치하지 않습니다.");
                    return;
                }

            }



            const formData = new FormData(profileForm);

            //포트폴리오 공개 여부
            if (portfolioPublic.checked) {
                formData.set("userPortfolioIsPublic", 1);
            } else {
                formData.set("userPortfolioIsPublic", 0);
            }


            // FormData 값 확인
            for (const pair of formData.entries()) {
                console.log(pair[0], pair[1]);
            }

            fetch("/user/api/profile", {
                method: "POST",
                body: formData
            })
                .then(response => response.json())
                .then(result => {

                    console.log(result);

                    if (result.code == "suc_001") {
                        alert("프로필 수정 요청 성공");

                    } else {
                        alert(result.message);
                    }

                });

        });



        //탈퇴
        const btnUserDelete = document.getElementById("btnUserDelete");
        const userDeleteForm = document.getElementById("userDeleteForm");

        btnUserDelete.addEventListener("click", () => {

            const result = confirm("정말로 탈퇴하시겠습니까?");

            if (!result) {
                return;
            }

            userDeleteForm.submit();
        });



        //취소
        const btnProfileCancel = document.getElementById("btnProfileCancel");

        btnProfileCancel.addEventListener("click", () => {


            currentPw.value = "";
            newPw.value = "";
            newPwCheck.value = "";


            passwordChange.value = "false";


            pwChangeArea.style.display = "none";
            btnPwToggle.innerText = "비밀번호 변경 ▼";



            inputProfileNick.value = originalNick;

            profileNickCheck = false;
            profileNickCheckMsg.innerText = "";

            portfolioPublic.checked = originalPortfolioPublic;

            // 프로필 사진 원래 상태로 복구
            profilePreview.src = originalProfilePhoto;
            profilePhotoInput.value = "";
            defaultPhoto.value = "false";



            profileModal.style.display = "none";

        });



        const data = {
            labels: ${stockNameList},
            datasets: [{
                label: "평가금액",
                data: ${stockPriceList},
                backgroundColor: [
                    "#4e73df",
                    "#1cc88a",
                    "#f6c23e",
                    "#858796"
                ],
                borderColor: "#ffffff",
                borderWidth: 2,
                hoverOffset: 10
            }]
        };

        // 2. 차트 설정
        const config = {
            type: "doughnut",
            data: data,
            options: {
                responsive: true,
                maintainAspectRatio: false,

                plugins: {
                    legend: {
                        position: "right"
                    },

                    title: {
                        display: true,
                        text: "포트폴리오"
                    },

                    tooltip: {
                        callbacks: {
                            label: function (context) {
                                const values = context.dataset.data;

                                const total = values.reduce(function (sum, value) {
                                    return sum + value;
                                }, 0);

                                const value = context.raw;
                                const percent = value / total * 100;

                                return context.label
                                    + ": "
                                    + value.toLocaleString()
                                    + "원 ("
                                    + percent.toFixed(1)
                                    + "%)";
                            }
                        }
                    }
                }
            }
        };

        // 3. canvas에 차트 생성
        const portfolioChart = new Chart(
            document.getElementById("portfolioChart"),
            config
        );


    </script>
</body>


</html>