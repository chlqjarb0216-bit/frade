<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>마이페이지</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<!-- 부트스트랩 CSS --> 
<link href ="https: //cdn.jsdelivr.net /npm /bootstrap 
	 @5.0.2 /dist /css /bootstrap.min.css
 " rel ="stylesheet " integrity ="sha384-EVSTQN3 /azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC
 " crossorigin ="anonymous "> 



<style>
:root {
    --main-bg: #f8fafc;
    --card-bg: #ffffff;
    --border-color: #e2e8f0;
    --text-primary: #1e293b;
    --text-secondary: #64748b;
    --text-muted: #94a3b8;
    --color-primary: #2563eb;
    --radius-card: 14px;
}

body {
    margin: 0;
    padding: 0;
    background-color: var(--main-bg);
    color: var(--text-primary);
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
}

.mypage-content {
    max-width: 1200px;
    margin: 24px auto 60px;
    padding: 0 16px;
    box-sizing: border-box;
}

.mypageHeader {
    display: flex;
    align-items: stretch;
    gap: 20px;
    width: 100%;
}

.profile-box {
    display: flex;
    align-items: center;
    gap: 20px;
    flex: 0 1 32%;
    width: auto;
    padding: 24px;
    background: var(--card-bg);
    border: 1px solid var(--border-color);
    border-radius: var(--radius-card);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.03);
}

.profile-photo img {
    border-radius: 50%;
    object-fit: cover;
    border: 1px solid var(--border-color);
}

.profile-info {
    display: flex;
    flex-direction: column;
    gap: 8px;
}

.profile-info strong {
    font-size: 18px;
    color: var(--text-primary);
}

.profile-info div:nth-child(2) {
    font-size: 13px;
    color: var(--text-secondary);
}

#btnProfileEdit {
    padding: 6px 14px;
    font-size: 13px;
    font-weight: 600;
    border-radius: 8px;
    border: 1px solid var(--border-color);
    background: #ffffff;
    color: var(--text-primary);
    cursor: pointer;
    transition: background-color 0.15s;
    width: fit-content;
}

#btnProfileEdit:hover {
    background-color: #f1f5f9;
}

.totalAsset-box {
    background: var(--card-bg);
    border: 1px solid var(--border-color);
    border-radius: var(--radius-card);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.03);
    display: grid;
    grid-template-columns: minmax(200px, 1fr) minmax(0, 2fr);
    grid-template-rows: repeat(2, minmax(0, 1fr));
    column-gap: 32px;
    align-items: center;
    flex: 1 1 68%;
    width: auto;
    padding: 24px;
    margin-left: 0;
}

.totalAsset-summary {
    grid-column: 1;
    grid-row: 1/3;
    align-self: center;
    border-right: 1px solid var(--border-color);
    padding-right: 20px;
}

.totalAsset-summary p:first-child {
    font-size: 13px;
    color: var(--text-secondary);
    margin: 0 0 4px 0;
    font-weight: 600;
}

.totalAsset-summary h2 {
    font-size: 26px;
    font-weight: 800;
    color: #0f172a;
    margin: 0 0 6px 0;
}

.totalAsset-summary p:last-child {
    font-size: 14px;
    font-weight: 600;
    color: #e53935;
    margin: 0;
    display: flex;
    gap: 6px;
}

.assetInfo-box-top, .assetInfo-box-bottom {
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
    grid-column: 2;
    width: 100%;
    align-items: center;
}

.assetInfo-box-top p:first-child, .assetInfo-box-bottom p:first-child {
    font-size: 12px;
    color: var(--text-muted);
    margin: 0 0 4px 0;
    font-weight: 600;
}

.assetInfo-box-top p:last-child, .assetInfo-box-bottom p:last-child {
    font-size: 15px;
    font-weight: 700;
    color: var(--text-primary);
    margin: 0;
}

.mypageMiddle {
    display: flex;
    gap: 20px;
    width: 100%;
    margin-top: 24px;
}

.portfolio-circle {
    flex: 1.2 1 0;
    min-width: 0;
    height: 320px;
    background: var(--card-bg);
    border: 1px solid var(--border-color);
    border-radius: var(--radius-card);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.03);
    padding: 16px;
    box-sizing: border-box;
}

