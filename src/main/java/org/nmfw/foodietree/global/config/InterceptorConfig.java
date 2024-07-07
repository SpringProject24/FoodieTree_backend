package org.nmfw.foodietree.global.config;

import lombok.RequiredArgsConstructor;
import org.nmfw.foodietree.global.interceptor.AfterLoginInterceptor;
import org.nmfw.foodietree.global.interceptor.AutoLoginInterceptor;
import org.nmfw.foodietree.global.interceptor.CustomerInterceptor;
import org.nmfw.foodietree.global.interceptor.StoreInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


// 만들어 놓은 인터셉터들을 스프링 컨텍스트에 등록하는 설정 파일
@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

	private final AfterLoginInterceptor afterLoginInterceptor;
	private final AutoLoginInterceptor autoLoginInterceptor;
	private final CustomerInterceptor customerInterceptor;
	private final StoreInterceptor storeInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry
			.addInterceptor(afterLoginInterceptor)
			.addPathPatterns("/customer/sign-up", "/customer/sign-in", "/store/sign-up",
				"/store/sign-in")
		;

		// 자동로그인 인터셉터 등록
		registry
			.addInterceptor(autoLoginInterceptor)
			.addPathPatterns("/**");

		registry
			.addInterceptor(customerInterceptor)
			.addPathPatterns("/customer/**")
			.excludePathPatterns("/customer/sign-in", "/customer/sign-up");

		registry
			.addInterceptor(storeInterceptor)
			.addPathPatterns("/store/**")
			.excludePathPatterns("/store/sign-in", "/store/sign-up");
	}
}
