<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="/resources/css/navbar.css">

<div class="nav-container">
    
    <div class="nav-left">
        <a href="/main" class="nav-logo-box">
            <img src="/resources/images/logo.png" alt="Frade-logo" class="nav-logo-img">
        </a>
        <ul class="nav-links">
            <li><a href="/trading">트레이딩</a></li>
            <li class="bar">|</li>
            <li><a href="/news/list">뉴스</a></li>
            <li class="bar">|</li>
            <li><a href="/community-lists">커뮤니티</a></li>
            <li class="bar">|</li>
            <li><a href="/ranking">랭킹</a></li>
        </ul>
    </div>

    
    <div class="nav-right-box">
        <div class="nav-top-auth">
            <c:choose>
                <c:when test="${empty sessionScope.loginUser}">
                    <a href="/user/login">로그인</a> | <a href="/user/signup">회원가입</a>
                </c:when>
                <c:otherwise>
                    <a href="#">알림</a> | <a href="/user/mypage">마이페이지</a> | <a href="/user/logout">로그아웃</a>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="nav-profile-box">
            <div class="nav-profile-img-circle"></div>
            <div class="nav-profile-txt">
                <span>${empty sessionScope.loginUser.tier ? '티어' : sessionScope.loginUser.tier}</span>
                <strong>${empty sessionScope.loginUser.userNick ? '닉네임' : sessionScope.loginUser.userNick}</strong>
            </div>
        </div>
    </div>
</div>
