package org.nmfw.foodietree.domain.reservation.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationDetailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;
    @Test
    @DisplayName("dtoTest")
    void dtoTest() {
        //given

        //when
        ReservationDetailDto dto = reservationService.getReservationDetail(3);
        //then
        System.out.println("reservationDetail = " + dto.getStatus());
        System.out.println("reservationDetail = " + dto.getPickedUpAt());
        System.out.println("reservationDetail.getNickname() = " + dto.getNickname());
    }

    @Test
    @DisplayName("상품 수량 만큼 예약 생성")
    void createReservation() {
        // given
        Map<String, String> data = Map.of(
                "storeId", "sji4205@naver.com",
                "cnt", "2"
        );
        String customerId = "thdghtjd115@gmail.com";
        int cnt = 2;
        // when
        boolean reservation = reservationService.createReservation(customerId, data);
        // then
        assertTrue(reservation);

    }
}