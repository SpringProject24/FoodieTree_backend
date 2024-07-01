package org.nmfw.foodietree.domain.customer.service;

import static org.nmfw.foodietree.domain.customer.service.LoginResult.NO_ID;
import static org.nmfw.foodietree.domain.customer.service.LoginResult.NO_PW;
import static org.nmfw.foodietree.domain.customer.service.LoginResult.SUCCESS;

import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.request.CustomerLoginDto;
import org.nmfw.foodietree.domain.customer.dto.request.SignUpDto;
import org.nmfw.foodietree.domain.customer.entity.Customer;
import org.nmfw.foodietree.domain.customer.mapper.CustomerMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

	//고객 정보를 데이터베이스에 저장하거나 조회하는데 사용되는 객체
	private final CustomerMapper customerMapper;

	//비밀번호 암호화 객체
	private final PasswordEncoder encoder;

	//회원 가입 중간 처리
	public boolean join(SignUpDto dto) {

		// dto를 엔터티로 변환
		Customer customer = dto.toEntity();

		// 비밀번호를 인코딩(암호화)
		String encodedPassword = encoder.encode(dto.getCustomerPassword());
		customer.setCustomerPassword(encodedPassword);

		return customerMapper.save(customer);
	}
//		String id = dto.getCustomerId();
//		String password = dto.getCustomerPassword();

		// 아이디 형식: 영문자(대소문자 구분 없음), 소문자로 구성,
		// 길이는 5~20 사이, 특수문자 불가
//        if(!id.matches("^[a-zA-Z0-9]{5,20}$")) {
//            throw new IllegalArgumentException("아이디 형식이 틀렸습니다.");
//        }
//        // 비밀번호 형식: 길이가 최대 8글자 이면서, 영문자, 숫자 포함
//        if(!password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{1,8}$")) {
//            throw new IllegalArgumentException("비밀번호 형식이 틀렸습니다.");
//        }

//        if(!customerMapper.existsById(dto.getCustomerId())) {
//            throw new IllegalArgumentException("존재하지 않는 아이디입니다.");
//        }

//		// dto를 엔터티로 변환
//		Customer customer = dto.toEntity();
//
//		// 비밀번호 인코딩
//		String encodedPassword = encoder.encode(dto.getCustomerPassword());
//		customer.setCustomerPassword(encodedPassword);
//
//		boolean saveResult = customerMapper.save(customer);
//
//		if (saveResult) {
//			customerMapper.savePreferredFoods(dto.getCustomerId(), dto.getPreferredFoods());
//		}
//
//		return saveResult;
//	}


	//로그인 검증 처리
//	public LoginResult authenticate(CustomerLoginDto dto, HttpSession session)
	public LoginResult authenticate(CustomerLoginDto dto) {

		//회원가입 여부 확인
		String customerId = dto.getCustomerId();
		Customer foundCustomer =
				customerMapper.findOne(customerId);

		//customer가 null일 경우
		if (foundCustomer == null) {
			log.info("{} - 회원가입이 필요합니다.", customerId);
			return NO_ID;
		}

		//비밀번호 일치 검사
		String inputCustomerPassword = dto.getCustomerPassword(); //클라이언트에 입력한 비번
		String originPassword = foundCustomer.getCustomerPassword(); //데이터베이스에 저장된 비번

		//실제 비밀번호와 암호화된 비밀번호 비교
		if (!encoder.matches(inputCustomerPassword, originPassword)) {
			log.info("비밀번호가 일치하지 않습니다.");
			return NO_PW;
		}

		log.info("{}님 로그인 성공", foundCustomer.getNickName());

//		//세션 최대 비활성화 간격
//		int maxInactiveInterval = session.getMaxInactiveInterval();
//
//		//세션 수명 1시간 설정
//		session.setMaxInactiveInterval(60 * 60);
//		log.debug("session time: {}", maxInactiveInterval);
//
//		session.setAttribute("loginUserName", foundCustomer.getNickName());

		return SUCCESS;
	}

	//아이디(이메일) 중복 검사
	// 아이디, 이메일 중복검사
	public boolean checkIdentifier(String keyword) {
		return customerMapper.existsById(keyword);
	}
}

//    public boolean checkIdentifier(String type, String keyword) {
//        return customerMapper.existsById(type, keyword);
//    }


