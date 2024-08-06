package org.nmfw.foodietree.domain.store.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.product.repository.ProductRepository;
import org.nmfw.foodietree.domain.store.dto.resp.StoreHolidayDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreMyPageCalendarModalDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreMyPageDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreReservationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class StoreMyPageServiceTest {

    @Autowired
    private StoreMyPageService storeMyPageService;
    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("")
    void reservaitonTest() {
        //given
        String storeId = "aaa@aaa.com";
        //when
        List<StoreReservationDto> reservations = storeMyPageService.findReservations(storeId);
        //then
        for (StoreReservationDto reservation : reservations) {
            System.out.println("\n\n\n");
            System.out.println("reservation = " + reservation.getStatus());
            System.out.println("reservation.getPickupTime() = " + reservation.getPickedUpAt());
            System.out.println("reservation.getReservationTime() = " + reservation.getCancelReservationAt());
        }
    }

    @Test
    @DisplayName("jpa로 전환 후 가게 정보 가져오기 테스트")
    void getStoreInfoTest() {
        //given
        String storeId = "aaa@aaa.com";
        //when
        StoreMyPageDto storeMyPageInfo = storeMyPageService.getStoreMyPageInfo(storeId);

        //then
        System.out.println("storeMyPageInfo = " + storeMyPageInfo);
    }

    @Test
    @DisplayName("jpa 전환 후 달력 상세 내역 가져오기 테스트")
    void getCalendarData() {
        //given
        String storeId = "thdghtjd115@naver.com";
        String date = "2024-08-01";
        //when
        StoreMyPageCalendarModalDto dto = storeMyPageService.getStoreMyPageCalendarModalInfo(storeId, date);

        //then
        System.out.println("dto = " + dto);
    }

    @Test
    @DisplayName("휴무일 저장 테스트")
    void saveHolidays() {
        //given
        String storeId = "thdghtjd115@naver.com";
        String date = "2024-08-27";
        //when
        boolean b = storeMyPageService.setHoliday(storeId, date);
        //then
        System.out.println("b = " + b);
    }

    @Test
    @DisplayName("휴무일 삭제 테스트")
    void deleteHolidays() {
        //given
        String storeId = "thdghtjd115@naver.com";
        String date = "2024-08-23";
        //when
        boolean b = storeMyPageService.undoHoliday(storeId, date);
        //then
        System.out.println("b = " + b);
    }

    @Test
    @DisplayName("상품 자동 업데이트 확인")
    void productAutoUpdateTest() {
        //given
        String storeId = "aaa@aaa.com";

        //when
//        productRepository.save(storeId);
        //then
    }

}