package org.nmfw.foodietree.domain.reservation.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationDetailDto;
import org.nmfw.foodietree.domain.reservation.entity.value.PaymentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class ReservationServiceTest {

    private static final Logger log = LoggerFactory.getLogger(ReservationServiceTest.class);
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

    @Test
    void testPaymentRequest() {
        // given
        Map<String, String> data = Map.of(
                "paymentId", "payment-f295ccab-df54-40fe-8356-1e65424b73cf"
        );
        // when
        PaymentStatus paymentStatus;
        try {
            paymentStatus = reservationService.processPaymentUpdate(data);
        } catch (InterruptedException e) {
            log.error(".await() error -> {}", e);
            throw new RuntimeException(e);
        }
        // then
        Assertions.assertAll(
                () -> assertEquals(PaymentStatus.PAID, paymentStatus)
        );

    }
}