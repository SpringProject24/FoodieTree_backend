package org.nmfw.foodietree.domain.notification.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.notification.dto.res.MessageDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.nmfw.foodietree.domain.notification.entity.QNotification.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom{

    private final JPAQueryFactory factory;
    private final EntityManager em;

    @Override
    public List<MessageDto> findAllByReceiverId(String receiverId) {
        int days = 3;
        return factory
            .select(Projections.fields(
                MessageDto.class,
                notification.notificationId.as("id"),
                notification.type.as("type"),
                notification.label.as("label"),
                notification.content.as("content"),
                notification.receiverId.as("receiverId"),
                notification.senderId.as("senderId"),
                notification.createdAt.as("createdAt"),
                notification.targetIdList.as("targetId"),
                notification.isRead.as("isRead")
            ))
            .from(notification)
            .where(
                notification.receiverId.eq(receiverId)
                .and(
                    notification.isRead.isFalse()
                    .or(notification.createdAt.goe(LocalDateTime.now().minusDays(days)))
                )
            ).fetch();
    }

    @Override
    public Long updateIsReadAll(List<Long> ids) {
        Long result = factory
                .update(notification)
                .set(notification.isRead, true)
                .where(notification.notificationId.in(ids))
                .execute();
        em.flush();
        em.clear();
        return result;
    }
}
