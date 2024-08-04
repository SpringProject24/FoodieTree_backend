//package org.nmfw.foodietree.domain.customer.repository;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.nmfw.foodietree.domain.customer.entity.Customer;
//import org.nmfw.foodietree.domain.customer.mapper.CustomerMapper;
//import org.nmfw.foodietree.domain.customer.mapper.CustomerMyPageMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
////@Transactional
//class CustomerMapperTest {
//
//    @Autowired
//    CustomerMapper customerMapper;
//
//    @Autowired
//    PasswordEncoder encoder;
//
//    @Test
//    @DisplayName("회원가입에 성공해야 한다.")
//    void saveTest() {
//        //given
//        Customer customer = Customer.builder()
//                .customerId("pet123@gmail.com")
//                .customerPassword("abcd1234")
//                .nickName("고양이")
//                .build();
//
//        //when
//        boolean flag = customerMapper.save(customer);
//
//        //then
//        assertTrue(flag);
//    }
//
//
//    @Test
//    @DisplayName("아이디가 pineapple@naver.com인 회원의 중복확인 결과가 ture이다.")
//    void findOneTest() {
//        //given
//        String id = "pineapple@naver.com";
//        //when
//        Customer foundCustomer = customerMapper.findOne(id);
//        //then
//        assertEquals("파인애플", foundCustomer.getNickName());
//    }
//
//
//    @Test
//    @DisplayName("계정명이 coffee@gmail.com 회원은 중복확인 결과가 true이다.")
//    void existsTest() {
//        //given
//        String customerId = "coffee@gmail.com";
//        //when
//        boolean flag = customerMapper.existsById(customerId);
//        //then
//        assertTrue(flag);
//    }
//
//
//
//    @Test
//    @DisplayName("평문의 암호를 인코딩해야 한다.")
//    void encodingTest() {
//        //given
//        String rawPassword = "happy999";
//
//        //when
//        String encodePassword = encoder.encode(rawPassword);
//
//        //then
//        System.out.println("encodedPassword = " + encodePassword);
//    }
//
//    @Test
//    @DisplayName("선호하는 음식 2개를 저장한다.")
//    void savePreferredFoodsTest() {
//        //given
//        String customerId = "pet123@gmail.com";
//        List<String> preferredFoods = Arrays.asList("한식", "디저트");
//
//        //when
//        boolean flag = customerMapper.savePreferredFoods(customerId, preferredFoods);
//
//        //then
//        assertTrue(flag);
//
//    }
//}