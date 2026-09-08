<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>${post.postTitle} - 상세조회</title>

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

.detail-container {
    max-width: 1080px;
    margin: 40px auto 80px;
    padding: 0 16px;
    box-sizing: border-box;
}

.top-nav-bar {
    display: flex;
    align-items: center;
    justify-content: space-between;
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
    transition: all 0.15s;
}

.btn-back:hover {
    background-color: #ffffff;
    color: var(--text-primary);
}

.action-buttons {
    display: flex;
    align-items: center;
    gap: 8px;
}

.btn-edit {
    display: inline-flex;
    align-items: center;
    padding: 7px 14px;
    font-size: 13px;
    font-weight: 600;
    color: var(--text-secondary);
    background-color: #ffffff;
    border: 1px solid var(--border-color);
    border-radius: 8px;
    text-decoration: none;
    transition: all 0.15s;
}

.btn-edit:hover {
    background-color: #f1f5f9;
    color: var(--text-primary);
}

.btn-delete {
    padding: 7px 14px;
    font-size: 13px;
    font-weight: 600;
    color: #ef4444;
    background-color: #ffffff;
    border: 1px solid #fecaca;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.15s;
}

.btn-delete:hover {
    background-color: #fef2f2;
}

.custom-card {
    background-color: var(--card-bg);
    border: 1px solid var(--border-color);
    border-radius: var(--radius-card);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.03);
    padding: 32px;
    margin-bottom: 24px;
    box-sizing: border-box;
}

.post-header {
    border-bottom: 1px solid var(--border-color);
    padding-bottom: 18px;
    margin-bottom: 24px;
}

.category-badge {
    display: inline-block;
    font-size: 12px;
    font-weight: 600;
    padding: 4px 8px;
    border-radius: 6px;
    background-color: #f1f5f9;
    color: var(--text-secondary);
    margin-bottom: 10px;
}

.post-title {
    font-size: 22px;
    font-weight: 800;
    color: #0f172a;
    margin: 0 0 14px 0;
    line-height: 1.4;
}

.post-meta-row {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    justify-content: space-between;
    font-size: 13px;
    color: var(--text-secondary);
    gap: 12px;
}

.meta-left {
    display: flex;
    align-items: center;
    gap: 8px;
}

.meta-author {
    font-weight: 600;
    color: var(--text-primary);
}

.meta-right {
    display: flex;
    align-items: center;
    gap: 14px;
}

.post-content {
    font-size: 15px;
    line-height: 1.8;
    min-height: 200px;
    white-space: pre-wrap;
    word-break: break-all;
    color: var(--text-primary);
    margin-bottom: 28px;
}

.attachment-details {
    background-color: #f8fafc;
    border: 1px solid var(--border-color);
    border-radius: 10px;
    padding: 16px;
    margin-bottom: 28px;
}

.attachment-summary {
    font-size: 13px;
    font-weight: 600;
    color: var(--text-secondary);
    cursor: pointer;
    user-select: none;
}

.attachment-content {
    margin-top: 14px;
    padding-top: 14px;
    border-top: 1px solid var(--border-color);
    display: flex;
    flex-direction: column;
    gap: 14px;
}

.img-preview-wrap {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
}

.img-preview-wrap img {
    max-width: 100%;
    max-height: 500px;
    object-fit: contain;
    border-radius: 8px;
    border: 1px solid var(--border-color);
}

.file-download-link {
    display: inline-flex;
    align-items: center;
    gap: 4px;
    font-size: 12px;
    color: var(--text-secondary);
    text-decoration: none;
    margin-top: 6px;
}

.file-download-link:hover {
    color: var(--color-primary);
}

.file-card-box {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 12px 16px;
    background-color: #ffffff;
    border: 1px solid var(--border-color);
    border-radius: 8px;
}

.file-card-info {
    overflow: hidden;
    margin-right: 16px;
}

.file-name-text {
    font-size: 13px;
    font-weight: 600;
    color: var(--text-primary);
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.file-card-actions {
    display: flex;
    gap: 8px;
    flex-shrink: 0;
}

.btn-file-preview {
    padding: 6px 12px;
    font-size: 12px;
    font-weight: 600;
    color: var(--text-secondary);
    background-color: #ffffff;
    border: 1px solid var(--border-color);
    border-radius: 6px;
    text-decoration: none;
}

.btn-file-download {
    padding: 6px 12px;
    font-size: 12px;
    font-weight: 600;
    color: var(--color-primary);
    background-color: #eff6ff;
    border: 1px solid #bfdbfe;
    border-radius: 6px;
    text-decoration: none;
}

.like-area {
    display: flex;
    justify-content: center;
    padding-top: 24px;
    border-top: 1px solid var(--border-color);
}

.btn-like {
    display: inline-flex;
    align-items: center;
    gap: 8px;
    padding: 10px 24px;
    border-radius: 30px;
    background-color: #ffffff;
    border: 1px solid var(--border-color);
    color: var(--text-primary);
    font-size: 14px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.15s;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.02);
}

