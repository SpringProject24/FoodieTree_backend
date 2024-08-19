package org.nmfw.foodietree.domain.notification.repository;

import org.nmfw.foodietree.domain.notification.dto.res.MessageDto;

import java.util.List;
import java.util.Optional;

public interface NotificationRepositoryCustom {

    // 알림 목록 조회
    List<MessageDto> findAllByReceiverId(String id);
}
