package org.nmfw.foodietree.domain.reservation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationDetailDto;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationFoundStoreIdDto;
import org.nmfw.foodietree.domain.reservation.entity.ReservationStatus;
import org.nmfw.foodietree.domain.reservation.mapper.ReservationMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {
    private final ReservationMapper reservationMapper;

    /**
     * 예약을 취소하고 취소가 성공했는지 여부를 반환
     * @param reservationId 취소할 예약의 ID
     * @return 취소가 완료되었는지 여부
     */
    public boolean cancelReservation(int reservationId) {
        reservationMapper.cancelReservation(reservationId);
        ReservationDetailDto reservation = reservationMapper.findReservationByReservationId(reservationId);
        if (reservation.getCancelReservationAt() != null) {
            reservation.setStatus(ReservationStatus.CANCELED);
            return true;
        }
        return false;
    }

    /**
     * 예약을 픽업 완료로 변경하고 완료 여부를 반환
     * @param reservationId 픽업 완료할 예약의 ID
     * @return 픽업 완료가 성공했는지 여부
     */
    public boolean completePickup(int reservationId) {
        reservationMapper.completePickup(reservationId);
        ReservationDetailDto reservation = reservationMapper.findReservationByReservationId(reservationId);
        if (reservation.getPickedUpAt() != null) {
            reservation.setStatus(ReservationStatus.PICKEDUP);
            return true;
        }
        return false;
    }

    /**
     * 픽업 가능 여부 확인, 현재 시점에서 예약 픽업이 가능한지 여부를 반환
     * @param reservationId 예약 ID
     * @return 픽업 가능 여부
     */
    public boolean isPickupAllowed(int reservationId) {
        ReservationDetailDto reservation = reservationMapper.findReservationByReservationId(reservationId);
        return LocalDateTime.now().isBefore(reservation.getPickupTime());
    }

    /**
     * 현재 시점에서 예약 취소가 가능한지 여부를 반환
     * @param reservationId 예약 ID
     * @return 취소 가능 여부
     */
    public boolean isCancelAllowed(int reservationId) {
        ReservationDetailDto reservation = reservationMapper.findReservationByReservationId(reservationId);
        return LocalDateTime.now().isBefore(reservation.getPickupTime().minusHours(1));
    }

    /**
     * 예약의 현재 상태를 결정하고 반환합니다.
     * @param reservation 예약 객체
     * @return 예약 상태
     */
    public ReservationStatus determinePickUpStatus(ReservationDetailDto reservation) {
        if (reservation.getPickedUpAt() != null) {
            return ReservationStatus.PICKEDUP;
        } else if (reservation.getCancelReservationAt() != null) {
            return ReservationStatus.CANCELED;
        } else if (reservation.getPickupTime().isBefore(LocalDateTime.now())) {
            return ReservationStatus.NOSHOW;
        } else {
            return ReservationStatus.RESERVED;
        }
    }

    /**
     * 예약 상세 정보 조회
     * @param reservationId 예약 ID
     * @return 예약 상세 정보 DTO
     */
    public ReservationDetailDto getReservationDetail(int reservationId) {
        ReservationDetailDto dto = reservationMapper.findReservationByReservationId(reservationId);
        dto.setStatus(determinePickUpStatus(dto));
        dto.formatTimes(); // 시간 필드 포멧팅
        return dto;
    }

    /**
     * 예약 생성 메서드
     * @param customerId 고객 ID
     * @param data 예약 생성에 필요한 데이터 맵
     * @return 예약 생성 성공 여부
     */
    public boolean createReservation(String customerId, Map<String, String> data) {
        log.info("Creating reservation for customer: {}, data: {}", customerId, data);
        int cnt = Integer.parseInt(data.get("cnt"));
        String storeId = data.get("storeId");
        List<ReservationFoundStoreIdDto> list = reservationMapper.findByStoreIdLimit(storeId, cnt);
        for (ReservationFoundStoreIdDto tar : list) {
            long productId = tar.getProductId();
            boolean flag = reservationMapper.createReservation(customerId, productId);
            if (!flag) return false;
        }
        return true;
    }
}