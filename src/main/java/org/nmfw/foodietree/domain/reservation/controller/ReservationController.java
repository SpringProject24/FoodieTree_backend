package org.nmfw.foodietree.domain.reservation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.security.TokenProvider;
import org.nmfw.foodietree.domain.customer.service.CustomerMyPageService;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationDetailDto;
import org.nmfw.foodietree.domain.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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

    // 테스트용 변수, 추후 토큰에서 사용하는것으로 변경 예정
//    String customerId = "test@gmail.com";
    int reservationId = 38;

    /**
     * 특정 고객의 예약 목록 조회
     * @return 예약 목록 DTO 리스트
     */
    @GetMapping("/list")
    public ResponseEntity<List<ReservationDetailDto>> getReservationList(
            @AuthenticationPrincipal TokenProvider.TokenUserInfo userInfo
    ) {
        // 추후 토큰을 통해 고객 ID를 가져옴
        String customerId = userInfo.getUsername();
        List<ReservationDetailDto> reservations = customerMyPageService.getReservationList(customerId);
        return ResponseEntity.ok().body(reservations);
    }
    

    /**
     * 특정 예약을 취소
     * @return 취소 성공 여부
     */
    @PatchMapping("/cancel")
    public ResponseEntity<?> cancelReservation(@RequestParam long reservationId) {
        log.info("cancel reservation with ID: {}", reservationId);
        // 추후 토큰을 통해 예약 ID를 가져옴
        // int reservationId = getReservationIdFromToken();
        boolean flag = reservationService.cancelReservation(reservationId);
        return flag ? ResponseEntity.ok().body(true) : ResponseEntity.badRequest().body(false);
    }

    /**
     * 특정 예약을 픽업 완료로 변경
     * @return 픽업 완료 성공 여부
     */
    @PatchMapping("/pickup")
    public ResponseEntity<?> completePickup(@RequestParam long reservationId) {
        log.info("complete pickup with ID: {}", reservationId);
        // 추후 토큰을 통해 예약 ID를 가져옴
        // int reservationId = getReservationIdFromToken();
        boolean flag = reservationService.completePickup(reservationId);
        return flag ? ResponseEntity.ok().body(true) : ResponseEntity.badRequest().body(false);
    }

    /**
     * 특정 예약의 취소 가능 여부 확인
     * @return 취소 가능 여부
     */
    @GetMapping("/check-cancel")
    public ResponseEntity<?> checkCancel(@RequestParam long reservationId) {
        log.info("check cancel is allowed without cancel fee for reservation ID: {}", reservationId);
        boolean flag = reservationService.isCancelAllowed(reservationId);
        return flag ? ResponseEntity.ok().body(true) : ResponseEntity.badRequest().body(false);
    }

    /**
     * 특정 예약의 픽업 가능 여부 확인
     * @return 픽업 가능 여부
     */
    @GetMapping("/check-pickup")
    public ResponseEntity<?> checkPickup(@RequestParam long reservationId) {
        log.info("check pickup for reservation ID: {}", reservationId);
        boolean flag = reservationService.isPickupAllowed(reservationId);
        return flag ? ResponseEntity.ok().body(true) : ResponseEntity.badRequest().body("픽업 확인 실패");
    }

    /**
     * 특정 예약의 상세 정보 조회
     * @param reservationId 예약 ID
     * @return 예약 상세 정보 DTO
     */
    @GetMapping("/{reservationId}/modal/detail")
    public ResponseEntity<?> getReservationDetail(@PathVariable long reservationId) {
        log.info("get reservation detail for reservation ID: {}", reservationId);
        ReservationDetailDto dto = reservationService.getReservationDetail(reservationId);
        return ResponseEntity.ok().body(dto);
    }

    /**
     * 새로운 예약을 생성, 테스트 미실시
     * @param customerId 고객 ID
     * @param data 예약 생성에 필요한 데이터 맵
     * @return 예약 생성 성공 여부
     */
    @PostMapping("/create-reservation")
    @CrossOrigin
    public ResponseEntity<?> createReservation(@RequestParam String customerId, @RequestBody Map<String, String> data) {
        boolean flag = reservationService.createReservation(customerId, data);
        return flag ? ResponseEntity.ok().body(true) : ResponseEntity.badRequest().body(false);
    }

    /**
     * 현재 인증된 사용자로부터
     * 고객 ID를 추출하는 메서드
     * @return 고객 ID
     */
    private String getCustomerIdFromToken() {
        TokenProvider.TokenUserInfo tokenUserInfo = (TokenProvider.TokenUserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return tokenUserInfo.getEmail(); // 윤종 임의 수정 getUserId-> getEmail 24/07/31 21:11
    }

    /**
     * 현재 인증된 사용자로부터
     * 예약 ID를 추출하는 메서드
     * @return 예약 ID
     */
    private int getReservationIdFromToken() {
        try {
            TokenProvider.TokenUserInfo tokenUserInfo = (TokenProvider.TokenUserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // 추후 실제 예약 ID를 토큰에서 추출
            return reservationId; // 임시로 하드코딩된 값 반환
        } catch (Exception e) {
            log.error("Error while getting reservation ID from token", e);
            throw e;
        }
    }
}