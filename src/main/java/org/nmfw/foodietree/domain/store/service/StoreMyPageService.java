package org.nmfw.foodietree.domain.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.entity.ReservationDetail;
import org.nmfw.foodietree.domain.customer.entity.value.PickUpStatus;
import org.nmfw.foodietree.domain.customer.service.CustomerMyPageService;
import org.nmfw.foodietree.domain.store.dto.resp.*;
import org.nmfw.foodietree.domain.store.mapper.StoreMyPageMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StoreMyPageService {

    private final StoreMyPageMapper storeMyPageMapper;
    private final CustomerMyPageService customerMyPageService;

    public StoreMyPageDto getStoreMyPageInfo(String storeId) {
        log.info("store my page service");
        return storeMyPageMapper.getStoreMyPageInfo(storeId);
    }

    public List<StoreReservationDto> findReservations(String storeId) {
        log.info("service get store reservations");
        List<StoreReservationDto> reservations = storeMyPageMapper.findReservations(storeId);

        for (StoreReservationDto dto : reservations) {
            ReservationDetail rdto = ReservationDetail.builder()
                    .pickedUpAt(dto.getPickedUpAt())
                    .pickupTime(dto.getPickupTime())
                    .cancelReservationAt(dto.getCancelReservationAt())
                    .reservationTime(dto.getReservationTime())
                    .build();
            PickUpStatus status = customerMyPageService.determinePickUpStatus(rdto);
            dto.setStatus(status);
        }
        return reservations;
    }

    public StoreMyPageCalendarModalDto getStoreMyPageCalendarModalInfo(String storeId, String date) {
        log.info("service get store my page calendar modal info");
        return storeMyPageMapper.getStoreMyPageCalendarModalInfo(storeId, date).get(0);
    }


    public StoreStatsDto getStats(String storeId){
        List<StoreReservationDto> reservations = storeMyPageMapper.findReservations(storeId);

        // 예약 내역 중 pickedUpAt이 null이 아닌 것들의 리스트
        List<StoreReservationDto> pickedUpReservations = reservations.stream()
                .filter(reservation -> reservation.getPickedUpAt() != null)
                .collect(Collectors.toList());

        // pickedUpAt이 null이 아닌 것들의 개수
        int total = pickedUpReservations.size();

        // CO2 계산
        double coTwo = total * 0.12;

        // totalCustomerCnt 계산
        int customerCnt = (int) pickedUpReservations.stream()
                .map(StoreReservationDto::getCustomerId)
                .distinct()
                .count();

        return StoreStatsDto.builder()
                .total(total)
                .coTwo(coTwo)
                .customerCnt(customerCnt)
                .build();
    }

    public boolean setHoliday(String storeId, String holidayDate) {
        log.info("service set holiday");
        storeMyPageMapper.setHoliday(storeId, holidayDate);
        List<StoreHolidayDto> holidays = storeMyPageMapper.getHolidays(storeId);
        for (StoreHolidayDto holiday : holidays) {
            log.info("holiday = " + holiday.getHolidays());
            if (holiday.getHolidays().equals(holidayDate)) {
                return true;
            }
        }
        return false;
    }

    public List<StoreHolidayDto> getHolidays(String storeId) {
        log.info("service get holidays");
        return storeMyPageMapper.getHolidays(storeId);
    }
}
