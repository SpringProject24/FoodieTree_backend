package org.nmfw.foodietree.domain.customer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.dto.EmailCustomerDto;
import org.nmfw.foodietree.domain.customer.dto.request.AutoLoginDto;
import org.nmfw.foodietree.domain.customer.dto.request.CustomerLoginDto;
import org.nmfw.foodietree.domain.customer.dto.request.SignUpDto;
import org.nmfw.foodietree.domain.customer.dto.resp.LoginUserInfoDto;
import org.nmfw.foodietree.domain.customer.entity.Customer;
import org.nmfw.foodietree.domain.customer.mapper.CustomerMapper;
import org.nmfw.foodietree.domain.customer.repository.CustomerRepository;
import org.nmfw.foodietree.domain.customer.util.LoginUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.nmfw.foodietree.domain.customer.util.LoginUtil.AUTO_LOGIN_COOKIE;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

	private final CustomerRepository customerRepository;

	@Transactional(readOnly = true)
	public Optional<LocalDateTime> findRefreshDateById(String email) {
		return customerRepository.findRefreshDateById(email);
	}

	@Transactional
	public void signUpUpdateCustomer(EmailCustomerDto emailCodeCustomerDto) {

		customerRepository.updateRefreshTokenExpireDate(
				emailCodeCustomerDto.getRefreshTokenExpireDate(),
				emailCodeCustomerDto.getCustomerId()
		);
	}

	@Transactional
	public void signUpSaveCustomer(EmailCustomerDto emailCustomerDto) {
		try {
			Customer customer = Customer.builder()
					.customerId(emailCustomerDto.getCustomerId())
					.refreshTokenExpireDate(emailCustomerDto.getRefreshTokenExpireDate())
					.userType(emailCustomerDto.getUserType())
					.build();

			customerRepository.save(customer);
		} catch (Exception e) {
			log.error("Error saving customer: ", e);
			throw e; // 예외를 다시 던져서 롤백 트리거
		}

	}

	@Transactional(readOnly = true)
	public boolean findOne(String email) {
		return customerRepository.existsByCustomerId(email);
	}

	@Transactional(readOnly = true)
	public Customer getCustomerById(String customerId) {
		return customerRepository.findByCustomerId(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
	}

	public void updateCustomer(LocalDateTime date, String email) {
		customerRepository.updateRefreshTokenExpireDate(date, email);
	}
}