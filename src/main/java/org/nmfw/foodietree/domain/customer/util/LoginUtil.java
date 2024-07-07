package org.nmfw.foodietree.domain.customer.util;


import lombok.extern.java.Log;
import org.nmfw.foodietree.domain.customer.dto.resp.LoginUserInfoDto;
import org.nmfw.foodietree.domain.store.dto.request.LoginStoreInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginUtil {

    public static final String LOGIN = "login";

    public static final String AUTO_LOGIN_COOKIE = "auto";
    private static final Logger log = LoggerFactory.getLogger(LoginUtil.class);

    // 로그인 여부 확인
    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(LOGIN) != null;
    }

    // 로그인한 회원의 계정명 얻기
//    public static String getLoggedInUserAccount(HttpSession session) {
//        LoginUserInfoDto loggedInUser = getLoggedInUser(session);
//        return loggedInUser != null ? loggedInUser.getCustomerId() : null;
//    }

    public static String getLoggedInUser(HttpSession session) {
        Object attribute = session.getAttribute(LOGIN);
        if (attribute instanceof LoginUserInfoDto) {
            LoginUserInfoDto obj = (LoginUserInfoDto) attribute;
            return obj.getCustomerId();
        } else if (attribute instanceof LoginStoreInfoDto) {
            LoginStoreInfoDto obj = (LoginStoreInfoDto) attribute;
            return obj.getStoreId();
        }
        return null;
    }

    public static boolean isAutoLogin(HttpServletRequest request) {
        Cookie autoLoginCookie = WebUtils.getCookie(request, AUTO_LOGIN_COOKIE);
        return autoLoginCookie != null;
    }
}