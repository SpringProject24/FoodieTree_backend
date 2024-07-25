package org.nmfw.foodietree.domain.customer.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.customer.dto.resp.CustomerFavStoreDto;
import org.nmfw.foodietree.domain.customer.dto.resp.CustomerMyPageDto;
import org.nmfw.foodietree.domain.customer.entity.CustomerIssues;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationDetailDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class CustomerMyPageMapperTest {

    private static final Logger log = LoggerFactory.getLogger(CustomerMyPageMapperTest.class);
    @Autowired
    CustomerMyPageMapper customerMyPageMapper;

    @Test
    @DisplayName("회원 개별 조회")
    void findOneTest() {
        //given
        String customerId = "test@gmail.com";
        //when
        CustomerMyPageDto customer = customerMyPageMapper.findOne(customerId);
        //then
//        assertEquals("김테스트", customer.getNickname());
        System.out.println("customer = " + customer);
    }

    @Test
    @DisplayName("선호 지역을 추출해옴")
    void findPreferenceAreaTest() {
        //given
        String customerId = "test@gmail.com";
        //when
        List<String> preferenceAreas = customerMyPageMapper.findPreferenceAreas(customerId);
        //then
        System.out.println("preferenceAreas = " + preferenceAreas);
    }


//    @Test
//    @DisplayName("선호 음식을 추출해옴")
//    void findPreferenceFoodTest() {
//        //given
//        String customerId = "test@gmail.com";
//        //when
//        List<String> preferenceFoods = customerMyPageMapper.findPreferenceFoods(customerId);
//        //then
//        System.out.println("preferenceAreas = " + preferenceFoods);
//    }

    @Test
    @DisplayName("회원 개인정보 업데이트")
    void updateCustomerInfoTest() {
        //given
        String customerId = "test@gmail.com";
        String type = "nickname";
        String value = "나는야김테스트";
        //when
        customerMyPageMapper.updateCustomerInfo(customerId, type, value);
        CustomerMyPageDto customer = customerMyPageMapper.findOne(customerId);
        //then
        assertEquals(value, customer.getNickname());
    }

    @Test
    @DisplayName("회원의 예약내역을 불러옴")
    void findReservationTest() {
        //given
        String customerId = "test@gmail.com";
        //when
        List<ReservationDetailDto> reservations = customerMyPageMapper.findReservations(customerId);
        //then
//        System.out.println("reservations = " + reservations);

        for (ReservationDetailDto reservation : reservations) {
            System.out.println("reservation = " + reservation.getReservationId());
            System.out.println("reservation = " + reservation.getStoreName());
        }
    }

    @Test
    @DisplayName("회원이 등록한 이슈 내역들을 조회함")
    void findIssuesTest() {
        //given
        String customerId = "test@gmail.com";
        //when
        List<CustomerIssues> issues = customerMyPageMapper.findIssues(customerId);
        //then
        System.out.println("issues = " + issues);
    }
    
    @Test
    @DisplayName("최애 가게 조회")
    void findFavStoreTest() {
        //given
        String customerId = "test@gmail.com";
        //when
        List<CustomerFavStoreDto> favStore = customerMyPageMapper.findFavStore(customerId);
        //then
        System.out.println("favStore = " + favStore);
    }
    
    @Test
    @DisplayName("닉네임 중복 검사")
    void checkNicknameTest() {
        //given
        String nickname = "나는야김테스트";
        //when
        boolean nicknameDuplicate = customerMyPageMapper.isNicknameDuplicate(nickname);
        //then
        System.out.println("nicknameDuplicate = " + nicknameDuplicate);
    }
}