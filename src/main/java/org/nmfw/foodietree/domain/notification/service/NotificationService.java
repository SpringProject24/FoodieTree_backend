package org.nmfw.foodietree.domain.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.notification.dto.req.NotificationDataDto;
import org.nmfw.foodietree.domain.notification.dto.res.MessageDto;
import org.nmfw.foodietree.domain.notification.entity.Notification;
import org.nmfw.foodietree.domain.notification.repository.NotificationRepository;
import org.nmfw.foodietree.domain.product.repository.ProductRepository;
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
    private final NotificationRepository notificationRepository;

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

        messagingTemplate.convertAndSend("/queue/customer/" + customerId, saveEntityAndGetDto(message));
        log.info("\nMessage sent to customer queue: {}", message);
        messagingTemplate.convertAndSend("/topic/store/" + data.get("storeId"), saveEntityAndGetDto(messageStore));
        log.info("\nMessage sent to store topic: {}", messageStore);

    }

    // 예약 취소 시 취소한 고객, 가게에게 알림
    public void sendCancelReservationAlert(NotificationDataDto dto) {
        String customerId = dto.getCustomerId();
        String storeId = dto.getStoreId();
        String targetId = dto.getTargetId().get(0);

        MessageDto message = MessageDto.builder()
                .type("RESERVATION_CANCEL")
                .receiverId(customerId)
                .senderId(storeId)
                .content("[예약 취소]" + dto.getStoreName() + " 예약을 취소하셨습니다.")
                .targetId(targetId)
                .isRead(false)
                .build();
        MessageDto messageStore = MessageDto.builder()
                .type("RESERVATION_CANCEL")
                .receiverId(storeId)
                .senderId(customerId)
                .content("[예약 취소‼️] " + customerId)
                .targetId(targetId)
                .isRead(false)
                .build();

        messagingTemplate.convertAndSend("/queue/customer/" + customerId, saveEntityAndGetDto(message));
        messagingTemplate.convertAndSend("/topic/store/" + storeId, saveEntityAndGetDto(messageStore));
        log.debug("예약 취소 알림 발송: {}", message);
    }
    // 픽업 완료 시 고객에게 리뷰 권유 알림
    public void sendReviewRequest(NotificationDataDto dto) {

        String customerId = dto.getCustomerId();
        String storeId = dto.getStoreId();
        String targetId = dto.getTargetId().get(0);

        MessageDto message = MessageDto.builder()
                .type("PICKUP_REVIEW")
                .receiverId(customerId)
                .senderId(storeId)
                .content("[리뷰]" +storeId + " 리뷰를 남기면 뱃지를 드려요😉")
                .targetId(targetId)
                .isRead(false)
                .build();

        messagingTemplate.convertAndSend("/queue/customer/" + customerId, saveEntityAndGetDto(message));
    }

    // 가게에서 픽업 확인 시 고객에게 픽업 완료 알림
    public void sendPickupConfirm(NotificationDataDto dto) {
        String customerId = dto.getCustomerId();
        MessageDto message = MessageDto.builder()
                .type("PICKUP_CONFIRM")
                .receiverId(customerId)
                .senderId(dto.getStoreId())
                .content("[픽업 완료]" + dto.getStoreId() + " ")
                .targetId(String.valueOf(dto.getTargetId()))
                .isRead(false)
                .build();

        messagingTemplate.convertAndSend("/queue/customer/" + customerId, saveEntityAndGetDto(message));
    }

    public MessageDto saveEntityAndGetDto(MessageDto dto) {
        Notification save = notificationRepository.save(dto.toEntity());
        log.debug("\n알림 엔터티 저장: {}", save);
        if(save == null) throw new RuntimeException("알림 처리 실패");
        dto.setId(save.getNotificationId());
        return dto;
    }

}
