package org.nmfw.foodietree.domain.customer.interceptor;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.entity.Customer;
import org.nmfw.foodietree.domain.customer.mapper.CustomerMapper;
import org.nmfw.foodietree.domain.customer.service.CustomerService;
import org.nmfw.foodietree.domain.customer.util.LoginUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class AutoLoginInterceptor implements HandlerInterceptor {

    private final CustomerMapper customerMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //1. 사이트에 들어오면 자동로그인 쿠키를 가지고 있는지 확인
        Cookie autoLoginCookie = WebUtils.getCookie(request, LoginUtil.AUTO_LOGIN_COOKIE);

        //2. 자동로그인 쿠키가 있으면 사이트 로그인 처리를 수행
        if (autoLoginCookie != null && !LoginUtil.isLoggedIn(request.getSession())) {
            // 3. 쿠키에 들어있는 랜덤값을 읽기
            String sessionId = autoLoginCookie.getValue();
            // 4. 세션아이디로 회원정보 조회
            Customer foundCustomer = customerMapper.findCustomerBySession(sessionId);

            // 5. 회원이 정상조회되었고 자동로그인 만료시간 이전이라면
            // 사이트 로그인 처리(세션에 DTO세팅)를 수행
            if (foundCustomer != null
                    && LocalDateTime.now().isBefore(foundCustomer.getLimitTime())) {

                CustomerService.maintainLoginState(request.getSession(), foundCustomer);
            }
        }

        return true;
    }
}