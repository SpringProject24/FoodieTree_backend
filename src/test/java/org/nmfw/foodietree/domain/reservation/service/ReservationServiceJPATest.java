package org.nmfw.foodietree.domain.reservation.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationDetailDto;
import org.nmfw.foodietree.domain.reservation.repository.ReservationRepository;
import org.nmfw.foodietree.domain.store.dto.resp.StoreReservationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.net.http.HttpTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class ReservationServiceJPATest {

    @Autowired
    ReservationService reservationService;
    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("예약생성")
    public void create() {
        // given
        Map<String, String> data = Map.of(
                "storeId", "sji4205@naver.com",
                "cnt", "2"
        );
        String customerId = "thdghtjd115@gmail.com";
        int cnt = 2;
        boolean reservation = reservationService.createReservation(customerId, data);

        assertTrue(reservation);
    }

//    @Test
//    public void cancel() {
//        Long reservationId = 43L;
//        boolean b = reservationService.cancelReservation(reservationId);
//        assertTrue(b);
//    }
    @Test
    public void pickup() {
        Long reservationId = 42L;
        boolean b = reservationService.completePickup(reservationId);
        assertTrue(b);
    }

    @Test
    public void getDetail() {
        long reservationId = 43L;
        ReservationDetailDto reservationDetail = reservationService.getReservationDetail(reservationId);
        System.out.println(reservationDetail.toString());

        assertNotNull(reservationDetail);
    }

    @Test
    public void findReservation() {
        long reservationId = 43L;
        ReservationDetailDto reservation = reservationRepository.findReservationByReservationId(reservationId);
        System.out.println(reservation.toString());
        assertNotNull(reservation);
    }

    @Test
    public void findReservations() {
        String storeId = "sji4205@naver.com";
        List<StoreReservationDto> reservations = reservationRepository.findReservations(storeId);
        System.out.println("=================");
        reservations.forEach(System.out::println);
        System.out.println("=================");
        assertNotNull(reservations);
    }
}