package org.nmfw.foodietree.domain.customer.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.customer.dto.resp.UpdateAreaDto;
import org.nmfw.foodietree.domain.customer.dto.resp.UpdateDto;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationDetailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class CustomerMyPageServiceTest {
    @Autowired
    CustomerMyPageService customerMyPageService;

    @Test
    @DisplayName("reservation test")
    void reservationTest() {
        //given
        String customerId = "test@gmail.com";
        //when
        List<ReservationDetailDto> reservationInfo = customerMyPageService.getReservationList(customerId);
        //then
        for (ReservationDetailDto myPageReservationDetailDto : reservationInfo) {
            System.out.println("\n\n\n");
            System.out.println("myPageReservationDetailDto = " + myPageReservationDetailDto.getStatus());
            System.out.println("myPageReservationDetailDto = " + myPageReservationDetailDto.getStoreName());
            System.out.println("\n\n\n");
        }
    }

    @Test
    @DisplayName("delete fav area test")
    void deleteFavArea() {
        //given
        String customerId = "test@gmail.com";
        UpdateAreaDto updateAreaDto = UpdateAreaDto.builder()
                .preferredArea("서울특별시 서대문구 신촌로 177")
                        .alias("177")
                        .build();
        //when
        boolean b = customerMyPageService.deleteCustomerAreaInfo(customerId, updateAreaDto);
        //then
        System.out.println("b = " + b);
    }
}