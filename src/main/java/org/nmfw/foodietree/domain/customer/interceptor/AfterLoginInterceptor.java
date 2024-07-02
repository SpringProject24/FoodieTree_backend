package org.nmfw.foodietree.domain.customer.interceptor;


import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.util.LoginUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@Slf4j
public class AfterLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("after login interceptor execute!");
        if (LoginUtil.isLoggedIn(request.getSession())) {
            response.sendRedirect("/");
            return false; //  true일 경우 컨트롤러 진입 허용, false 일 경우 진입 차단
        }
        return true;
    }
}
