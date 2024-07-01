package org.nmfw.foodietree.domain.customer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.request.CustomerLoginDto;
import org.nmfw.foodietree.domain.customer.dto.request.SignUpDto;
import org.nmfw.foodietree.domain.customer.dto.resp.LoginUserInfoDto;
import org.nmfw.foodietree.domain.customer.entity.Customer;
import org.nmfw.foodietree.domain.customer.mapper.CustomerMapper;
import org.nmfw.foodietree.domain.store.service.StoreLoginResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

import static org.nmfw.foodietree.domain.store.service.StoreLoginResult.*;

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
		String encodedPassword = encoder.encode(dto.getCustomerPassword());
		customer.setCustomerPassword(encodedPassword); //인코딩 된 비밀번호를 Customer에 주입

		boolean saved = customerMapper.save(customer); //데이터에 저장

		if (saved && dto.getFood() != null) {
			customerMapper.savePreferredFoods(dto.getCustomerId(), dto.getFood());
		}

		System.out.println("\n save = " + saved);
		System.out.println(dto.getFood());

		return saved; // 데이터 저장 결과 반환
	}

	/* 기존코드

	//회원 가입 중간 처리 (저장 성공 여부 boolean 값으로 반환)
	public boolean join(SignUpDto dto) {

		Customer customer = dto.toEntity();

		// 비밀번호를 인코딩(암호화)
		String encodedPassword = encoder.encode(dto.getCustomerPassword());
		customer.setCustomerPassword(encodedPassword); //인코딩 된 비밀번호를 Customer에 주입

		return customerMapper.save(customer); //데이터에 저장
	}

	 */


	//로그인 검증 처리
	public LoginResult authenticate(CustomerLoginDto dto, HttpSession session) {

		// 회원가입 여부 확인
		String customerId = dto.getCustomerId();
		Customer foundCustomer = customerMapper.findOne(customerId); //db에 있는 customerId 꺼내옴.

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


		log.info("{}님 로그인 성공", foundCustomer.getNickName());


		//세션 최대 비활성화 간격
		int maxInactiveInterval = session.getMaxInactiveInterval();

		//세션 수명 1시간 설정
		session.setMaxInactiveInterval(1800);
		log.debug("session time: {}", maxInactiveInterval);

		session.setAttribute("login", new LoginUserInfoDto(foundCustomer) );

		return LoginResult.SUCCESS;
	}

	// 아이디 중복 검사
	public boolean checkIdentifier(String keyword) {
		return customerMapper.existsById(keyword);
	}

}