.btn-like:hover {
    background-color: #f8fafc;
    border-color: #cbd5e1;
}

.badge-like-count {
    background-color: #1e293b;
    color: #ffffff;
    padding: 2px 8px;
    border-radius: 12px;
    font-size: 12px;
    margin-left: 4px;
}

.comment-card-header {
    border-bottom: 1px solid var(--border-color);
    padding-bottom: 12px;
    margin-bottom: 20px;
}

.comment-card-title {
    font-size: 16px;
    font-weight: 700;
    color: #0f172a;
    margin: 0;
}

.comment-form-group {
    display: flex;
    flex-direction: column;
    gap: 10px;
    margin-bottom: 24px;
}

.custom-textarea {
    width: 100%;
    border: 1px solid var(--border-color);
    border-radius: 10px;
    font-size: 14px;
    padding: 12px 14px;
    resize: none;
    outline: none;
    box-sizing: border-box;
    font-family: inherit;
    color: var(--text-primary);
    background-color: #ffffff;
}

.custom-textarea:focus {
    border-color: var(--color-primary);
}

.custom-textarea[readonly] {
    background-color: #f8fafc;
    cursor: pointer;
}

.comment-submit-row {
    display: flex;
    justify-content: flex-end;
}

.btn-comment-submit {
    height: 36px;
    padding: 0 20px;
    background-color: #1e293b;
    border: none;
    border-radius: 8px;
    color: #ffffff;
    font-size: 13px;
    font-weight: 600;
    cursor: pointer;
    transition: background-color 0.15s;
}

.btn-comment-submit:hover {
    background-color: #0f172a;
}

.btn-comment-login {
    display: inline-flex;
    align-items: center;
    height: 36px;
    padding: 0 20px;
    background-color: #ffffff;
    border: 1px solid var(--border-color);
    border-radius: 8px;
    color: var(--text-primary);
    font-size: 13px;
    font-weight: 600;
    text-decoration: none;
}

.comment-item {
    padding: 16px 0;
    border-bottom: 1px solid #f1f5f9;
}

.comment-item:last-child {
    border-bottom: none;
}

.comment-meta-row {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 6px;
}

.comment-author-wrap {
    display: flex;
    align-items: center;
}

.comment-author {
    font-size: 13px;
    font-weight: 600;
    color: var(--text-primary);
}

.comment-date {
    font-size: 12px;
    color: var(--text-muted);
}

.comment-body {
    margin: 0;
    font-size: 14px;
    color: var(--text-primary);
    white-space: pre-wrap;
    line-height: 1.5;
}

.btn-comment-delete {
    background: none;
    border: none;
    color: #ef4444;
    font-size: 12px;
    cursor: pointer;
    padding: 0;
    margin-left: 8px;
}

.pagination-nav {
    margin-top: 24px;
}

.pagination {
    display: flex;
    justify-content: center;
    list-style: none;
    padding: 0;
    margin: 0;
    gap: 4px;
}

.pagination .page-item {
    display: inline-block;
}

.pagination .page-link {
    display: flex;
    align-items: center;
    justify-content: center;
    min-width: 32px;
    height: 32px;
    padding: 0 6px;
    color: var(--text-secondary);
    background-color: #ffffff;
    border: 1px solid var(--border-color);
    border-radius: 8px;
    font-size: 13px;
    text-decoration: none;
    box-sizing: border-box;
    cursor: pointer;
    transition: all 0.15s;
}

.pagination .page-link:hover {
    background-color: #f1f5f9;
    color: var(--text-primary);
}

.pagination .page-item.active .page-link {
    background-color: var(--color-primary);
    border-color: var(--color-primary);
    color: #ffffff;
    font-weight: 600;
}
</style>
</head>

