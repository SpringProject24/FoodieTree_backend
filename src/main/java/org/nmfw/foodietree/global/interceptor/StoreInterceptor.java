package org.nmfw.foodietree.global.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.nmfw.foodietree.domain.customer.util.LoginUtil;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.nmfw.foodietree.domain.store.mapper.StoreMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

@Configuration
@RequiredArgsConstructor
public class StoreInterceptor implements HandlerInterceptor {

	private final StoreMapper storeMapper;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
		Object handler) throws Exception {
		String loggedInUser = LoginUtil.getLoggedInUser(request.getSession());
		if (loggedInUser != null) {
			response.sendRedirect("/store/sign-in?message=signin-required");
			return false;
		}
		Store one = storeMapper.findOne(loggedInUser);
		if (one == null) {
			response.setStatus(403);
			response.sendRedirect("/access-deny?message=authorization");
			return false;
		}
		return true;
	}
}
