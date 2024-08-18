package org.nmfw.foodietree.domain.reservation.service;

import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.product.repository.ProductRepository;
import org.nmfw.foodietree.domain.reservation.dto.resp.PaymentIdDto;
import org.nmfw.foodietree.domain.reservation.dto.resp.PaymentResponseDto;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationDetailDto;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationFoundStoreIdDto;
import org.nmfw.foodietree.domain.reservation.entity.Reservation;
import org.nmfw.foodietree.domain.reservation.entity.ReservationStatus;
import org.nmfw.foodietree.domain.reservation.entity.value.PaymentStatus;
import org.nmfw.foodietree.domain.reservation.mapper.ReservationMapper;
import org.nmfw.foodietree.domain.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReservationService {
    private final ReservationMapper reservationMapper;
    private final ReservationRepository reservationRepository;
    private final ProductRepository productRepository;
    @Value("${env.payment.api.url}")
    private String apiUrl;
    @Value("${env.payment.api.key}")
    private String paymentKey;

    /**
     * 예약을 취소하고 취소가 성공했는지 여부를 반환
     * @param reservationId 취소할 예약의 ID
     * @return 취소가 완료되었는지 여부
     */
    public boolean cancelReservation(long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예약 내역이 존재하지 않습니다."));

        // 취소한 적이 없으면 취소
        if(reservation.getCancelReservationAt() == null) {
            reservation.setCancelReservationAt(LocalDateTime.now());
            return true;
        }
        // 이미 픽업했거나, 노쇼인 경우를 확인하지 않아도 되는지?
        return false;
    }

    /**
     * 예약을 픽업 완료로 변경하고 완료 여부를 반환
     * @param reservationId 픽업 완료할 예약의 ID
     * @return 픽업 완료가 성공했는지 여부
     */
    public boolean completePickup(long reservationId) {

//        ReservationDetailDto reservation = reservationRepository.findReservationByReservationId(reservationId);
//        if(reservation == null) throw new RuntimeException("예약내역을 찾울 수 없습니다.");
//
//        // 취소시간, 픽업시간이 있는 경우 false
//        ReservationStatus status = determinePickUpStatus(reservation);
//        if(status == ReservationStatus.RESERVED) {
//            reservationRepository.completePickup(reservationId);
//            return true;
//        }
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예약 내역이 존재하지 않습니다."));

        if(reservation.getPickedUpAt() == null) {
            reservation.setPickedUpAt(LocalDateTime.now());
            return true;
        }

        return false;
    }

    /**
     * 픽업 가능 여부 확인, 현재 시점에서 예약 픽업이 가능한지 여부를 반환
     * @param reservationId 예약 ID
     * @return 픽업 가능 여부
     */
    public boolean isPickupAllowed(long reservationId) {
        ReservationDetailDto reservation = reservationRepository.findReservationByReservationId(reservationId);
//        return LocalDateTime.now().isBefore(reservation.getPickupEndTime());
        return LocalDateTime.now().isBefore(reservation.getPickupTime());
    }

    /**
     * 현재 시점에서 예약 취소가 가능한지 여부를 반환
     * @param reservationId 예약 ID
     * @return 취소 가능 여부
     */
    public boolean isCancelAllowed(long reservationId) {
        ReservationDetailDto reservation = reservationRepository.findReservationByReservationId(reservationId);
//        return LocalDateTime.now().isBefore(reservation.getPickupEndTime().minusHours(1));
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
//        } else if (reservation.getPickupEndTime().isBefore(LocalDateTime.now())) {
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
    public ReservationDetailDto getReservationDetail(long reservationId) {
        ReservationDetailDto dto = reservationRepository.findReservationByReservationId(reservationId);
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
        String paymentId = data.get("paymentId");

        List<ReservationFoundStoreIdDto> list = reservationRepository.findByStoreIdLimit(storeId, cnt);
        if (list.isEmpty()) return false;

        List<Reservation> collect = list.stream()
            .map(e -> Reservation.builder()
                .productId(e.getProductId())
                .customerId(customerId)
                .paymentId(paymentId)
                .build())
            .collect(Collectors.toList());
        reservationRepository.saveAll(collect);
		return true;
	}

    public PaymentStatus processPaymentUpdate(Map<String, String> data) throws InterruptedException {
        String paymentId = data.get("paymentId");
        final PaymentResponseDto[] dto = new PaymentResponseDto[1];
        CountDownLatch cdl = new CountDownLatch(1);

        initiatePaymentRequest(dto, paymentId, cdl); // non-block
        List<PaymentIdDto> paymentIdList = reservationRepository.findByPaymentIdForPrice(paymentId);
        List<Reservation> reservationList = reservationRepository.findByPaymentId(paymentId);
        Integer totalPrice = paymentIdList.stream().reduce(0, (total, y) -> total + y.getPrice(), Integer::sum);
        cdl.await();
        return handlePaymentResponse(dto[0], totalPrice, reservationList);
    }

    private void initiatePaymentRequest(PaymentResponseDto[] tar, String paymentId, CountDownLatch cdl) {
        String url = apiUrl + paymentId;
        WebClient.create()
                .get()
                .uri(url)
                .header("Authorization", "PortOne " + paymentKey)
                .retrieve()
                .bodyToMono(PaymentResponseDto.class)
                .doOnTerminate(cdl::countDown)
                .subscribe(e -> tar[0] = e, error -> {
                    log.warn("{} API Error: {};", this, error.getMessage());
                    cdl.countDown();
                });
    }

    private PaymentStatus handlePaymentResponse(PaymentResponseDto dto, Integer totalPrice, List<Reservation> list) {
        if (totalPrice.equals(dto.getAmount().getPaid())) {
            switch (dto.getStatus()) {
                case ("PAID") :
                    List<Reservation> result = list.stream()
                            .peek(e -> e.setPaymentTime(dto.getPaidAt().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                            .collect(Collectors.toList());
                    reservationRepository.saveAll(result);
                    return PaymentStatus.PAID;
                case ("FAILED") :
                    log.info("failed!");
                    return PaymentStatus.FAILED;
                default:
                    return PaymentStatus.DEFAULT;
            }
        }
        return PaymentStatus.INCONSISTENCY;
    }
}