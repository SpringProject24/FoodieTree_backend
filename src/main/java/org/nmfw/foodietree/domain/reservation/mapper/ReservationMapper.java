package org.nmfw.foodietree.domain.reservation.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nmfw.foodietree.domain.customer.dto.resp.MyPageReservationDetailDto;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationFoundStoreIdDto;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationModalDetailDto;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationStatusDto;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReservationMapper {

    // 예약 조회
    List<MyPageReservationDetailDto> findAll(@Param("customerId") String customerId);

    // 예약 시간 상세 조회
    ReservationStatusDto findByReservationId(@Param("reservationId") int reservationId);

    // 예약 취소
    void cancelReservation(@Param("reservationId") int reservationId);

    // 픽업 완료
    void completePickup(@Param("reservationId") int reservationId);

    ReservationModalDetailDto findModalDetailByReservationId(@Param("reservationId") int reservationId);

    boolean createReservation(@Param("customerId") String customerId, @Param("productId") long productId);

    List<ReservationFoundStoreIdDto> findByStoreIdLimit(@Param("storeId") String storeId, @Param("cnt") int cnt);
}
