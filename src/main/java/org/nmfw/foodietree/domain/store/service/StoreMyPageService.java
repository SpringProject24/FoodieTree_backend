package org.nmfw.foodietree.domain.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.product.dto.response.ProductInfoDto;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationDetailDto;
import org.nmfw.foodietree.domain.reservation.entity.ReservationStatus;
import org.nmfw.foodietree.domain.reservation.mapper.ReservationMapper;
import org.nmfw.foodietree.domain.reservation.service.ReservationService;
import org.nmfw.foodietree.domain.store.dto.resp.*;
import org.nmfw.foodietree.domain.store.mapper.StoreMyPageMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StoreMyPageService {

    private final StoreMyPageMapper storeMyPageMapper;
    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;

    public StoreMyPageDto getStoreMyPageInfo(String storeId) {
        log.info("Fetching store my page info for storeId: {}", storeId);
        StoreMyPageDto store = storeMyPageMapper.getStoreMyPageInfo(storeId);
        if (store == null) {
            throw new IllegalArgumentException("Store not found with ID: " + storeId);
        }
        return store;
    }

    public List<StoreReservationDto> findReservations(String storeId) {
        log.info("service get store reservations");
        List<StoreReservationDto> reservations = reservationMapper.findReservations(storeId);

        for (StoreReservationDto dto : reservations) {
            ReservationDetailDto rdto = ReservationDetailDto.builder()
                    .pickedUpAt(dto.getPickedUpAt())
                    .pickupTime(dto.getPickupTime())
                    .cancelReservationAt(dto.getCancelReservationAt())
                    .reservationTime(dto.getReservationTime())
                    .build();
            ReservationStatus status = reservationService.determinePickUpStatus(rdto);
            dto.setStatus(status);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월dd일 HH시mm분");

        for (StoreReservationDto reservation : reservations) {
            if (reservation.getReservationTime() != null) {
                reservation.setReservationTimeF(reservation.getReservationTime().format(formatter));
            }
            if (reservation.getCancelReservationAt() != null) {
                reservation.setCancelReservationAtF(reservation.getCancelReservationAt().format(formatter));
            }
            if (reservation.getPickedUpAt() != null) {
                reservation.setPickedUpAtF(reservation.getPickedUpAt().format(formatter));
            }
            if (reservation.getPickupTime() != null) {
                reservation.setPickupTimeF(reservation.getPickupTime().format(formatter));
            }
            if (reservation.getProductUploadDate() != null) {
                reservation.setProductUploadDateF(reservation.getProductUploadDate().format(formatter));
            }
        }

        return reservations;
    }

    public StoreMyPageCalendarModalDto getStoreMyPageCalendarModalInfo(String storeId, String date) {
        log.info("service get store my page calendar modal info");
        List<StoreMyPageCalendarModalDto> list = storeMyPageMapper.getStoreMyPageCalendarModalInfo(storeId, date);
        if (list.isEmpty()) {
            throw new IllegalArgumentException("No calendar info found for storeId: " + storeId + " on date: " + date);
        }
        StoreMyPageCalendarModalDto dto = list.get(0);

        List<ProductInfoDto> productCntByDate = storeMyPageMapper.getProductCntByDate(storeId, date);
        int tpuc = productCntByDate.stream()
                .filter(product -> product.getPickedUpAt() != null && product.getReservationTime() != null && product.getCancelReservationAt() == null)
                .collect(Collectors.toList())
                .size();

        int tpc = productCntByDate.size();

        dto.setTodayPickedUpCnt(tpuc);
        dto.setTodayProductCnt(tpc);
        return dto;
    }

    public StoreStatsDto getStats(String storeId) {
        List<StoreReservationDto> reservations = reservationMapper.findReservations(storeId);

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

    public boolean undoHoliday(String storeId, String holidayDate) {
        log.info("service remove holiday");
        storeMyPageMapper.undoHoliday(storeId, holidayDate);
        List<StoreHolidayDto> holidays = storeMyPageMapper.getHolidays(storeId);
        for (StoreHolidayDto holiday : holidays) {
            if (holiday.getHolidays().equals(holidayDate)) {
                return false;
            }
        }
        return true;
    }

    public List<StoreHolidayDto> getHolidays(String storeId) {
        log.info("service get holidays");
        return storeMyPageMapper.getHolidays(storeId);
    }

    public boolean checkHoliday(String storeId, String date) {
        log.info("service check holiday");
        List<StoreHolidayDto> holidays = storeMyPageMapper.getHolidays(storeId);
        for (StoreHolidayDto holiday : holidays) {
            if (holiday.getHolidays().equals(date)) {
                return true;
            }
        }
        return false;
    }

    public boolean setPickupTime(String storeId, String date, String pickupTime) {
        log.info("service set pickup time");
//        storeMyPageMapper.setPickupTime(storeId, date, pickupTime);
        return true;
    }

    public StoreProductCountDto getStoreProductCnt(String storeId) {
        if (storeId == null) {
            throw new IllegalArgumentException("Store ID cannot be null");
        }

        StoreMyPageDto storeMyPageInfo = getStoreMyPageInfo(storeId);
        int productCnt = storeMyPageInfo.getProductCnt();

        LocalDate today = LocalDate.now();
        List<ProductInfoDto> dto = storeMyPageMapper.getProductCntByDate(storeId, today.toString());
        int todayProductCnt = dto.size();
        int todayPickedUpCnt = dto.stream()
                .filter(product -> product.getPickedUpAt() != null && product.getReservationTime() != null)
                .collect(Collectors.toList())
                .size();

        int readyToPickUpCnt = dto.stream()
                .filter(product -> product.getReservationTime() != null
                        && product.getPickedUpAt() == null
                        && product.getCancelReservationAt() == null)
                .collect(Collectors.toList())
                .size();

        int remainCnt = todayProductCnt - todayPickedUpCnt - readyToPickUpCnt;
        return StoreProductCountDto.builder()
                .productCnt(productCnt)
                .todayProductCnt(todayProductCnt)
                .todayPickedUpCnt(todayPickedUpCnt)
                .readyToPickUpCnt(readyToPickUpCnt)
                .remainCnt(remainCnt)
                .build();
    }

    public boolean updateProductCnt(String storeId, int newCount) {
        log.info("service update product count");

        StoreMyPageDto storeMyPageInfo = getStoreMyPageInfo(storeId);
        LocalTime closedAt = storeMyPageInfo.getClosedAt();
        LocalDate today = LocalDate.now(); // 오늘 날짜
        LocalTime closedAtTime = LocalTime.parse(closedAt.toString()); // closedAt을 LocalTime으로 파싱

        LocalDateTime pickupDateTime = today.atTime(closedAtTime); // LocalDate와 LocalTime을 결합하여 LocalDateTime 생성
        String pickupTime = pickupDateTime.toString(); // 문자열로 변환

        for (int i = 0; i < newCount; i++) {
            storeMyPageMapper.updateProductAuto(storeId, pickupTime);
        }

        List<ProductInfoDto> dto = storeMyPageMapper.getProductCntByDate(storeId, today.toString());
        // true false 뭐로 반환할지 생각좀...
        return true;
    }

    /**
     * 특정 일자의 픽업 완료된 랜덤박스 개수를 가져오는 메서드
     * @param storeId 가게 ID
     * @param date 특정 일자
     * @return 픽업 완료된 랜덤박스 개수
     */
    public int countPickedUpProductsByDate(String storeId, String date) {
        return storeMyPageMapper.countPickedUpProductsByDate(storeId, date);
    }
}