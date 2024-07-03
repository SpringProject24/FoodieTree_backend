package org.nmfw.foodietree.domain.customer.service;

import static org.nmfw.foodietree.domain.customer.service.LoginResult.NO_ID;
import static org.nmfw.foodietree.domain.customer.service.LoginResult.NO_PW;
import static org.nmfw.foodietree.domain.customer.service.LoginResult.SUCCESS;

import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.request.AutoLoginDto;
import org.nmfw.foodietree.domain.customer.dto.request.CustomerLoginDto;
import org.nmfw.foodietree.domain.customer.dto.request.SignUpDto;
import org.nmfw.foodietree.domain.customer.dto.resp.LoginUserInfoDto;
import org.nmfw.foodietree.domain.customer.entity.Customer;
import org.nmfw.foodietree.domain.customer.mapper.CustomerMapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

import static org.nmfw.foodietree.domain.customer.util.LoginUtil.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

	private final CustomerMapper customerMapper;
	private final PasswordEncoder encoder;

	public boolean join(SignUpDto dto) {
		Customer customer = dto.toEntity();
		String encodedPassword = encoder.encode(dto.getCustomerPassword());
		customer.setCustomerPassword(encodedPassword);
		boolean saved = customerMapper.save(customer);

		if (saved && dto.getFood() != null) {
			customerMapper.savePreferredFoods(dto.getCustomerId(), dto.getFood());
		}

		System.out.println("\n save = " + saved);
		System.out.println(dto.getFood());

		return saved;
	}

	// 로그인 검증 처리
	public LoginResult authenticate(CustomerLoginDto dto, HttpSession session,
									HttpServletResponse response) {

		//회원가입 여부 확인
		String customerId = dto.getCustomerId();
		Customer foundCustomer = customerMapper.findOne(customerId);

		//customer가 null일 경우
		if (foundCustomer == null) {
			log.info("{} - 회원가입이 필요합니다.", customerId);
			return NO_ID;
		}

		//비밀번호 일치 검사
		String inputCustomerPassword = dto.getCustomerPassword();
		String originPassword = foundCustomer.getCustomerPassword();

		//실제 비밀번호와 암호화된 비밀번호 비교
		if (!encoder.matches(inputCustomerPassword, originPassword)) {
			log.info("비밀번호가 일치하지 않습니다.");
			return NO_PW;
		}

		// 자동로그인 추가 처리

		if (dto.isAutoLogin()) {

			String sessionId = session.getId();

			Cookie autoLoginCookie = new Cookie(AUTO_LOGIN_COOKIE, sessionId);
			// 쿠키 설정
			autoLoginCookie.setPath("/"); // 쿠키를 사용할 경로
			autoLoginCookie.setMaxAge(60 * 60 * 24 * 90); // 자동로그인 유지 시간

			// 쿠키 클라이언트에 전송
			response.addCookie(autoLoginCookie);

			// DB에 쿠키값을 저장
			customerMapper.updateAutoLogin(
					AutoLoginDto.builder()
							.sessionId(sessionId)
							.limitTime(LocalDateTime.now().plusDays(90))
							.customerId(customerId)
							.build()
			);
		}

		//일반 로그인 처리
		log.info("{}님 로그인 성공", foundCustomer.getNickName());

		// 세션의 수명 : 설정된 시간 OR 브라우저를 닫기 전까지
		int maxInactiveInterval = session.getMaxInactiveInterval();
		session.setMaxInactiveInterval(60 * 60); // 세션 수명 1시간 설정
		log.debug("session time: {}", maxInactiveInterval);
		session.setAttribute("login", new LoginUserInfoDto(foundCustomer));

		return SUCCESS;
	}


	// 아이디 중복검사
	public boolean checkIdentifier(String keyword) {
		return customerMapper.existsById(keyword);
	}

	public void updateSessionId(String customerId, String sessionId) {
		customerMapper.updateSessionId(customerId, sessionId);
	}

}