.portfolio-Structure-box {
    flex: 1 1 0;
    min-width: 0;
    background: var(--card-bg);
    border: 1px solid var(--border-color);
    border-radius: var(--radius-card);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.03);
    padding: 16px;
    box-sizing: border-box;
    border-collapse: separate;
    border-spacing: 0;
}

.portfolio-Structure-box td {
    padding: 12px 8px;
    border-bottom: 1px solid #f1f5f9;
    font-size: 14px;
}

.portfolio-Structure-box tr:last-child td {
    border-bottom: none;
}

.portfolio-Structure-box td:first-child {
    font-weight: 600;
    color: var(--text-primary);
}

.portfolio-Structure-box td:nth-child(2) {
    color: var(--text-secondary);
    text-align: center;
}

.portfolio-Structure-box td:last-child {
    font-weight: 700;
    text-align: right;
    color: var(--text-primary);
}

.history-box {
    flex: 1.8 1 0;
    min-width: 0;
    height: 320px;
    overflow-y: auto;
    background: var(--card-bg);
    border: 1px solid var(--border-color);
    border-radius: var(--radius-card);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.03);
    padding: 16px 20px;
    box-sizing: border-box;
}

.history-box > table {
    width: 100%;
    border-collapse: collapse;
}

.history-box th {
    padding-bottom: 12px;
    font-size: 15px;
    font-weight: 700;
    text-align: left;
    border-bottom: 1px solid var(--border-color);
}

.history-box td {
    padding: 10px 4px;
    font-size: 13px;
    border-bottom: 1px solid #f1f5f9;
}

.history-box tr:last-child td {
    border-bottom: none;
}

.stock-table-wrap {
    background: var(--card-bg);
    border: 1px solid var(--border-color);
    border-radius: var(--radius-card);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.03);
    padding: 20px;
    margin-top: 12px;
    overflow-x: auto;
}

.stock-table {
    width: 100%;
    table-layout: fixed;
    border-collapse: collapse;
}

.stock-table th {
    padding: 12px 8px;
    font-size: 13px;
    font-weight: 600;
    color: var(--text-secondary);
    border-bottom: 1px solid var(--border-color);
    text-align: center;
}

.stock-table td {
    padding: 14px 8px;
    font-size: 14px;
    text-align: center;
    border-bottom: 1px solid #f8fafc;
}

.stock-table tr:hover td {
    background-color: #f8fafc;
}

.stock-table th:first-child, .stock-table td:first-child {
    text-align: left;
}

.holding-stock-name {
    display: block;
    font-weight: 700;
    color: var(--text-primary);
}

.holding-stock-code {
    display: block;
    margin-top: 2px;
    color: var(--text-muted);
    font-size: 11px;
}

.stock-table th:last-child, .stock-table td:last-child {
    text-align: right;
}

.profile-modal {
    display: none;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 9999;
}

.profile-modal-content {
    width: 480px;
    max-height: 85vh;
    overflow-y: auto;
    margin: 60px auto;
    padding: 24px;
    background-color: #ffffff;
    border-radius: 16px;
    box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
}

.profile-modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid var(--border-color);
    padding-bottom: 16px;
}

.profile-modal-header h2 {
    font-size: 18px;
    font-weight: 700;
    margin: 0;
}

.profile-photo-edit {
    display: flex;
    align-items: center;
    gap: 20px;
    padding: 20px 0;
    border-bottom: 1px solid var(--border-color);
}

.profile-photo-preview img {
    border-radius: 50%;
    object-fit: cover;
}

.profile-photo-buttons p {
    margin: 2px 0 6px 0;
}

.profile-photo-buttons button, .profile-nick-edit button {
    padding: 6px 12px;
    font-size: 12px;
    font-weight: 600;
    border-radius: 6px;
    border: 1px solid var(--border-color);
    background-color: #ffffff;
    cursor: pointer;
}

.profile-nick-edit {
    padding: 20px 0;
    border-bottom: 1px solid var(--border-color);
}

.profile-nick-edit p {
    margin: 4px 0 8px 0;
}

.profile-nick-edit input, #pwChangeArea input {
    height: 38px;
    border: 1px solid var(--border-color);
    border-radius: 8px;
    padding: 0 12px;
    font-size: 14px;
    outline: none;
}

