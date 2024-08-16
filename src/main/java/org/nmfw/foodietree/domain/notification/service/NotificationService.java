package org.nmfw.foodietree.domain.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.security.TokenProvider;
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

import static org.nmfw.foodietree.domain.auth.security.TokenProvider.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;

    // 예약 추가 시 예약고객 및 가게에 알림 발송
    public void sendCreatedReservationAlert(String customerId, Map<String, String> data) {

        MessageDto message = MessageDto.builder()
                .type("RESERVATION_ADD")
                .receiverId(customerId)
                .senderId(data.get("storeId"))
                .content(data.get("storeId") + ": 예약 성공하셨습니다!")
                .targetId(data.get("targetId"))
                .isRead(false)
                .build();
        MessageDto messageStore = MessageDto.builder()
                .type("RESERVATION_ADD")
                .receiverId(data.get("storeId"))
                .senderId(customerId)
                .content("새로운 예약 주문 : " + customerId)
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
        Product byProductId = productRepository.findByProductId(reservation.getProductId());
        Store store = byProductId.getStore();
        log.debug("취소 product 기준 store: {}", store);
        String storeId = byProductId.getStoreId();
        log.debug("취소 storeId 확인: {}", storeId);

        MessageDto message = MessageDto.builder()
                .type("RESERVATION_CANCEL")
                .receiverId(customerId)
                .senderId(storeId)
                .content(storeId + ": 예약이 취소되었습니다.")
                .targetId(String.valueOf(reservation.getReservationId()))
                .isRead(false)
                .build();
        MessageDto messageStore = MessageDto.builder()
                .type("RESERVATION_CANCEL")
                .receiverId(storeId)
                .senderId(customerId)
                .content("‼️예약 취소 : " + customerId)
                .targetId(String.valueOf(reservation.getReservationId()))
                .isRead(false)
                .build();

        messagingTemplate.convertAndSend("/queue/customer/" + customerId, message);
        messagingTemplate.convertAndSend("/topic/store/" + storeId, messageStore);
        log.debug("예약 취소 알림 발송: {}", message);
    }
}
