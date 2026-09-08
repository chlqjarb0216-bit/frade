<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>커뮤니티 게시글 ${not empty post ? '수정' : '작성'}</title>

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
    font-family: -apple-system, BlinkMacSystemFont, "Apple SD Gothic Neo", "Pretendard", Roboto, "Noto Sans KR", sans-serif;
}

.write-container {
    max-width: 1080px;
    margin: 40px auto 80px;
    padding: 0 16px;
    box-sizing: border-box;
}

.write-header {
    margin-bottom: 24px;
}

.btn-back {
    display: inline-flex;
    align-items: center;
    padding: 8px 16px;
    font-size: 13px;
    font-weight: 600;
    color: var(--text-secondary);
    background-color: transparent;
    border: 1px solid var(--border-color);
    border-radius: 8px;
    text-decoration: none;
    margin-bottom: 16px;
    transition: all 0.15s;
}

.btn-back:hover {
    background-color: #ffffff;
    color: var(--text-primary);
}

.write-header h3 {
    margin: 0 0 6px 0;
    font-size: 24px;
    font-weight: 800;
    color: #0f172a;
}

.write-header p {
    margin: 0;
    font-size: 14px;
    color: var(--text-secondary);
}

.write-header hr {
    margin-top: 16px;
    margin-bottom: 0;
    border: none;
    border-top: 1px solid var(--border-color);
}

.custom-card {
    background-color: var(--card-bg);
    border: 1px solid var(--border-color);
    border-radius: var(--radius-card);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.03);
    padding: 36px;
    box-sizing: border-box;
}

.form-group {
    margin-bottom: 24px;
}

.form-label {
    display: block;
    font-weight: 600;
    font-size: 14px;
    color: var(--text-primary);
    margin-bottom: 8px;
}

.category-group {
    display: inline-flex;
    border: 1px solid var(--border-color);
    border-radius: 8px;
    overflow: hidden;
    background-color: #ffffff;
}

.category-option {
    position: relative;
}

.category-option input[type="radio"] {
    position: absolute;
    opacity: 0;
    width: 0;
    height: 0;
}

.category-option label {
    display: inline-block;
    padding: 8px 20px;
    font-size: 13px;
    font-weight: 600;
    color: var(--text-secondary);
    cursor: pointer;
    background-color: #ffffff;
    transition: all 0.15s;
    border-right: 1px solid var(--border-color);
    user-select: none;
}

.category-option:last-child label {
    border-right: none;
}

.category-option input[type="radio"]:checked + label {
    background-color: var(--color-primary);
    color: #ffffff;
}

.custom-form-control {
    width: 100%;
    border: 1px solid var(--border-color);
    border-radius: 8px;
    font-size: 14px;
    padding: 11px 14px;
    box-sizing: border-box;
    outline: none;
    font-family: inherit;
    color: var(--text-primary);
    background-color: #ffffff;
    transition: border-color 0.15s;
}

.custom-form-control:focus {
    border-color: var(--color-primary);
}

textarea.custom-form-control {
    resize: vertical;
    min-height: 240px;
    line-height: 1.6;
}

.file-input-control {
    width: 100%;
    border: 1px solid var(--border-color);
    border-radius: 8px;
    font-size: 13px;
    padding: 9px 12px;
    box-sizing: border-box;
    background-color: #ffffff;
    color: var(--text-primary);
    cursor: pointer;
}

.existing-file-box {
    padding: 16px;
    margin-bottom: 12px;
    background-color: #f8fafc;
    border: 1px solid var(--border-color);
    border-radius: 8px;
}

.existing-file-title {
    font-weight: 600;
    font-size: 13px;
    color: var(--text-secondary);
    margin-bottom: 8px;
}

.existing-file-list {
    list-style: none;
    padding: 0;
    margin: 0 0 10px 0;
    font-size: 13px;
    color: var(--text-secondary);
}

.existing-file-list li {
    margin-bottom: 4px;
}

.delete-check-label {
    display: inline-flex;
    align-items: center;
    gap: 6px;
    font-size: 12px;
    font-weight: 600;
    color: #ef4444;
    cursor: pointer;
}

.file-guide-text {
    font-size: 12px;
    color: var(--text-muted);
    margin-top: 8px;
}

.form-actions {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    gap: 10px;
    padding-top: 20px;
    border-top: 1px solid var(--border-color);
}

.btn-cancel {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    padding: 9px 20px;
    font-size: 13px;
    font-weight: 600;
    color: var(--text-secondary);
    background-color: #ffffff;
    border: 1px solid var(--border-color);
    border-radius: 8px;
    text-decoration: none;
    cursor: pointer;
    transition: all 0.15s;
}

.btn-cancel:hover {
    background-color: #f1f5f9;
    color: var(--text-primary);
}

.btn-submit {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    padding: 9px 24px;
    font-size: 13px;
    font-weight: 600;
    color: #ffffff;
    background-color: var(--color-primary);
    border: none;
    border-radius: 8px;
    cursor: pointer;
    transition: background-color 0.15s;
}