.profile-nick-edit input:focus, #pwChangeArea input:focus {
    border-color: var(--color-primary);
}

.profile-pw-edit {
    padding: 20px 0;
    border-bottom: 1px solid var(--border-color);
}

#btnPwToggle {
    width: 100%;
    text-align: left;
    background: none;
    border: none;
    font-weight: 600;
    color: var(--text-secondary);
    padding: 0;
    cursor: pointer;
}

.profile-public-edit {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 0;
    border-bottom: 1px solid var(--border-color);
}

.profile-public-edit p {
    margin: 4px 0 0 0;
    font-size: 12px;
    color: var(--text-secondary);
}

.switch {
    position: relative;
    display: inline-block;
    width: 44px;
    height: 24px;
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
    background-color: #cbd5e1;
    border-radius: 24px;
    transition: 0.2s;
}

.slider:before {
    position: absolute;
    content: "";
    height: 18px;
    width: 18px;
    left: 3px;
    bottom: 3px;
    background-color: white;
    border-radius: 50%;
    transition: 0.2s;
}

.switch input:checked + .slider {
    background-color: var(--color-primary);
}

.switch input:checked + .slider:before {
    transform: translateX(20px);
}

.profile-delete {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 0;
}

.profile-delete p {
    margin: 4px 0 0 0;
    font-size: 12px;
    color: var(--text-secondary);
}

#btnUserDelete {
    color: #ef4444;
    background: none;
    border: none;
    font-size: 13px;
    font-weight: 600;
    cursor: pointer;
}

.profile-modal-footer {
    display: flex;
    justify-content: flex-end;
    gap: 8px;
    padding-top: 16px;
    border-top: 1px solid var(--border-color);
}

#btnProfileCancel {
    padding: 8px 16px;
    border-radius: 8px;
    border: 1px solid var(--border-color);
    background: #ffffff;
    font-size: 13px;
    font-weight: 600;
    cursor: pointer;
}

#btnProfileSave {
    padding: 8px 16px;
    border-radius: 8px;
    border: none;
    background: var(--color-primary);
    color: #ffffff;
    font-size: 13px;
    font-weight: 600;
    cursor: pointer;
}

@media (max-width: 900px) {
    .mypageHeader {
        flex-direction: column;
    }
    .profile-box, .totalAsset-box {
        width: 100%;
        box-sizing: border-box;
    }
    .totalAsset-box {
        height: auto;
    }
    .assetInfo-box-top, .assetInfo-box-bottom {
        width: 100%;
        height: auto;
    }
    .mypageMiddle {
        flex-direction: column;
    }
    .portfolio-circle, .portfolio-Structure-box, .history-box {
        width: 100%;
        box-sizing: border-box;
    }
}

@media (max-width: 600px) {
    .totalAsset-box {
        grid-template-columns: 1fr;
        grid-template-rows: auto;
        row-gap: 16px;
    }
    .totalAsset-summary {
        border-right: none;
        border-bottom: 1px solid var(--border-color);
        padding-bottom: 16px;
    }
    .totalAsset-summary, .assetInfo-box-top, .assetInfo-box-bottom {
        grid-column: 1;
        grid-row: auto;
    }
}
</style>
</head>

