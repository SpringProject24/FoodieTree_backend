package org.nmfw.foodietree.global.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.nmfw.foodietree.domain.customer.util.LoginUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

@Configuration
@RequiredArgsConstructor
public class AfterLoginInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
		Object handler) throws Exception {
		if (LoginUtil.isLoggedIn(request.getSession())) {
			response.sendRedirect("/");
			return false;
		}
		return true;
	}
}
