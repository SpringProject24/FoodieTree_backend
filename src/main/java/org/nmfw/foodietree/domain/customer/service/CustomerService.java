package org.nmfw.foodietree.domain.customer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.request.AutoLoginDto;
import org.nmfw.foodietree.domain.customer.dto.request.CustomerLoginDto;
import org.nmfw.foodietree.domain.customer.dto.request.SignUpDto;
import org.nmfw.foodietree.domain.customer.dto.resp.LoginUserInfoDto;
import org.nmfw.foodietree.domain.customer.entity.Customer;
import org.nmfw.foodietree.domain.customer.mapper.CustomerMapper;
import org.nmfw.foodietree.domain.customer.util.LoginUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

import static org.nmfw.foodietree.domain.customer.util.LoginUtil.AUTO_LOGIN_COOKIE;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

	//고객 정보를 데이터베이스에 저장하거나 조회하는데 사용되는 객체
	private final CustomerMapper customerMapper;
	//비밀번호 암호화 객체
	private final PasswordEncoder encoder;

	//회원 가입 중간 처리 (저장 성공 여부 boolean 값으로 반환)
	public boolean join(SignUpDto dto) {
		Customer customer = dto.toEntity();

		// 비밀번호를 인코딩(암호화)
//		String encodedPassword = encoder.encode(dto.getCustomerPassword());
//		customer.setCustomerPassword(encodedPassword); //인코딩 된 비밀번호를 Customer에 주입

		boolean saved = customerMapper.save(customer); //데이터에 저장

//		if (saved && dto.getFood() != null) {
//			customerMapper.savePreferredFoods(dto.getCustomerId(), dto.getFood());
//		}

//		System.out.println("\n save = " + saved);
//		System.out.println(dto.getFood());

		return saved; // 데이터 저장 결과 반환
	}


	//로그인 검증 처리
	public LoginResult authenticate(CustomerLoginDto dto,
									HttpSession session,
									HttpServletResponse response) {

		// 회원가입 여부 확인
		String customerId = dto.getCustomerId();
		System.out.println("\ncustomerId = " + customerId);

		Customer foundCustomer = customerMapper.findOne(customerId); //db에 있는 customerId 꺼내옴.
		System.out.println("\nfoundCustomer = " + foundCustomer);

		if (foundCustomer == null) {
			log.info("{} - 회원가입이 필요합니다.", customerId);
			return LoginResult.NO_ID;
		}

		// 비밀번호 일치 검사
		String inputPassword = dto.getCustomerPassword();
		String originPassword = foundCustomer.getCustomerPassword();

		if (!encoder.matches(inputPassword, originPassword)) {
			log.info("비밀번호가 일치하지 않습니다.");
			return LoginResult.NO_PW;
		}

		// 자동로그인 추가 처리
		if (dto.isAutoLogin()) {
			// 1. 자동 로그인 쿠키 생성
			String sessionId = session.getId();

			System.out.println("\nsessionId = " + sessionId);
			Cookie autoLoginCookie = new Cookie(AUTO_LOGIN_COOKIE, sessionId);
			System.out.println("\nautoLoginCookie = " + autoLoginCookie);
			// 쿠키 설정
			autoLoginCookie.setPath("/"); // 쿠키를 사용할 경로
			autoLoginCookie.setMaxAge(60 * 60 * 24 * 90); // 자동로그인 유지 시간
			// 2. 쿠키를 클라이언트에 전송 - 응답바디에 실어보내야 함
			response.addCookie(autoLoginCookie);
			log.debug("\nAuto login cookie set: " + autoLoginCookie.toString());

			// 3. DB에도 해당 쿠키값을 저장
			customerMapper.updateAutoLogin(
					AutoLoginDto.builder()
							.sessionId(sessionId)
							.limitTime(LocalDateTime.now().plusDays(90))
							.id(customerId)
							.build()
			);
		}


		maintainLoginState(session, foundCustomer);

		return LoginResult.SUCCESS;
	}

	public static void maintainLoginState(HttpSession session, Customer foundCustomer) {
		log.info("{}님 로그인 성공", foundCustomer.getCustomerId());

		// 세션의 수명 : 설정된 시간 or 브라우저를 닫기 전까지
		int maxInactiveInterval = session.getMaxInactiveInterval();
		session.setMaxInactiveInterval(60 * 60);
		log.debug("session time: {}", maxInactiveInterval);

		session.setAttribute("login", new LoginUserInfoDto(foundCustomer));
	}

	// 아이디 중복 검사
	public boolean checkIdentifier(String keyword) {
		return customerMapper.existsById(keyword);
	}

	public void autoLoginClear(HttpServletRequest request,
							   HttpServletResponse response) {

		Cookie c = WebUtils.getCookie(request, AUTO_LOGIN_COOKIE);
		if(c != null) {
			c.setPath("/");
			c.setMaxAge(0);
			response.addCookie(c);
			customerMapper.updateAutoLogin(
					AutoLoginDto.builder()
							.sessionId("none")
							.limitTime(LocalDateTime.now())
							.id(LoginUtil.getLoggedInUser(request.getSession()))
							.build()
			);
		}





	}
}