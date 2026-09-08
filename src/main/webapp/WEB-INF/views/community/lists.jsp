<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core"%> <%@ taglib prefix="spring"
uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>질문 게시판 - 목록</title>

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
        font-family:
          -apple-system, BlinkMacSystemFont, "Apple SD Gothic Neo",
          "Pretendard", Roboto, "Noto Sans KR", sans-serif;
      }

      .community-container {
        max-width: 1080px;
        margin: 40px auto 80px;
        padding: 0 16px;
        box-sizing: border-box;
      }

      .board-header {
        margin-bottom: 24px;
      }

      .board-header h3 {
        margin: 0 0 6px 0;
        font-size: 24px;
        font-weight: 800;
        color: #0f172a;
      }

      .board-header p {
        margin: 0;
        font-size: 14px;
        color: var(--text-secondary);
      }

      .board-header hr {
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
      }

      .filter-bar {
        padding: 16px 20px;
        margin-bottom: 16px;
        display: flex;
        flex-wrap: wrap;
        align-items: center;
        justify-content: space-between;
        gap: 16px;
      }

      .search-form {
        display: flex;
        align-items: center;
        gap: 16px;
        margin: 0;
        flex-wrap: wrap;
      }

      .radio-group {
        display: flex;
        align-items: center;
        gap: 12px;
        font-size: 13px;
        color: var(--text-secondary);
      }

      .form-check {
        display: flex;
        align-items: center;
        gap: 6px;
        margin: 0;
        cursor: pointer;
      }

      .form-check-input {
        appearance: none;
        -webkit-appearance: none;
        width: 16px;
        height: 16px;
        border: 1.5px solid #cbd5e1;
        border-radius: 50%;
        margin: 0;
        cursor: pointer;
        position: relative;
      }

      .form-check-input:checked {
        background-color: var(--color-primary);
        border-color: var(--color-primary);
      }

      .form-check-input:checked::after {
        content: "";
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        width: 6px;
        height: 6px;
        background-color: #ffffff;
        border-radius: 50%;
      }

      .form-check-label {
        cursor: pointer;
        font-weight: 500;
        color: var(--text-primary);
      }

      .search-input-group {
        display: flex;
        align-items: center;
        width: 270px;
      }

      .search-input {
        flex: 1;
        height: 38px;
        border: 1px solid var(--border-color);
        border-top-left-radius: 8px;
        border-bottom-left-radius: 8px;
        font-size: 13px;
        padding: 0 12px;
        outline: none;
        box-sizing: border-box;
        color: var(--text-primary);
      }

      .search-input:focus {
        border-color: var(--color-primary);
      }

      .btn-search {
        height: 38px;
        padding: 0 16px;
        background-color: #1e293b;
        border: 1px solid #1e293b;
        border-top-right-radius: 8px;
        border-bottom-right-radius: 8px;
        color: #ffffff;
        font-size: 13px;
        font-weight: 600;
        cursor: pointer;
        transition: background-color 0.15s;
      }

      .btn-search:hover {
        background-color: #0f172a;
      }

      .btn-write {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        height: 38px;
        padding: 0 16px;
        background-color: var(--color-primary);
        border: none;
        border-radius: 8px;
        color: #ffffff;
        font-size: 13px;
        font-weight: 600;
        text-decoration: none;
        cursor: pointer;
        transition: background-color 0.15s;
      }

      .btn-write:hover {
        background-color: #1d4ed8;
      }

      .table-card {
        overflow: hidden;
        margin-bottom: 24px;
      }

      .table-responsive {
        width: 100%;
        overflow-x: auto;
      }

      .custom-table {
        width: 100%;
        border-collapse: collapse;
        margin: 0;
        text-align: center;
      }

      .custom-table thead th {
        background-color: #ffffff;
        color: var(--text-secondary);
        font-weight: 600;
        font-size: 13px;
        border-bottom: 1px solid var(--border-color);
        padding: 14px 12px;
      }

      .custom-table tbody td {
        padding: 16px 12px;
        border-bottom: 1px solid #f1f5f9;
        font-size: 14px;
        vertical-align: middle;
      }

      .custom-table tbody tr:last-child td {
        border-bottom: none;
      }

      .custom-table tbody tr:hover td {
        background-color: #f8fafc;
      }

      .post-title-link {
        text-decoration: none;
        color: var(--text-primary);
        font-weight: 500;
        transition: color 0.15s;
      }

      .post-title-link:hover {
        color: var(--color-primary);
      }

      .category-badge {
        font-size: 12px;
        font-weight: 600;
        padding: 4px 8px;
        border-radius: 6px;
        background-color: #f1f5f9;
        color: var(--text-secondary);
      }

      .text-start {
        text-align: left;
      }

      .ps-4 {
        padding-left: 24px;
      }

      .text-secondary {
        color: var(--text-secondary);
      }

      .small {
        font-size: 13px;
      }

      .text-center {
        text-align: center;
      }

      .py-5 {
        padding-top: 48px;
        padding-bottom: 48px;
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
        min-width: 34px;
        height: 34px;
        padding: 0 8px;
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

    <div class="community-container">
      <c:if test="${not empty msg}">
        <script>
          alert("${msg}");
        </script>
      </c:if>

      <div class="board-header">
        <h3>질문 게시판</h3>
        <p>투자 아이디어와 궁금한 점을 한눈에 확인하고 자유롭게 공유하세요.</p>
        <hr />
      </div>

      <div class="custom-card filter-bar">
        <div
          style="
            display: flex;
            flex-wrap: wrap;
            align-items: center;
            justify-content: space-between;
            width: 100%;
            gap: 16px;
          ">
          <form onsubmit="searchPosts(event)" method="get" class="search-form">
            <div class="radio-group">
              <div class="form-check">
                <input
                  class="form-check-input"
                  type="radio"
                  name="type"
                  id="postT"
                  value="0"
                  checked />
                <label class="form-check-label" for="postT">제목</label>
              </div>
              <div class="form-check">
                <input
                  class="form-check-input"
                  type="radio"
                  name="type"
                  id="postW"
                  value="1" />
                <label class="form-check-label" for="postW">작성자</label>
              </div>
            </div>

            <div class="search-input-group">
              <input
                type="text"
                class="search-input"
                name="keyword"
                id="keyword"
                placeholder="검색어를 입력하세요" />
              <button class="btn-search" type="submit">검색</button>
            </div>
          </form>

          <div>
            <a href="/community-lists/write" class="btn-write"> + 글작성 </a>
          </div>
        </div>
      </div>

      <div class="custom-card table-card">
        <div class="table-responsive">
          <table class="custom-table">
            <thead>
              <tr>
                <th style="width: 10%">번호</th>
                <th style="width: 14%">카테고리</th>
                <th style="width: 46%" class="text-start ps-4">제목</th>
                <th style="width: 12%">작성자</th>
                <th style="width: 8%">조회수</th>
                <th style="width: 10%">작성일</th>
              </tr>
            </thead>
            <tbody id="postTableBody"></tbody>
          </table>
        </div>
      </div>

      <nav aria-label="Page navigation" class="pagination-nav">
        <ul id="paging" class="pagination"></ul>
      </nav>
    </div>

    <script>
      window.onload = function () {
        loadPosts(1);
      };

      function searchPosts(event) {
        event.preventDefault();
        loadPosts(1);
      }

      function loadPosts(page) {
        const keyword = document.getElementById("keyword").value;
        const type = document.querySelector('input[name="type"]:checked').value;

        fetch(
          `/api/community-lists/post-list?page=\${page}&keyword=\${keyword}&type=\${type}`,
          {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
            },
            body: null,
          },
        )
          .then((response) => response.json())
          .then((postList) => {
            if (postList.code == "suc_002") {
              renderTable([]);
              clearPaging();
              return;
            }
            if (postList.code !== "suc_001") {
              alert("데이터를 불러오지 못했습니다.");
              return;
            }

            renderTable(postList.data.list);
            renderPaging(postList.data);
          });
      }

      function renderTable(postList) {
        const postTableBody = document.getElementById("postTableBody");
        let html = "";

        if (!postList || postList.length === 0) {
          postTableBody.innerHTML =
            '<tr><td colspan="6" class="text-center py-5 text-secondary small">등록된 게시글이 없습니다.</td></tr>';
          return;
        }

        postList.forEach((post) => {
          let category =
            post.postCategoryNum == 1
              ? "자유"
              : post.postCategoryNum == 2
                ? "정보"
                : "질문";
          html += `
                    <tr>
                        <td class="text-secondary small">\${post.postNum}</td>
                        <td><span class="category-badge">\${category}</span></td>
                        <td class="text-start ps-4">
                            <a class="post-title-link" href="/community-lists/detail?postNum=\${post.postNum}">\${post.postTitle}</a>
                        </td>
                        <td class="text-secondary small">\${post.userName}</td>
                        <td class="text-secondary small">\${post.postViewCnt}</td>
                        <td class="text-secondary small">\${post.postedDateString}</td>
                    </tr>
                `;
        });
        postTableBody.innerHTML = html;
      }

      function clearPaging() {
        const paging = document.getElementById("paging");
        let html = "";

        html += `
                <div class="text-center py-5 text-secondary">
                    <p class="small" style="margin: 0;">조회된 게시글이 없습니다.</p>
                </div>
            `;
        paging.innerHTML = html;
      }

      function renderPaging(pageInfo) {
        const paging = document.getElementById("paging");
        let html = "";

        if (pageInfo.startPage > 1) {
          html += `
                    <li class="page-item">
                        <a class="page-link" href="#" onclick="loadPosts(\${pageInfo.startPage - 1}); return false;">‹</a>
                    </li>
                `;
        }

        for (let i = pageInfo.startPage; i <= pageInfo.endPage; i++) {
          let active = i === pageInfo.currentPage ? "active" : "";
          html += `
                    <li class="page-item \${active}">
                        <a class="page-link" href="#" onclick="loadPosts(\${i}); return false;">\${i}</a>
                    </li>
                `;
        }

        if (pageInfo.endPage < pageInfo.totalPages) {
          html += `
                    <li class="page-item">
                        <a class="page-link" href="#" onclick="loadPosts(\${pageInfo.endPage + 1}); return false;">›</a>
                    </li>
                `;
        }

        paging.innerHTML = html;
      }
    </script>
  </body>
</html>
