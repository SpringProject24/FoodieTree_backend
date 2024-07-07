package org.nmfw.foodietree.domain.reservation.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationFoundStoreIdDto;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationModalDetailDto;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationStatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReservationMapperTest {
    private static final Logger log = LoggerFactory.getLogger(ReservationMapperTest.class);
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

    @Test
    @DisplayName("예약 생성")
    void createReservation() {
        // given
        String customerId = "thdghtjd115@gmail.com";
        long productId = 83;
        // when
        boolean reservation = reservationMapper.createReservation(customerId, productId);
        // then
        assertTrue(reservation);
    }

    @Test
    @DisplayName("상품 수량 조회")
    void findByStoreIdLimit() {
        // given
        String storeId = "sji4205@naver.com";
        int cnt = 3;
        // when
        List<ReservationFoundStoreIdDto> byStoreIdLimit = reservationMapper.findByStoreIdLimit(storeId, cnt);
        // then
        assertNotNull(byStoreIdLimit);
        for (ReservationFoundStoreIdDto stringStringMap : byStoreIdLimit) {
            log.info("{}", stringStringMap);
        }
    }
}