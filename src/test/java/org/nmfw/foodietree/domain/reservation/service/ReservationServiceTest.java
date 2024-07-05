package org.nmfw.foodietree.domain.reservation.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationModalDetailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;
    @Test
    @DisplayName("dtoTest")
    void dtoTest() {
        //given

        //when
        ReservationModalDetailDto reservationDetail = reservationService.getReservationDetail(3);
        //then
        System.out.println("reservationDetail = " + reservationDetail.getStatus());
        System.out.println("reservationDetail = " + reservationDetail.getPickedUpAt());
        System.out.println("reservationDetail.getNickname() = " + reservationDetail.getNickname());
    }
}