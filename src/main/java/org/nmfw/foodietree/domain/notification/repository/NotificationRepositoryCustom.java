package org.nmfw.foodietree.domain.notification.repository;

import org.nmfw.foodietree.domain.notification.dto.res.MessageDto;

import java.util.List;

public interface NotificationRepositoryCustom {

    // 알림 목록 조회
    List<MessageDto> findAllByReceiverId(String id);

    // 알림 id 리스트 bulk update
    Long updateIsReadAll(List<Long> ids);
}
