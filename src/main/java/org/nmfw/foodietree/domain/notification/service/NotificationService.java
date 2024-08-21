package org.nmfw.foodietree.domain.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.notification.dto.req.NotificationDataDto;
import org.nmfw.foodietree.domain.notification.dto.res.MessageDto;
import org.nmfw.foodietree.domain.notification.entity.Notification;
import org.nmfw.foodietree.domain.notification.repository.NotificationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;

    // 예약 추가 시 예약고객 및 가게에 알림 발송
    public void sendCreatedReservationAlert(NotificationDataDto dto) {
        String customerId = dto.getCustomerId();
        String storeId = dto.getStoreId();
        List<String> list = dto.getTargetId();
        MessageDto message = MessageDto.builder()
                .type("RESERVATION_ADD")
                .receiverId(customerId)
                .senderId(storeId)
                .label("예약")
                .content(dto.getStoreName() + " 스페셜팩 " +list.size()+ "개 예약하셨습니다. ")
                .targetId(list)
                .isRead(false)
                .build();
        MessageDto messageStore = MessageDto.builder()
                .type("RESERVATION_ADD")
                .receiverId(storeId)
                .senderId(customerId)
                .label("예약")
                .content(customerId + " " + list.size()+"건")
                .targetId(list)
                .isRead(false)
                .build();
        messagingTemplate.convertAndSend("/queue/customer/" + customerId, saveEntityAndGetDto(message));
        log.info("\nMessage sent to customer queue: {}", message);
        messagingTemplate.convertAndSend("/topic/store/" + storeId, saveEntityAndGetDto(messageStore));
        log.info("\nMessage sent to store topic: {}", messageStore);

    }

    // 예약 취소 시 취소한 고객, 가게에게 알림
    public void sendCancelReservationAlert(NotificationDataDto dto) {
        String customerId = dto.getCustomerId();
        String storeId = dto.getStoreId();
        List<String> list = dto.getTargetId();

        MessageDto message = MessageDto.builder()
                .type("RESERVATION_CANCEL")
                .receiverId(customerId)
                .senderId(storeId)
                .label("예약 취소")
                .content(dto.getStoreName() + " 예약을 취소하셨습니다. ")
                .targetId(list)
                .isRead(false)
                .build();
        MessageDto messageStore = MessageDto.builder()
                .type("RESERVATION_CANCEL")
                .receiverId(storeId)
                .senderId(customerId)
                .label("예약 취소")
                .content(customerId + "님 주문 취소‼️ ")
                .targetId(list)
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
        MessageDto message = MessageDto.builder()
                .type("PICKUP_REVIEW")
                .receiverId(customerId)
                .senderId(storeId)
                .label("리뷰")
                .content(dto.getStoreName() + " 스페셜팩 어떠셨나요? 리뷰로 공유해주세요 😉 ")
                .targetId(dto.getTargetId())
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
                .label("픽업 완료")
                .content(dto.getStoreName() + " 맛있게 드세요! 🤤 ")
                .targetId(dto.getTargetId())
                .isRead(false)
                .build();

        messagingTemplate.convertAndSend("/queue/customer/" + customerId, saveEntityAndGetDto(message));
    }
    // 리스트 조회
    public List<MessageDto> getList(String userId) {
        List<MessageDto> list = notificationRepository.findAllByReceiverId(userId);
        log.debug("\ngetList: {}", list);
        return list;
    }

    public MessageDto saveEntityAndGetDto(MessageDto dto) {
        Notification save = notificationRepository.save(dto.toEntity());
        log.debug("\n알림 엔터티 저장: {}", save);
        if(save == null) throw new RuntimeException("알림 처리 실패");
        dto.setId(save.getNotificationId());
        dto.setCreatedAt(save.getCreatedAt());
        log.debug("세이브엔터티 dto: {}", dto);
        return dto;
    }
    // 하나의 알림을 읽음처리
    public boolean markOneAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 알림입니다."));
        notification.setIsRead("y");
        Notification save = notificationRepository.save(notification);
        log.debug("\n읽음 처리 수정된 알림 {}",save);
        return true;
    }
}
