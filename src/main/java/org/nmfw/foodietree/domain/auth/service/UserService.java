package org.nmfw.foodietree.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeCustomerDto;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeDto;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeStoreDto;
import org.nmfw.foodietree.domain.auth.security.TokenProvider;
import org.nmfw.foodietree.domain.customer.mapper.CustomerMapper;
import org.nmfw.foodietree.domain.store.mapper.StoreMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final CustomerMapper customerMapper;
    private final StoreMapper storeMapper;

    private final TokenProvider tokenProvider;



    public void saveUserInfo(EmailCodeDto emailCodeDto) {

        // 최초 회원 정보 저장 로직 :  customer인지 store 인지 null 값으로 구분
        if (emailCodeDto.getCustomerId() == null) {
            String token = tokenProvider.createRefreshToken(emailCodeDto.getStoreId());
            Date expirationDate = tokenProvider.getExpirationDateFromToken(token);

            EmailCodeStoreDto emailCodeStoreDto = EmailCodeStoreDto.builder()
                    .storeId(emailCodeDto.getStoreId())
                    .userType(emailCodeDto.getUserType())
                    .refreshTokenExpireDate(expirationDate)
                    .build();
            storeMapper.signUpSaveStore(emailCodeStoreDto);

        } else {
            String token = tokenProvider.createRefreshToken(emailCodeDto.getCustomerId());
            Date expirationDate = tokenProvider.getExpirationDateFromToken(token);

            EmailCodeCustomerDto emailCodeCustomerDto = EmailCodeCustomerDto.builder()
                    .customerId(emailCodeDto.getCustomerId())
                    .refreshTokenExpireDate(expirationDate)
                    .build();
            customerMapper.signUpSaveCustomer(emailCodeCustomerDto);
        }
    }

    // access token, refresh token resend
    public void updateUserInfo(EmailCodeDto emailCodeDto) {

        //store 일 경우
        if (emailCodeDto.getCustomerId() == null) {
            String token = tokenProvider.createRefreshToken(emailCodeDto.getStoreId());
            Date expirationDate = tokenProvider.getExpirationDateFromToken(token);

            EmailCodeStoreDto emailCodeStoreDto = EmailCodeStoreDto.builder()
                    .storeId(emailCodeDto.getStoreId())
                    .userType(emailCodeDto.getUserType())
                    .refreshTokenExpireDate(expirationDate)
                    .build();
            storeMapper.signUpUpdateStore(emailCodeStoreDto);

        } else {

            //custmer 일 경우
            String token = tokenProvider.createRefreshToken(emailCodeDto.getCustomerId());
            Date expirationDate = tokenProvider.getExpirationDateFromToken(token);

            EmailCodeCustomerDto emailCodeCustomerDto = EmailCodeCustomerDto.builder()
                    .customerId(emailCodeDto.getCustomerId())
                    .refreshTokenExpireDate(expirationDate)
                    .build();
            customerMapper.signUpUpdateCustomer(emailCodeCustomerDto);
        }
    }
}
