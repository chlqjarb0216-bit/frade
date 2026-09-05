package com.frade.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.frade.dto.user.UserSessionDTO;

public class LoginManager {

    public static final String SESSION_LOGIN_USER_KEY = "loginUser";

    // 로그인 유저 세션 저장
    public static void setSessionLoginUser(
            HttpSession session, UserSessionDTO loginUser) {

        session.setAttribute(SESSION_LOGIN_USER_KEY, loginUser);
    }

    public static void setSessionLoginUser(
            HttpServletRequest request, UserSessionDTO loginUser) {

        setSessionLoginUser(request.getSession(), loginUser);
    }

    // 로그인 유저 가져오기
    public static UserSessionDTO getLoginUser(HttpSession session) {

        return (UserSessionDTO)
                session.getAttribute(SESSION_LOGIN_USER_KEY);
    }

    public static UserSessionDTO getLoginUser(
            HttpServletRequest request) {

        return getLoginUser(request.getSession());
    }

    // 로그인 여부 확인
    public static boolean isLogin(HttpSession session) {

        if(session.getAttribute(SESSION_LOGIN_USER_KEY) != null) {
            return true;
        }

        return false;
    }

    public static boolean isLogin(HttpServletRequest request) {

        return isLogin(request.getSession());
    }

    // 로그아웃
    public static void logout(HttpSession session) {

        session.invalidate();
    }

    public static void logout(HttpServletRequest request) {

        logout(request.getSession());
    }
    
 // 로그인 유저 번호 가져오기
    public static int getLoginUserNum(HttpSession session) {

        UserSessionDTO loginUser = getLoginUser(session);

        return loginUser.getUserNum();
    }

    public static int getLoginUserNum(HttpServletRequest request) {

        return getLoginUserNum(request.getSession());
    }
   
    
    
}