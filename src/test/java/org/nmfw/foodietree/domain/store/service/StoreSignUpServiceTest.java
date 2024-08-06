//package org.nmfw.foodietree.domain.store.service;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.nmfw.foodietree.domain.store.entity.Store;
//import org.nmfw.foodietree.domain.store.mapper.StoreMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
////@Transactional
//class StoreSignUpServiceTest {
//    @Autowired
//    StoreMapper storeMapper;
//
//    @Autowired
//    PasswordEncoder encoder;
//
//    @Test
//    @DisplayName("가게 회원가입에 성공해야함")
//    void storeSaveTest() {
//        //given
//        Store store = Store.builder()
//                .storeId("bbb@gmail.com")
//                .password("hhhh")
//                .build();
//        //when
//        boolean flag = storeMapper.storeSave(store);
//        //then
//        assertTrue(flag);
//    }
//}