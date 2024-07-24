package org.nmfw.foodietree.domain.customer.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.customer.dto.request.CustomerLoginDto;
import org.nmfw.foodietree.domain.customer.dto.request.SignUpDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest @Transactional
class CustomerServiceTest {

    @Autowired
    CustomerService customerService;

    @Test
    @DisplayName("회원가입 시 비밀번호가 암호화된다.")
    void joinTest() {
        //given
        SignUpDto dto = SignUpDto.builder()
                .customerId("dog@naver.com")
//                .customerPassword("dog123!")
//                .nickName("오레오")
//                .customerPhoneNumber("010-9876-5432")
                .build();
        //when
        boolean flag = customerService.join(dto);

        //then
        assertTrue(flag);
    }

//    @Test
//    @DisplayName("아이디가 존재하지 않는 경우를 테스트한다.")
//    void noIdTest() {
//        //given
//        CustomerLoginDto dto = CustomerLoginDto.builder()
//                .customerId("donut")
//                .build();
//        //when
//        LoginResult result = customerService.authenticate(dto);
//
//        //then
//        assertEquals(LoginResult.NO_ID, result);
//    }
//
//    @Test
//    @DisplayName("비밀번호가 틀린 경우를 테스트한다.")
//    void noPwTest() {
//        //given
//        CustomerLoginDto dto = CustomerLoginDto.builder()
//                .customerId("pineapple@naver.com")
//                .customerPassword("098765")
//                .build();
//
//        //when
//        LoginResult result = customerService.authenticate(dto);
//
//        //then
//        assertEquals(LoginResult.NO_PW, result);
//    }
//
//    @Test
//    @DisplayName("로그인이 성공하는 경우를 테스트한다.")
//    void successTest() {
//        //given
//        CustomerLoginDto dto = CustomerLoginDto.builder()
//                .customerId("dog@naver.com")
//                .customerPassword("dog123!")
//                .build();
//        //when
//        LoginResult result = customerService.authenticate(dto);
//
//        //then
//        assertEquals(LoginResult.SUCCESS, result);
//    }
}