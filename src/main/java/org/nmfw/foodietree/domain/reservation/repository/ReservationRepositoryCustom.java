package org.nmfw.foodietree.domain.reservation.repository;

import org.apache.ibatis.annotations.Param;
import org.nmfw.foodietree.domain.reservation.dto.resp.PaymentIdDto;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationDetailDto;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationFoundStoreIdDto;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationStatusDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreReservationDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReservationRepositoryCustom {

    // 예약 생성 -> JPA save
//    boolean createReservation(String customerId, Long productId);

    // 예약 취소
    void cancelReservation(Long reservationId);

    // 픽업 완료
    void completePickup(Long reservationId);

    // customerId로 예약 목록 조회
    List<ReservationDetailDto> findReservationsByCustomerId(String customerId);

    // 예약 시간 조회
    ReservationStatusDto findTimeByReservationId(Long reservationId);

    // 예약 상세 조회
    ReservationDetailDto findReservationByReservationId(Long reservationId);

    // 예약 가능 제품 조회
    List<ReservationFoundStoreIdDto> findByStoreIdLimit(String storeId, int cnt);

    // 가게 예약리스트 조회
    List<StoreReservationDto> findReservations(String storeId);

    // 결제 식별 값으로 예약건들의 가격정보 조회
    List<PaymentIdDto> findByPaymentIdForPrice(String paymentId);

    List<StoreReservationDto> findReservationsByStoreId(String storeId);
}
