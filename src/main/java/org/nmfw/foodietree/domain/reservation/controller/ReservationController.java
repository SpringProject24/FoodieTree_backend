package org.nmfw.foodietree.domain.reservation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.resp.MyPageReservationDetailDto;
import org.nmfw.foodietree.domain.customer.service.CustomerMyPageService;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationDetailDto;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationModalDetailDto;
import org.nmfw.foodietree.domain.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final CustomerMyPageService customerMyPageService;

    /**
     * 특정 고객의 예약 목록 조회
     *
     * @param customerId 고객 ID
     * @return 예약 목록 DTO 리스트
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<List<MyPageReservationDetailDto>> getReservationList(@PathVariable String customerId) {
        List<MyPageReservationDetailDto> reservations = customerMyPageService.getReservationList(customerId);
        return ResponseEntity.ok().body(reservations);
    }

    /**
     * 특정 예약을 취소
     *
     * @param reservationId 취소할 예약의 ID
     * @return 취소 성공 여부
     */
    @PatchMapping("/{reservationId}/cancel")
    public ResponseEntity<?> cancelReservation(@PathVariable int reservationId) {
        log.info("cancel reservation");
        boolean flag = reservationService.cancelReservation(reservationId);
        return flag ? ResponseEntity.ok().body(true) : ResponseEntity.badRequest().body(false);
    }

    /**
     * 특정 예약을 픽업 완료로 변경
     *
     * @param reservationId 픽업 완료할 예약의 ID
     * @return 픽업 완료 성공 여부
     */
    @PatchMapping("/{reservationId}/pickup")
    public ResponseEntity<?> completePickup(@PathVariable int reservationId) {
        log.info("complete pickup");
        boolean flag = reservationService.completePickup(reservationId);
        return flag ? ResponseEntity.ok().body(true) : ResponseEntity.badRequest().body(false);
    }

    /**
     * 특정 예약의 취소 가능 여부 확인
     *
     * @param reservationId 예약 ID
     * @return 취소 가능 여부
     */
    @GetMapping("/{reservationId}/check/cancel")
    public ResponseEntity<?> checkCancel(@PathVariable int reservationId) {
        log.info("check cancel is allowed without cancel fee");
        boolean flag = reservationService.isCancelAllowed(reservationId);
        return flag ? ResponseEntity.ok().body(true) : ResponseEntity.badRequest().body(false);
    }

    /**
     * 특정 예약의 픽업 가능 여부 확인
     *
     * @param reservationId 예약 ID
     * @return 픽업 가능 여부
     */
    @GetMapping("/{reservationId}/check/pickup")
    public ResponseEntity<?> checkPickup(@PathVariable int reservationId) {
        log.info("check pickup");
        boolean flag = reservationService.isPickupAllowed(reservationId);
        return flag ? ResponseEntity.ok().body(true) : ResponseEntity.badRequest().body("픽업 확인 실패");
    }

    /**
     * 특정 예약의 상세 정보 조회
     *
     * @param reservationId 예약 ID
     * @return 예약 상세 정보 DTO
     */
    @GetMapping("/{reservationId}/modal/detail")
    public ResponseEntity<?> getReservationDetail(@PathVariable int reservationId) {
        log.info("get reservation detail");
        ReservationDetailDto dto = reservationService.getReservationDetail(reservationId);
        return ResponseEntity.ok().body(dto);
    }

    /**
     * 새로운 예약을 생성
     *
     * @param customerId 고객 ID
     * @param data 예약 생성에 필요한 데이터 맵
     * @return 예약 생성 성공 여부
     */
    @PostMapping("/{customerId}")
    @CrossOrigin
    public ResponseEntity<?> createReservation(@PathVariable String customerId, @RequestBody Map<String, String> data) {
        boolean flag = reservationService.createReservation(customerId, data);
        return flag ? ResponseEntity.ok().body(true) : ResponseEntity.badRequest().body(false);
    }
}