package org.nmfw.foodietree.domain.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.notification.dto.req.NotificationDataDto;
import org.nmfw.foodietree.domain.notification.dto.res.MessageDto;
import org.nmfw.foodietree.domain.notification.entity.Notification;
import org.nmfw.foodietree.domain.notification.repository.NotificationRepository;
import org.nmfw.foodietree.domain.store.entity.value.ApproveStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;
    private final TaskScheduler notificationTaskScheduler;

    public NotificationService(SimpMessagingTemplate messagingTemplate,
                               NotificationRepository notificationRepository,
                               @Qualifier("notificationTaskScheduler") TaskScheduler notificationTaskScheduler) {
        this.messagingTemplate = messagingTemplate;
        this.notificationRepository = notificationRepository;
        this.notificationTaskScheduler = notificationTaskScheduler;
    }

    long minutesDelay = 1; // 리뷰 요청 알림 지연 - 1분

    /**
     * 예약 생성 시 고객과 가게에 알림 발송
     *
     * @param dto - 알림에 필요한 데이터
     */
    public void sendCreatedReservationAlert(NotificationDataDto dto) {
        String customerId = dto.getCustomerId();
        String storeId = dto.getStoreId();
        List<String> list = dto.getTargetId();
        MessageDto message = MessageDto.builder()
                .type("RESERVATION_ADD")
                .receiverId(customerId)
                .senderId(storeId)
                .label("예약")
                .content(dto.getStoreName() + " 스페셜팩 " + list.size() + "개 예약하셨습니다. ")
                .targetId(list)
                .isRead(false)
                .build();
        MessageDto messageStore = MessageDto.builder()
                .type("RESERVATION_ADD")
                .receiverId(storeId)
                .senderId(customerId)
                .label("예약")
                .content(customerId + " " + list.size() + "건")
                .targetId(list)
                .isRead(false)
                .build();
        messagingTemplate.convertAndSend("/queue/customer/" + customerId, saveEntityAndGetDto(message));
        log.info("\nMessage sent to customer queue: {}", message);
        messagingTemplate.convertAndSend("/topic/store/" + storeId, saveEntityAndGetDto(messageStore));
        log.info("\nMessage sent to store topic: {}", messageStore);

    }

    /**
     * 예약 취소 시 고객과 가게에 알림 발송
     *
     * @param dto - 알림에 필요한 데이터
     */
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

    /**
     * 픽업 완료 후 고객에게 리뷰 요청 알림 발송
     *
     * @param dto - 알림에 필요한 데이터
     */
    public void sendReviewRequest(NotificationDataDto dto) {
        String customerId = dto.getCustomerId();
        String storeId = dto.getStoreId();
        MessageDto message = MessageDto.builder()
                .type("PICKUP_REVIEW")
                .receiverId(customerId)
                .senderId(storeId)
                .label("리뷰")
                .content(dto.getStoreName() + " 어떠셨나요? 궁금해요 😉 ")
                .targetId(dto.getTargetId())
                .isRead(false)
                .build();

        messagingTemplate.convertAndSend("/queue/customer/" + customerId, saveEntityAndGetDto(message));
    }

    /**
     * 가게에서 픽업을 확인하면 고객에게 픽업 완료 알림 발송
     *
     * @param dto - 알림에 필요한 데이터
     */
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

    /**
     * 특정 사용자 ID에 해당하는 모든 알림 메시지 조회
     *
     * @param userId - 사용자 ID
     * @return List<MessageDto> - 알림 메시지 리스트
     */
    public List<MessageDto> getList(String userId) {
        List<MessageDto> list = notificationRepository.findAllByReceiverId(userId);
        log.debug("\ngetList: {}", list);
        return list;
    }

    /**
     * 알림 엔터티를 저장하고 DTO로 반환
     *
     * @param dto - 저장할 알림 메시지 DTO
     * @return MessageDto - 저장된 알림 메시지 DTO
     */
    public MessageDto saveEntityAndGetDto(MessageDto dto) {
        Notification save = notificationRepository.save(dto.toEntity());
        log.debug("\n알림 엔터티 저장: {}", save);
        dto.setId(save.getNotificationId());
        dto.setCreatedAt(save.getCreatedAt());
        log.debug("세이브엔터티 dto: {}", dto);
        return dto;
    }

    /**
     * 특정 알림을 읽음 처리
     *
     * @param id - 알림 ID
     * @return boolean - 처리 결과
     */
    public boolean markOneAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 알림입니다."));
        notification.setRead(true);
        Notification save = notificationRepository.save(notification);
        log.debug("\n읽음 처리 수정된 알림 {}", save);
        return true;
    }

    /**
     * 다수의 알림을 모두 읽음 처리
     *
     * @param ids - 알림 ID 리스트
     * @return boolean - 처리 결과
     */
    public boolean markAllAsRead(List<Long> ids) {
        Long resultCnt = notificationRepository.updateIsReadAll(ids);
        return resultCnt == ids.size();
    }

    /**
     * 픽업 완료 30분 후 리뷰알림 발송 예약
     *
     * @param dto - 알림에 필요한 정보
     */
    public void scheduleReviewRequest(NotificationDataDto dto) {
        LocalDateTime targetTime = LocalDateTime.now().plusMinutes(minutesDelay);
        notificationTaskScheduler.schedule(() -> sendReviewRequest(dto), getInstantTime(targetTime));
    }

    /**
     * LocalDateTime 타입을 Date 타입으로 변환
     *
     * @param targetTime - 변환하고자 하는 LocalDateTime
     * @return Date
     */
    public Date getInstantTime(LocalDateTime targetTime) {
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        return Date.from(targetTime.atZone(zoneId).toInstant());
    }

    /**
     *
     * @param list - 알림에 필요한 데이터
     * @param approveStatus - 등록 요청 결과 ENUM (APPROVED,REJECTED)
     * @param adminId - 해당 요청을 처리한 관리자 계정
     */
    public void sendApprovalResult(List<NotificationDataDto> list, ApproveStatus approveStatus, String adminId) {
        String status = approveStatus.getDesc();

        for (NotificationDataDto dto : list) {
            String storeId = dto.getStoreId();
            MessageDto message = MessageDto.builder()
                    .type("APPROVE_RESULT")
                    .receiverId(storeId)
                    .senderId(adminId)
                    .label("등록 결과")
                    .content(dto.getStoreName() + " 등록이 " + status + " 되었습니다.")
                    .targetId(dto.getTargetId())
                    .isRead(false)
                    .build();
            messagingTemplate.convertAndSend("/topic/store/" + storeId, saveEntityAndGetDto(message));
        }
    }
}
