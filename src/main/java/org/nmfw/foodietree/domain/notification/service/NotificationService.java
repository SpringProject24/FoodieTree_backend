package org.nmfw.foodietree.domain.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.notification.dto.res.MessageDto;
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

}
