package org.nmfw.foodietree.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.dto.EmailCustomerDto;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeDto;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeStoreDto;
import org.nmfw.foodietree.domain.auth.security.TokenProvider;
import org.nmfw.foodietree.domain.customer.entity.Customer;
import org.nmfw.foodietree.domain.customer.service.CustomerService;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.nmfw.foodietree.domain.store.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final CustomerService customerService;
    private final StoreService storeService;

    private final TokenProvider tokenProvider;


    public ResponseEntity<Map<String, ? extends Serializable>> saveUserInfo(EmailCodeDto emailCodeDto) {

        String emailCodeDtoUserType = emailCodeDto.getUserType();
        log.info("emailcodedto에서 유저타입 찾기 {}", emailCodeDtoUserType);
        String emailCodeDtoEmail = emailCodeDto.getEmail();
        log.info("emailcodedto에서 이메일 찾기 {}", emailCodeDtoEmail);

        String token = tokenProvider.createToken(emailCodeDto);
        String refreshToken = tokenProvider.createRefreshToken(emailCodeDtoEmail, emailCodeDtoUserType);
        LocalDateTime expirationDate = tokenProvider.getExpirationDateFromRefreshToken(refreshToken);

        // 최초 회원 정보 저장 로직 :  customer인지 store 인지 null 값으로 구분
        if (emailCodeDtoUserType.equals("store")) {

            EmailCodeStoreDto emailCodeStoreDto = EmailCodeStoreDto.builder()
                    .storeId(emailCodeDtoEmail)
                    .userType(emailCodeDtoUserType)
                    .refreshTokenExpireDate(expirationDate)
                    .build();

            storeService.signUpSaveStore(emailCodeStoreDto);

        } else if(emailCodeDtoUserType.equals("customer")) {

            EmailCustomerDto emailCodeCustomerDto = EmailCustomerDto.builder()
                    .customerId(emailCodeDtoEmail)
                    .userType(emailCodeDtoUserType)
                    .refreshTokenExpireDate(expirationDate)
                    .build();

            customerService.signUpSaveCustomer(emailCodeCustomerDto);
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "token", token,
                "refreshToken", refreshToken,
                "email", emailCodeDtoEmail,
                "role", emailCodeDtoUserType,
                "message", "Token reissued successfully."
        ));

    }

    // access token, refresh token 재발급
    public ResponseEntity<Map<String, ? extends Serializable>> updateUserInfo(EmailCodeDto emailCodeDto) {

        String emailCodeDtoUserType = emailCodeDto.getUserType();
        String emailCodeDtoEmail = emailCodeDto.getEmail();

        String token = tokenProvider.createToken(emailCodeDto);
        log.info(" 새로운 토큰 재발급 ! {}",token);
        String refreshToken = tokenProvider.createRefreshToken(emailCodeDtoEmail, emailCodeDtoUserType);
        log.info("새로운 리프레시 토큰 재발급 ! {} ",refreshToken);

        LocalDateTime expirationDate = tokenProvider.getExpirationDateFromRefreshToken(refreshToken);

        if (emailCodeDtoUserType.equals("store")) {

            EmailCodeStoreDto emailCodeStoreDto = EmailCodeStoreDto.builder()
                    .storeId(emailCodeDtoEmail)
                    .userType(emailCodeDtoUserType)
                    .refreshTokenExpireDate(expirationDate)
                    .build();

            storeService.signUpUpdateStore(emailCodeStoreDto);

            // customer 일 경우
        } else if (emailCodeDtoUserType.equals("customer")) {

            EmailCustomerDto emailCodeCustomerDto = EmailCustomerDto.builder()
                    .customerId(emailCodeDtoEmail)
                    .userType(emailCodeDtoUserType)
                    .refreshTokenExpireDate(expirationDate)
                    .build();

            customerService.signUpUpdateCustomer(emailCodeCustomerDto);

        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "token", token,
                "refreshToken", refreshToken,
                "email", emailCodeDtoEmail,  // "email" 키에 email을 할당
                "role", emailCodeDtoUserType,  // "role" 키에 userType을 할당
                "message", "Token reissued successfully."
        ));
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
            if (storeService.findOne(emailCodeDto.getEmail())) {

                log.info("로그인 로직 확인 : 들어오는 유저타입 : {}, TRUE", emailCodeDto.getUserType());
                result = true;
            }
        } else if ("customer".equals(emailCodeDto.getUserType())) {
            log.info("customer 타입 확인");
            log.info("로그인 로직 확인 : 들어오는 유저타입 : {}", emailCodeDto.getUserType());
            if (customerService.findOne(emailCodeDto.getEmail())) {

                log.info("로그인 로직 확인 : 들어오는 유저타입 : {}, TRUE", emailCodeDto.getUserType());
                result = true;
            }
        }

        return result;
    }

    public LocalDateTime getUserRefreshTokenExpiryDate(String email, String userType) {
        if ("customer".equals(userType)) {
            Customer customer = customerService.getCustomerById(email);
            log.info("customer object : {},customer email : {}, customer 의 리프레시 만료일자 인 서버 : {}", customer,customer.getCustomerId(), customer.getRefreshTokenExpireDate());
            return customer != null ? customer.getRefreshTokenExpireDate() : null;
        } else if ("store".equals(userType)) {
            Store store = storeService.getStoreById(email);

            log.info("store email : {}, store 의 리프레시 만료일자 인 서버 : {}", email, store.getRefreshTokenExpireDate());
            return store != null ? store.getRefreshTokenExpireDate() : null;
        } else {
            throw new IllegalArgumentException("Invalid user type");
        }
    }

    public void setUserRefreshTokenExpiryDate(String email, String userType) {
        String newRefreshToken = tokenProvider.createRefreshToken(email, userType);
        LocalDateTime newExpiryDate = tokenProvider.getExpirationDateFromRefreshToken(newRefreshToken);

        if ("customer".equals(userType)) {
            Customer customer = customerService.getCustomerById(email);
            if (customer != null) {
                customerService.updateCustomer(newExpiryDate, email);
            }
        } else if ("store".equals(userType)) {
            Store store = storeService.getStoreById(email);
            if (store != null) {
                storeService.updateStore(newExpiryDate, email);
            }
        } else {
            throw new IllegalArgumentException("Invalid user type");
        }
    }
}