<body>
    <jsp:include page="../common/navbar.jsp"></jsp:include>

    <main class="mypage-content">
        <div class="mypageHeader">
            <div class="profile-box">
                <div class="profile-photo">
                    <c:choose>
                        <c:when test="${not empty userProfile.userPhoto}">
                            <img src="${pageContext.request.contextPath}/file-storage/user_profile/${userProfile.userPhoto}" alt="프로필 사진" width="70" height="70">
                        </c:when>
                        <c:otherwise>
                            <img src="${pageContext.request.contextPath}/resources/images/Default_profile.png" alt="프로필 사진" width="70" height="70">
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

            <div id="profileModal" class="profile-modal">
                <div class="profile-modal-content">
                    <div class="profile-modal-header">
                        <h2>프로필 수정</h2>
                    </div>
                    <form action="/user/profile" method="post" enctype="multipart/form-data" id="profileForm">
                        <div class="profile-modal-body">
                            <div class="profile-photo-edit">
                                <div class="profile-photo-preview">
                                    <c:choose>
                                        <c:when test="${not empty userProfile.userPhoto}">
                                            <img src="${pageContext.request.contextPath}/file-storage/user_profile/${userProfile.userPhoto}" alt="프로필 사진" id="profilePreview" width="80" height="80">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/resources/images/Default_profile.png" alt="프로필 사진" id="profilePreview" width="80" height="80">
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="profile-photo-buttons">
                                    <p><strong>프로필 사진</strong></p>
                                    <p style="font-size: 12px; color: var(--text-muted);">JPG, PNG JPEG의 사진만 선택이 가능합니다.</p>
                                    <input type="file" id="profilePhotoInput" name="profilePhoto" accept=".png,.jpg,.jpeg" style="display: none;">
                                    <input type="hidden" id="defaultPhoto" name="defaultPhoto" value="false">
                                    <button type="button" id="btnPhotoChange">사진 변경</button>
                                    <button type="button" id="btnDefaultPhoto">기본 이미지</button>
                                </div>
                            </div>

                            <div class="profile-nick-edit">
                                <p><strong>닉네임</strong></p>
                                <div style="display: flex; gap: 8px;">
                                    <input type="text" name="userNick" id="inputProfileNick" value="${userProfile.userNick}" style="flex: 1;">
                                    <button type="button" id="btnProfileNickCheck">중복확인</button>
                                </div>
                                <p id="profileNickCheckMsg" style="font-size: 12px; margin-top: 4px;"></p>
                            </div>

                            <div class="profile-pw-edit">
                                <input type="hidden" id="passwordChange" name="passwordChange" value="false">
                                <button type="button" id="btnPwToggle">비밀번호 변경 ▼</button>
                                <div id="pwChangeArea" style="display: none; margin-top: 12px;">
                                    <p style="font-size: 13px; margin: 8px 0 4px 0;"><strong>현재 비밀번호</strong></p>
                                    <input type="password" name="currentPw" id="currentPw" style="width: 100%;">
                                    <p style="font-size: 13px; margin: 8px 0 4px 0;"><strong>새 비밀번호</strong></p>
                                    <input type="password" name="newPw" id="newPw" style="width: 100%;">
                                    <p style="font-size: 13px; margin: 8px 0 4px 0;"><strong>새 비밀번호 확인</strong></p>
                                    <input type="password" name="newPwCheck" id="newPwCheck" style="width: 100%;">
                                </div>
                            </div>

                            <div class="profile-public-edit">
                                <div>
                                    <strong style="font-size: 14px;">포트폴리오 공개</strong>
                                    <p>다른 사용자에게 내 포트폴리오를 공개합니다.</p>
                                </div>
                                <label class="switch">
                                    <input type="checkbox" id="portfolioPublic" name="userPortfolioIsPublic" value="1" <c:if test="${userProfile.userPortfolioIsPublic == 1}">checked</c:if>>
                                    <span class="slider"></span>
                                </label>
                            </div>

                            <div class="profile-delete">
                                <div>
                                    <strong style="font-size: 14px;">회원 탈퇴</strong>
                                    <p>탈퇴 시 계정 정보를 복구할 수 없습니다.</p>
                                </div>
                                <button type="button" id="btnUserDelete">탈퇴하기</button>
                            </div>

                            <div class="profile-modal-footer">
                                <button type="button" id="btnProfileCancel">취소</button>
                                <button type="submit" id="btnProfileSave">변경사항 저장</button>
                            </div>
                        </div>
                    </form>
                    <form action="/user/withdraw" method="post" id="userDeleteForm"></form>
                </div>
            </div>
        

        <div class="totalAsset-box" style="margin-top: 20px;">
            <div class="totalAsset-summary">
                <p>총 자산</p>
                <h2><fmt:formatNumber value="${assetsInfo.totalAsset}" pattern="#,###"/>원</h2>
                <p><fmt:formatNumber value="${assetsInfo.totalRevenue}" pattern="#,###"/>원
    <span>(${assetsInfo.revenuePercent}%)</span></p>
            </div>

            <div class="assetInfo-box-top">
                <div>
                    <p>초기 투자금</p>
                    <p>10,000,000원</p>
                </div>
                <div>
                    <p>주식 평가금</p>
                    <p><fmt:formatNumber value="${assetsInfo.totalValuation}" pattern="#,###"/>원</p>
                </div>
                <div>
                    <p>예수금</p>
                    <p> <fmt:formatNumber value="${assetsInfo.cash}" pattern="#,###"/>원</p>
                </div>
            </div>

            <div class="assetInfo-box-bottom">
                <div>
                    <p>보유 종목수</p>
                    <p>${assetsInfo.stockCnt}개</p>
                </div>
                <div>
                    <p>총 거래 횟수</p>
                    <p>${assetsInfo.tradeCnt}회</p>
                </div>
                <div></div>
            </div>
        </div>
      </div>

        <div class="mypageMiddle">
            <div class="portfolio-circle">
                <canvas id="portfolioChart"></canvas>
            </div>

            <table class="portfolio-Structure-box">
                <c:if test="${empty portfolioInfoList}">
                    <tr><td colspan="3" style="text-align: center; color: var(--text-muted);">포트폴리오가 비어있습니다.</td></tr>
                </c:if>
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
                        <c:if test="${empty historyList}">
                            <tr><td colspan="4" style="text-align: center; color: var(--text-muted); padding: 30px 0;">거래기록이 없습니다.</td></tr>
                        </c:if>
                        <c:forEach var="history" items="${historyList}">
                            <tr>
                                <td>${history.stockName}</td>
                                <td>${history.tradePrice}원</td>
                                <td>${history.tradeCnt}주</td>
                                <td style="color: var(--text-muted);">${history.tradeDate}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="stock-table-wrap">
            <p style="font-size: 16px; font-weight: 700; margin: 0 0 16px 0;">보유 종목</p>
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
                <tbody>
                    <c:if test="${empty portfolioInfoList}">
                        <tr><td colspan="8" style="text-align: center; color: var(--text-muted); padding: 40px 0;">보유 종목이 없습니다.</td></tr>
                    </c:if>
                    <c:forEach var="portfolioInfo" items="${portfolioInfoList}">
                        <tr>
                            <td>
                                <span class="holding-stock-name">${portfolioInfo.stockName}</span>
                                <span class="holding-stock-code">${portfolioInfo.stockCode}</span>
                            </td>
                            <td><fmt:formatNumber value="${portfolioInfo.stockCnt}" pattern="#,###"/>주</td>
                            <td><fmt:formatNumber value="${portfolioInfo.avgStockBuyCost}" pattern="#,###"/>원</td>
                            <td><fmt:formatNumber value="${portfolioInfo.stockNowPrice}" pattern="#,###"/>원</td>
                            <td><fmt:formatNumber value="${portfolioInfo.valuationAmount}" pattern="#,###"/>원</td>
                            <td style="font-weight: 600;"><fmt:formatNumber value="${portfolioInfo.pnl}" pattern="#,###"/>원</td>
                            <td style="font-weight: 600;"><fmt:formatNumber value="${portfolioInfo.profitPercent}" pattern="#,##0.00"/>%</td>
                            <td><fmt:formatNumber value="${portfolioInfo.weightPercent}" pattern="#,##0.00"/>%</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </main>

    <c:if test="${not empty profileSuccess}">
        <script>
            alert("${profileSuccess}");
        </script>
    </c:if>

    <c:if test="${not empty profileFail}">
        <script>
            alert("${profileFail}");
        </script>
    </c:if>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>

    <script>
        const btnProfileEdit = document.getElementById("btnProfileEdit");
        const profileModal = document.getElementById("profileModal");
        let originalPortfolioPublic;
        let originalProfilePhoto;

        btnProfileEdit.addEventListener("click", () => {
            originalNick = inputProfileNick.value;
            profileModal.style.display = "block";
            profileNickCheck = false;
            profileNickCheckMsg.innerText = "";
            btnProfileNickCheck.disabled = true;
            originalPortfolioPublic = portfolioPublic.checked;
            originalProfilePhoto = profilePreview.src;
        });

        const btnPhotoChange = document.getElementById("btnPhotoChange");
        const profilePhotoInput = document.getElementById("profilePhotoInput");
        const profilePreview = document.getElementById("profilePreview");

        const btnDefaultPhoto = document.getElementById("btnDefaultPhoto");
        const defaultPhoto = document.getElementById("defaultPhoto");

        btnPhotoChange.addEventListener("click", () => {
            profilePhotoInput.click();
        });

        profilePhotoInput.addEventListener("change", () => {
            const file = profilePhotoInput.files[0];
            if (file == null) {
                return;
            }
            defaultPhoto.value = "false";
            const fileName = file.name.toLowerCase();
            if (!fileName.endsWith(".png") && !fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg")) {
                alert("PNG, JPG, JPEG 파일만 사용할 수 있습니다.");
                profilePhotoInput.value = "";
                return;
            }
            const reader = new FileReader();
            reader.onload = (e) => {
                profilePreview.src = e.target.result;
            };
            reader.readAsDataURL(file);
        });

        btnDefaultPhoto.addEventListener("click", () => {
            profilePhotoInput.value = "";
            profilePreview.src = "/resources/images/Default_profile.png";
            defaultPhoto.value = "true";
        });

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
            fetch("/api/user/checkNick", {
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

        const currentPw = document.getElementById("currentPw");
        const newPw = document.getElementById("newPw");
        const newPwCheck = document.getElementById("newPwCheck");
        const btnPwToggle = document.getElementById("btnPwToggle");
        const pwChangeArea = document.getElementById("pwChangeArea");
        const passwordChange = document.getElementById("passwordChange");

        inputProfileNick.addEventListener("input", () => {
            inputProfileNick.value = inputProfileNick.value.replace(/\s/g, "");
            profileNickCheck = false;
            profileNickCheckMsg.innerText = "";
            if (inputProfileNick.value == originalNick) {
                btnProfileNickCheck.disabled = true;
            } else {
                btnProfileNickCheck.disabled = false;
            }
        });

        currentPw.addEventListener("input", () => {
            currentPw.value = currentPw.value.replace(/\s/g, "");
        });

        newPw.addEventListener("input", () => {
            newPw.value = newPw.value.replace(/\s/g, "");
        });

        newPwCheck.addEventListener("input", () => {
            newPwCheck.value = newPwCheck.value.replace(/\s/g, "");
        });

        btnPwToggle.addEventListener("click", () => {
            if (pwChangeArea.style.display == "none") {
                pwChangeArea.style.display = "block";
                btnPwToggle.innerText = "비밀번호 변경 ▲";
                passwordChange.value = "true";
            } else {
                if (currentPw.value != "" || newPw.value != "" || newPwCheck.value != "") {
                    alert("입력한 비밀번호 정보가 있습니다.");
                    return;
                }
                pwChangeArea.style.display = "none";
                btnPwToggle.innerText = "비밀번호 변경 ▼";
                passwordChange.value = "false";
            }
        });

        const portfolioPublic = document.getElementById("portfolioPublic");
        const profileForm = document.getElementById("profileForm");

        profileForm.addEventListener("submit", (e) => {
            e.preventDefault();
            if (inputProfileNick.value != originalNick) {
                if (profileNickCheck == false) {
                    alert("변경할 닉네임의 중복확인을 해주세요.");
                    return;
                }
            }
            if (passwordChange.value == "true") {
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
            profileForm.submit();
        });

        const btnUserDelete = document.getElementById("btnUserDelete");
        const userDeleteForm = document.getElementById("userDeleteForm");

        btnUserDelete.addEventListener("click", () => {
            const result = confirm("정말로 탈퇴하시겠습니까?");
            if (!result) {
                return;
            }
            userDeleteForm.submit();
        });

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
            profilePreview.src = originalProfilePhoto;
            profilePhotoInput.value = "";
            defaultPhoto.value = "false";
            profileModal.style.display = "none";
        });

        const portfolioLabels = ${stockNameList};
        const portfolioColors = portfolioLabels.map(function (_, index) {
            const hue = (220 + index * 137.508) % 360;
            const saturation = [72, 65, 78][index % 3];
            const lightness = [48, 58, 40][Math.floor(index / 3) % 3];
            return "hsl(" + hue.toFixed(3) + ", " + saturation + "%, " + lightness + "%)";
        });
        const data = {
            labels: portfolioLabels,
            datasets: [{
                label: "평가금액",
                data: ${stockPriceList},
                backgroundColor: portfolioColors,
                borderColor: "#ffffff",
                borderWidth: 2,
                hoverOffset: 10
            }]
        };

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

        const portfolioChart = new Chart(
            document.getElementById("portfolioChart"),
            config
        );
    </script>
</body>
</html>