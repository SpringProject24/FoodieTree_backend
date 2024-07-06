package org.nmfw.foodietree.domain.store.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.store.dto.resp.StoreReservationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class StoreMyPageServiceTest {

    @Autowired
    private StoreMyPageService storeMyPageService;

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
}