.btn-submit:hover {
    background-color: #1d4ed8;
}
</style>
</head>
<body>

    <jsp:include page="../common/navbar.jsp"></jsp:include>

    <div class="write-container">
        
        <div class="write-header">
            <c:choose>
                <c:when test="${not empty post}">
                    <a href="/community-lists/detail?postNum=${post.postNum}" class="btn-back">← 게시글로</a>
                    <h3>게시글 수정</h3>
                </c:when>
                <c:otherwise>
                    <a href="/community-lists" class="btn-back">← 커뮤니티로</a>
                    <h3>게시글 작성</h3>
                </c:otherwise>
            </c:choose>
            <p>투자 아이디어, 질문, 정보를 자유롭게 나눠보세요.</p>
            <hr>
        </div>

        <div class="custom-card">
            <form id="postForm" action="${not empty post ? '/community-lists/edit' : '/community-lists/write'}" method="post" enctype="multipart/form-data" onsubmit="postValidate(event)">
                
                <c:if test="${not empty post}">
                    <input type="hidden" name="postNum" value="${post.postNum}">
                </c:if>

                <div class="form-group">
                    <label class="form-label">카테고리</label>
                    <div class="category-group">
                        <div class="category-option">
                            <input type="radio" name="postCategoryNum" id="catF" value="1" ${post.postCategoryNum == 1 ? 'checked' : ''}>
                            <label for="catF">자유</label>
                        </div>

                        <div class="category-option">
                            <input type="radio" name="postCategoryNum" id="catI" value="2" ${post.postCategoryNum == 2 ? 'checked' : ''}>
                            <label for="catI">정보</label>
                        </div>

                        <div class="category-option">
                            <input type="radio" name="postCategoryNum" id="catQ" value="3" ${post.postCategoryNum == 3 ? 'checked' : ''}>
                            <label for="catQ">질문</label>
                        </div>
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="postTitle" class="form-label">제목</label>
                    <input type="text" class="custom-form-control" placeholder="제목을 입력해주세요 (최대 한글 30자)" id="postTitle" name="postTitle" value="<c:out value='${post.postTitle}'/>">
                </div>
                
                <div class="form-group">
                    <label for="postContent" class="form-label">내용</label>
                    <textarea class="custom-form-control" placeholder="내용을 입력해주세요" id="postContent" name="postContent" rows="10"><c:out value="${post.postContent}"/></textarea>
                </div>
                
                <div class="form-group">
                    <label for="uploadFiles" class="form-label">첨부파일</label>
                    <c:if test="${not empty post and not empty post.fileList}">
                        <div class="existing-file-box">
                            <div class="existing-file-title">📎 현재 첨부된 파일:</div>
                            <ul class="existing-file-list">
                                <c:forEach var="file" items="${post.fileList}">
                                    <li>• ${file}</li>
                                </c:forEach>
                            </ul>
                            <label class="delete-check-label" for="deleteExistingFiles">
                                <input type="checkbox" id="deleteExistingFiles" name="deleteExistingFiles" value="true">
                                기존 첨부파일 모두 삭제
                            </label>
                        </div>
                    </c:if>
                    <input class="file-input-control" type="file" id="uploadFiles" name="uploadFiles" multiple>
                    <div class="file-guide-text">
                        * 파일은 최대 3개, 개당 10MB 이하로 첨부 가능합니다.<c:if test="${not empty post and not empty post.fileList}"> (기존 파일 삭제를 체크하지 않고 첨부 시 기존 파일에 추가됩니다.)</c:if>
                    </div>
                </div>
                
                <div class="form-actions">
                    <c:choose>
                        <c:when test="${not empty post}">
                            <a href="/community-lists/detail?postNum=${post.postNum}" class="btn-cancel">취소</a>
                            <button type="submit" class="btn-submit">수정 완료</button>
                        </c:when>
                        <c:otherwise>
                            <a href="/community-lists" class="btn-cancel">취소</a>
                            <button type="submit" class="btn-submit">작성</button>
                        </c:otherwise>
                    </c:choose>
                </div>
                
            </form>
        </div>
    </div>

    <script>
        const serverMsg = "${msg}";
        
        if (serverMsg !== "") {
            alert(serverMsg);
        }
        
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
            
            const encoder = new TextEncoder();
            const titleByteSize = encoder.encode(titleValue).length;
            const contentByteSize = encoder.encode(contentValue).length;

            if (titleByteSize > 90) {
                alert('제목이 너무 깁니다. 한글 기준 약 30자 이내로 작성해주세요. (최대 90바이트 / 현재'+ titleByteSize +'바이트)');
                titleInput.focus();
                return;
            }

            if (contentByteSize > 4000) {
                alert('내용이 너무 깁니다. 한글 기준 약 1333자 이내로 작성해주세요 (최대 4000바이트 / 현재'+contentByteSize+'바이트)');
                contentInput.focus();
                return;
            }
            
            const deleteCheckbox = document.getElementById('deleteExistingFiles');
            const isDeleteChecked = deleteCheckbox ? deleteCheckbox.checked : false;
            const existingFileCount = isDeleteChecked ? 0 : Number("${not empty post and not empty post.fileList ? post.fileList.size() : 0}");

            if (existingFileCount + files.length > 3) {
                alert('첨부파일은 기존 파일 포함 최대 3개까지만 가능합니다. (현재 유지 파일: ' + existingFileCount + '개, 신규 첨부: ' + files.length + '개)');
                return false;
            }

            const maxSize = 10 * 1024 * 1024;

            for (let i = 0; i < files.length; i++) {
                if (files[i].size > maxSize) {
                    alert('[' + files[i].name + '] 파일의 크기가 10MB를 초과합니다.');
                    return false;
                }
            }
            document.getElementById('postForm').submit();
        }
    </script>
</body>
</html>