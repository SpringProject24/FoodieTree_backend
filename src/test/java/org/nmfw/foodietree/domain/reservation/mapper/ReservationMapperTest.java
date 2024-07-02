package org.nmfw.foodietree.domain.reservation.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationModalDetailDto;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationStatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservationMapperTest {
    @Autowired
    private ReservationMapper reservationMapper;

    @Test
    @DisplayName("예약 상태 조회")
    void findById() {
        //given
        int reservationId = 3;
        //when
        ReservationStatusDto byReservationId = reservationMapper.findByReservationId(reservationId);

        //then
        System.out.println("byReservationId = " + byReservationId);

    }

    @Test
    @DisplayName("예약 모달 상세 조회")
    void modalDetail() {
        //given
        int reservationId = 3;
        //when
        ReservationModalDetailDto dto = reservationMapper.findModalDetailByReservationId(reservationId);
        //then
        System.out.println("dto = " + dto);
    }
}