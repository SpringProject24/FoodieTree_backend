package org.nmfw.foodietree.global.interceptor;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.entity.Customer;
import org.nmfw.foodietree.domain.customer.mapper.CustomerMapper;
import org.nmfw.foodietree.domain.customer.util.LoginUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CustomerInterceptor implements HandlerInterceptor {

	private final CustomerMapper customerMapper;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
		Object handler) throws Exception {
		Map<String, String> attribute = (Map<String, String>) request
			.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

		String found = attribute.get("customerId");
		if (found != null) {
			return true;
		}

		String loggedInUser = LoginUtil.getLoggedInUser(request.getSession());
		if (loggedInUser == null) {
			response.sendRedirect("/customer/sign-in?message=signin-required");
			return false;
		}
		log.info("{}", loggedInUser);
		Customer one = customerMapper.findOne(loggedInUser);

		if (one == null) {
			response.setStatus(403);
			response.sendRedirect("/access-deny?message=authorization");
			return false;
		}
		return true;
	}
}
