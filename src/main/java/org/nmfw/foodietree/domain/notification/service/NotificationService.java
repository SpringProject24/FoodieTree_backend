package org.nmfw.foodietree.domain.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.notification.dto.res.MessageDto;
import org.nmfw.foodietree.domain.product.entity.Product;
import org.nmfw.foodietree.domain.product.repository.ProductRepository;
import org.nmfw.foodietree.domain.reservation.entity.Reservation;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.nmfw.foodietree.domain.store.repository.StoreRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ProductRepository productRepository;

    // 예약 추가 시 예약고객 및 가게에 알림 발송
    public void sendCreatedReservationAlert(String customerId, Map<String, String> data) {

        MessageDto message = MessageDto.builder()
                .type("RESERVATION_ADD")
                .receiverId(customerId)
                .senderId(data.get("storeId"))
                .content("[예약]" + data.get("storeId") + " 예약 하셨습니다.")
                .targetId(data.get("targetId"))
                .isRead(false)
                .build();
        MessageDto messageStore = MessageDto.builder()
                .type("RESERVATION_ADD")
                .receiverId(data.get("storeId"))
                .senderId(customerId)
                .content("[예약]" + customerId)
                .targetId(data.get("targetId"))
                .isRead(false)
                .build();
        messagingTemplate.convertAndSend("/queue/customer/" + customerId, message);
        log.info("\nMessage sent to customer queue: {}", message);
        messagingTemplate.convertAndSend("/topic/store/" + data.get("storeId"), messageStore);
        log.info("\nMessage sent to store topic: {}", messageStore);

    }

    // 예약 취소 시 취소한 고객, 가게에게 알림
    public void sendCancelReservationAlert(Reservation reservation) {
        String customerId = reservation.getCustomerId();
        Product byProductId = productRepository.findById(reservation.getProductId())
                .orElseThrow(() -> new RuntimeException("재고가 없습니다."));
        Store store = byProductId.getStore();
        log.debug("취소 product 기준 store: {}", store);
        String storeId = byProductId.getStoreId();
        log.debug("취소 storeId 확인: {}", storeId);

        MessageDto message = MessageDto.builder()
                .type("RESERVATION_CANCEL")
                .receiverId(customerId)
                .senderId(storeId)
                .content("[예약 취소]" + storeId + " 예약을 취소하셨습니다.")
                .targetId(String.valueOf(reservation.getReservationId()))
                .isRead(false)
                .build();
        MessageDto messageStore = MessageDto.builder()
                .type("RESERVATION_CANCEL")
                .receiverId(storeId)
                .senderId(customerId)
                .content("[예약 취소‼️] "+reservation.getReservationId()+ "/" + customerId)
                .targetId(String.valueOf(reservation.getReservationId()))
                .isRead(false)
                .build();

        messagingTemplate.convertAndSend("/queue/customer/" + customerId, message);
        messagingTemplate.convertAndSend("/topic/store/" + storeId, messageStore);
        log.debug("예약 취소 알림 발송: {}", message);
    }
    // 픽업 완료 시 고객에게 리뷰 권유 알림
    public void sendReviewRequest(Reservation reservation) {

        String customerId = reservation.getCustomerId();
        Product byProductId = productRepository.findById(reservation.getProductId())
                .orElseThrow(() -> new RuntimeException("재고가 없습니다."));
        Store store = byProductId.getStore();
        log.debug("픽업리뷰 product 기준 store: {}", store);
        String storeId = byProductId.getStoreId();
        log.debug("픽업리뷰 storeId 확인: {}", storeId);

        // 리뷰 권유 알림을 보내는 로직을 구현
        MessageDto message = MessageDto.builder()
                .type("PICKUP_REVIEW")
                .receiverId(customerId)
                .senderId(storeId)
                .content("[리뷰]" +storeId + " 리뷰를 남기면 뱃지를 드려요😉")
                .targetId(String.valueOf(reservation.getReservationId()))
                .isRead(false)
                .build();
        messagingTemplate.convertAndSend("/queue/customer/" + customerId, message);
    }

}