<body>

    <jsp:include page="../common/navbar.jsp"></jsp:include>

    <div class="detail-container">
        
        <div class="top-nav-bar">
            <a href="/community-lists" class="btn-back">
                ← 목록으로
            </a>
            <c:if test="${not empty sessionScope.loginUser and sessionScope.loginUser.userNum == post.userNum}">
                <div class="action-buttons">
                    <a href="/community-lists/edit?postNum=${post.postNum}" class="btn-edit">수정</a>

                    <form action="/community-lists/delete" method="post" onsubmit="return confirm('정말 삭제하시겠습니까?');" style="display: inline; margin: 0;">
                        <input type="hidden" name="postNum" value="${post.postNum}">
                        <button type="submit" class="btn-delete">삭제</button>
                    </form>
                </div>
            </c:if>
        </div>

        <div class="custom-card">
            
            <div class="post-header">
                <div>
                    <span class="category-badge">
                        <c:choose>
                            <c:when test="${post.postCategoryNum == 1}">자유</c:when>
                            <c:when test="${post.postCategoryNum == 2}">정보</c:when>
                            <c:when test="${post.postCategoryNum == 3}">질문</c:when>
                            <c:otherwise>커뮤니티</c:otherwise>
                        </c:choose>
                    </span>
                </div>
                <h3 class="post-title">${post.postTitle}</h3>
                <div class="post-meta-row">
                    <div class="meta-left">
                        <span class="meta-author">${post.userName}</span>
                        <span>•</span>
                        <span>${post.postedDateString}</span>
                    </div>
                    <div class="meta-right">
                        <span>조회 ${post.postViewCnt}</span>
                        <span>추천 ${post.postLikeCnt}</span>
                    </div>
                </div>
            </div>

            <div class="post-content"><c:out value="${post.postContent}"/></div>

            <c:if test="${not empty post.fileList}">
                <details class="attachment-details">
                    <summary class="attachment-summary">📎 첨부파일 보기 (${post.fileList.size()})</summary>
                    <div class="attachment-content">
                        <c:forEach var="fileName" items="${post.fileList}">
                            <c:set var="lowerName" value="${fn:toLowerCase(fileName)}" />
                            <c:choose>
                                <c:when test="${fn:endsWith(lowerName, '.jpg') or fn:endsWith(lowerName, '.jpeg') or fn:endsWith(lowerName, '.png') or fn:endsWith(lowerName, '.gif') or fn:endsWith(lowerName, '.webp') or fn:endsWith(lowerName, '.bmp') or fn:endsWith(lowerName, '.svg')}">
                                    <div class="img-preview-wrap">
                                        <a href="/file-storage/post_uploadfile/${fileName}" target="_blank" title="새 탭에서 원본 보기">
                                            <img src="/file-storage/post_uploadfile/${fileName}" alt="${fileName}"/>
                                        </a>
                                        <a href="/file-storage/post_uploadfile/${fileName}" download="${fileName}" class="file-download-link">
                                            ⬇️ ${fileName} 다운로드
                                        </a>
                                    </div>
                                </c:when>

                                <c:otherwise>
                                    <div class="file-card-box">
                                        <div class="file-card-info">
                                            <div class="file-name-text">${fileName}</div>
                                            <small style="color: var(--text-muted); font-size: 11px;">일반 첨부파일</small>
                                        </div>
                                        <div class="file-card-actions">
                                            <c:if test="${fn:endsWith(lowerName, '.pdf')}">
                                                <a href="/file-storage/post_uploadfile/${fileName}" target="_blank" class="btn-file-preview">
                                                    미리보기
                                                </a>
                                            </c:if>
                                            <a href="/file-storage/post_uploadfile/${fileName}" download="${fileName}" class="btn-file-download">
                                                ⬇️ 다운로드
                                            </a>
                                        </div>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </div>
                </details>
            </c:if>

            <div class="like-area">
                <button type="button" class="btn-like" id="btnPostLike">
                    <span>👍</span>
                    <span>추천</span>
                    <span class="badge-like-count" id="postLikeCount">${post.postLikeCnt}</span>
                </button>
            </div>

        </div>

        <div class="custom-card">
            
            <div class="comment-card-header">
                <h6 class="comment-card-title">
                    💬 댓글 <span style="color: var(--color-primary);" id="commentCount">0</span>
                </h6>
            </div>

            <div style="margin-bottom: 24px;">
                <c:choose>
                    <c:when test="${not empty sessionScope.loginUser}">
                        <div class="comment-form-group">
                            <textarea id="commentContent" class="custom-textarea" rows="3" placeholder="댓글을 입력하세요 (최대 100자)"></textarea>
                            <div class="comment-submit-row">
                                <button type="button" class="btn-comment-submit" onclick="submitComment()">작성</button>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="comment-form-group">
                            <textarea id="commentContent" class="custom-textarea" rows="3" placeholder="로그인 후 댓글을 작성할 수 있습니다." readonly onclick="if(confirm('로그인이 필요한 서비스입니다. 로그인 페이지로 이동하시겠습니까?')) location.href='/user/login';"></textarea>
                            <div class="comment-submit-row">
                                <a href="/user/login" class="btn-comment-login">로그인</a>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <div id="commentList" style="display: flex; flex-direction: column;">
            </div>

            <nav aria-label="Page navigation" class="pagination-nav">
                <ul id="paging" class="pagination">
                </ul>
            </nav>

        </div>

    </div>

    <script>
        const postNum = "${post.postNum}";
        const loginUserNum = ${not empty sessionScope.loginUser ? sessionScope.loginUser.userNum : -1};

        window.onload = function(){
            loadComments(1);
        }
        
        function loadComments(page){
            fetch(`/api/community-lists/comment-list?page=\${page}&postNum=\${postNum}`,{
                method: 'GET',
                headers:{
                    'Content-Type' : 'application/json'
                }
            })
            .then(response => response.json())
            .then(commentList=>{
                if(commentList.code === "suc_002"){
                    document.getElementById('commentCount').innerText = 0;
                    renderCommentList([]);
                    document.getElementById('paging').innerHTML = '';
                    return;
                }
                if(commentList.code !== "suc_001") {
                    alert("댓글 데이터를 불러오지 못했습니다.");
                    return;
                }
                
                console.log("받아온 댓글 리스트:" , commentList.data);
                
                document.getElementById('commentCount').innerText = commentList.data.totalCount;
                
                renderCommentList(commentList.data.list);
                renderPaging(commentList.data);
            });
        }
        
        function renderCommentList(commentList){
            const commentTable = document.getElementById('commentList');
            let html = '';
            
            if(!commentList || commentList.length === 0){
                commentTable.innerHTML = '<div style="text-align: center; padding: 32px 0; color: var(--text-secondary); font-size: 13px;">등록된 댓글이 없습니다.</div>';
                return;
            }
            
            commentList.forEach(comment =>{
                const dateStr = comment.commentedDateString || '';
                const isMyComment = (loginUserNum !== -1 && comment.userNum === loginUserNum);
                const deleteBtn = isMyComment ? `
                    <button type="button" class="btn-comment-delete" onclick="deleteComment(\${comment.commentNum})">삭제</button>
                ` : '';

                html += `
                    <div class="comment-item">
                        <div class="comment-meta-row">
                            <div class="comment-author-wrap">
                                <span class="comment-author">\${comment.userName}</span>
                                \${deleteBtn}
                            </div>
                            <span class="comment-date">\${dateStr}</span>
                        </div>
                        <p class="comment-body">\${comment.commentContent}</p>
                    </div>
                `;
            });
            
            commentTable.innerHTML = html;
        }
        
        function renderPaging(pageInfo){
            const paging = document.getElementById('paging');
            let html = '';

            if (pageInfo.startPage > 1) {
                html += `
                    <li class="page-item">
                        <a class="page-link" href="#" onclick="loadComments(\${pageInfo.startPage - 1}); return false;">‹</a>
                    </li>
                `;
            }

            for (let i = pageInfo.startPage; i <= pageInfo.endPage; i++) {
                let active = (i === pageInfo.currentPage) ? "active" : "";
                html += `
                    <li class="page-item \${active}">
                        <a class="page-link" href="#" onclick="loadComments(\${i}); return false;">\${i}</a>
                    </li>
                `;
            }

            if (pageInfo.endPage < pageInfo.totalPages) {
                html += `
                    <li class="page-item">
                        <a class="page-link" href="#" onclick="loadComments(\${pageInfo.endPage + 1}); return false;">›</a>
                    </li>
                `;
            }

            paging.innerHTML = html;
        }
        
        function submitComment(){
            if (loginUserNum === -1) {
                if (confirm("댓글 작성은 로그인이 필요한 서비스입니다. 로그인 페이지로 이동하시겠습니까?")) {
                    location.href = "/user/login";
                }
                return;
            }
            const contentInput = document.getElementById('commentContent');
            const content = contentInput.value.trim();
            
            if (content.length > 100) {
                alert("댓글은 최대 100자까지만 작성할 수 있습니다.");
                contentInput.focus();
                return;
            }
            
            if(content == ""){
                alert("댓글 내용을 입력해주세요.");
                contentInput.focus();
                return;
            }
            
            const requestData = {
                    postNum: postNum,
                    commentContent: content
            };
            
            fetch(`/api/community-lists/comment-write`,{
                method : 'POST',
                headers:{
                    'Content-Type' : 'application/json'
                },
                body: JSON.stringify(requestData)
            })
            .then(response =>response.json())
            .then(writeInfo=>{
                if(writeInfo.code === "suc_001"){
                    contentInput.value = '';
                    loadComments(1);
                } else{
                    alert(writeInfo.message);
                }
            });
        }

        function deleteComment(commentNum){
            if(!confirm('정말 삭제하시겠습니까?')){
                return;
            }

            fetch(`/api/community-lists/comment-delete`,{
                method: 'POST',
                headers:{
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: `commentNum=\${commentNum}`
            })
            .then(response => response.json())
            .then(res =>{
                if(res.code === "suc_001"){
                    loadComments(1);
                } else{
                    alert(res.message || "댓글 삭제에 실패했습니다.");
                }
            })
            .catch(err =>{
                console.error("댓글 삭제 에러", err);
                alert("댓글 삭제 중 오류가 발생했습니다.");
            });
        }
    </script>
</body>

</html>