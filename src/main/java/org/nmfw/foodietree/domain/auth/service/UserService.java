package org.nmfw.foodietree.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.dto.EmailCustomerDto;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeDto;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeStoreDto;
import org.nmfw.foodietree.domain.auth.security.TokenProvider;
import org.nmfw.foodietree.domain.customer.entity.Customer;
import org.nmfw.foodietree.domain.customer.mapper.CustomerMapper;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.nmfw.foodietree.domain.store.mapper.StoreMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final CustomerMapper customerMapper;
    private final StoreMapper storeMapper;

    private final TokenProvider tokenProvider;


    public void saveUserInfo(EmailCodeDto emailCodeDto) {

        String emailCodeDtoUserType = emailCodeDto.getUserType();
        String emailCodeDtoEmail = emailCodeDto.getEmail();

        String refreshToken = tokenProvider.createRefreshToken(emailCodeDtoEmail, emailCodeDtoUserType);
        Date expirationDate = tokenProvider.getExpirationDateFromRefreshToken(refreshToken);

        // 최초 회원 정보 저장 로직 :  customer인지 store 인지 null 값으로 구분
        if (emailCodeDtoUserType.equals("store")) {

            EmailCodeStoreDto emailCodeStoreDto = EmailCodeStoreDto.builder()
                    .storeId(emailCodeDtoEmail)
                    .userType(emailCodeDtoUserType)
                    .refreshTokenExpireDate(expirationDate)
                    .build();

            storeMapper.signUpSaveStore(emailCodeStoreDto);

        } else if(emailCodeDtoUserType.equals("customer")) {

            EmailCustomerDto emailCodeCustomerDto = EmailCustomerDto.builder()
                    .customerId(emailCodeDtoEmail)
                    .userType(emailCodeDtoUserType)
                    .refreshTokenExpireDate(expirationDate)
                    .build();

            customerMapper.signUpSaveCustomer(emailCodeCustomerDto);
        }

    }

    // access token, refresh token 재발급
    public void updateUserInfo(EmailCodeDto emailCodeDto) {

        String emailCodeDtoUserType = emailCodeDto.getUserType();
        String emailCodeDtoEmail = emailCodeDto.getEmail();

        String token = tokenProvider.createRefreshToken(emailCodeDtoEmail, emailCodeDtoUserType);
        Date expirationDate = tokenProvider.getExpirationDateFromRefreshToken(token);

        if (emailCodeDtoUserType.equals("store")) {

            EmailCodeStoreDto emailCodeStoreDto = EmailCodeStoreDto.builder()
                    .storeId(emailCodeDtoEmail)
                    .userType(emailCodeDtoUserType)
                    .refreshTokenExpireDate(expirationDate)
                    .build();
            storeMapper.signUpUpdateStore(emailCodeStoreDto);

            // customer 일 경우
        } else if (emailCodeDtoUserType.equals("customer")) {

            EmailCustomerDto emailCodeCustomerDto = EmailCustomerDto.builder()
                    .customerId(emailCodeDtoEmail)
                    .userType(emailCodeDtoUserType)
                    .refreshTokenExpireDate(expirationDate)
                    .build();

            customerMapper.signUpUpdateCustomer(emailCodeCustomerDto);
        }
    }

    // customer, store DB 에 회원이 존재하는지 여부 확인
    // 들어오는 dto role 에 따라서 테이블 조회 후 null 일 경우 false 반환
    // 이미 회원가입(저장)인 경우 true 를 바환
    //usage 에서 false일 경우 DB 저장
    // true 일 경우 update
    public boolean findByEmail(EmailCodeDto emailCodeDto) {

        log.info("로그인 로직 내 이메일이 회원가입 되어있는지 확인 !!! ");
        boolean result = false; // 초기값을 false로 설정

        // 문자열 비교시 equals 사용
        if ("store".equals(emailCodeDto.getUserType())) {
            log.info("store 타입 확인");
            log.info("로그인 로직 확인 : 들어오는 유저타입 : {}", emailCodeDto.getUserType());
            if (storeMapper.findOne(emailCodeDto.getEmail()) != null) {
                log.info("로그인 로직 확인 : 들어오는 유저타입 : {}, TRUE", emailCodeDto.getUserType());
                result = true;
            }
        } else if ("customer".equals(emailCodeDto.getUserType())) {
            log.info("customer 타입 확인");
            log.info("로그인 로직 확인 : 들어오는 유저타입 : {}", emailCodeDto.getUserType());
            if (customerMapper.findOne(emailCodeDto.getEmail()) != null) {
                log.info("로그인 로직 확인 : 들어오는 유저타입 : {}, TRUE", emailCodeDto.getUserType());
                result = true;
            }
        }

        return result;
    }

    public LocalDateTime getRefreshTokenExpiryDate(String email, String userType) {
        if ("customer".equals(userType)) {
            Customer customer = customerMapper.findOne(email);
            log.info("customer object : {},customer email : {}, customer 의 리프레시 만료일자 인 서버 : {}", customer,customer.getCustomerId(), customer.getRefreshTokenExpireDate());
            return customer != null ? customer.getRefreshTokenExpireDate() : null;
        } else if ("store".equals(userType)) {
            Store store = storeMapper.findOne(email);
            log.info("store email : {}, store 의 리프레시 만료일자 인 서버 : {}", email, store.getRefreshTokenExpireDate());
            return store != null ? store.getRefreshTokenExpireDate() : null;
        } else {
            throw new IllegalArgumentException("Invalid user type");
        }
    }
}

