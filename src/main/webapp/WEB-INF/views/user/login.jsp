<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />

        <!-- 스마트폰 등의 환경에서 원래 크기로 보이도록 -->
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />

        <title>로그인</title>

        <style>

            * {
                box-sizing: border-box;
                margin: 0;
                height: 0;
            }

            .body {
                font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Noto Sans KR", sans-serif;
                background-color: #ffffff;
                color: #333;
            }

            .login-container {
                width: 100%;
                max-width: 360px;
                margin: 80px auto 120px;
                padding: 0 20px;
            }

            .login-title {
                text-align: center;
                font-size: 24px;
                font-weight: 700;
                color: #222;
                margin-bottom: 36px;
                letter-spacing: -0.5px;
            }

            .login-form-box {
                display: flex;
                flex-direction: column;
                gap: 12px;
            }

            .input-box {
                width: 100%;
                height: 52px;
                padding: 0 18px;
                border: 1px solid #e2e2e2;
                border-radius: 6px;
                font-size: 14px;
                color: #333;
                outline: none;
                transition: border-color 0.2s ease;
            }

            .imput-box:focus {
                border-color: #111;
            }

            .error-msg {
                color: red;
                font-size: 13px;
                line-height: 1.4;
                margin-top: 4px;
                padding-left: 4px;
            }

            .login-btn {
                width: 100%;
                height: 54px;
                background-color: #3182f6;
                color: #ffffff;
                border: 1px solid #3182f6;
                border-radius: 6px;
                font-size: 16px;
                font-weight: 600;
                cursor: pointer;
                margin-top: 12px;
                transition: border-color 0.2s;
            }

            .login-btn:hover {
                background-color: #1b64da;

            }

            .signup-btn {
                display: flex;
                align-items: center;
                justify-content: center;
                width: 100%;
                height: 54px;
                background-color: #ffffff;
                color: #3182f6;
                border: 1px solid #3182f6;
                border-radius: 6px;
                font-size: 16px;
                font-weight: 600;
                text-decoration: none;
                transition: background-color 0.2s, border-color 0.2s;
            }

            .signup-btn:hover {
                background-color: #f0f7ff;
            }


        </style>
    </head>
    <body>

        <jsp:include page="../common/navbar.jsp"></jsp:include>

        <div class="login-container">
            <h1 class="login-title">로그인</h1>
            <form action="" method="post" class="login-form-box">
                
                <input type="text" name="userId" class="input-box" placeholder="아이디를 입력해 주세요."/>

                <input type="password" name="userPw" class="input-box" placeholder="비밀번호를 입력해 주세요."/>
                

                <c:if test="${not empty loginValidationFail}">
                        <p class="error-msg">${loginValidationFail}</p>
                </c:if>

                <c:if test="${loginfail == 'true'}">
                    <p class="error-msg">아이디 또는 비밀번호거 일치하지 않습니다.</p>
                </c:if>


                <button type="submit" class="login-btn">로그인</button>
                <a href="/user/signup" class="signup-btn">회원가입</a>
                
            </form>
        </div>
        <c:if test="${not empty signupSuccess}">
                    <script>
                        alert("${signupSuccess}");
                    </script>

        </c:if>
    </body>
</html>
