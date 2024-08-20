package org.nmfw.foodietree.domain.notification.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.notification.dto.res.MessageDto;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static org.nmfw.foodietree.domain.notification.entity.QNotification.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom{

    private final JPAQueryFactory factory;

    @Override
    public List<MessageDto> findAllByReceiverId(String receiverId) {
        List<MessageDto> list = factory
            .select(Projections.fields(
                MessageDto.class,
                notification.notificationId.as("id"),
                notification.type.as("type"),
                notification.label.as("label"),
                notification.content.as("content"),
                notification.receiverId.as("receiverId"),
                notification.senderId.as("senderId"),
                notification.createdAt.as("createdAt"),
                notification.targetIdList.as("targetId")
                ))
            .from(notification)
            .where(
                notification.receiverId.eq(receiverId)
                    .and(
                        notification.isRead.isNull()
                        .or(notification.createdAt.goe(LocalDateTime.now().minusDays(3)))
                    )
            ).fetch();
        log.debug("\n\n알림 조회 결과: {}",list);
        return list;
    }
}
