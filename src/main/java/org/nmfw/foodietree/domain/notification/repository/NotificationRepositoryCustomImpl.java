package org.nmfw.foodietree.domain.notification.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.nmfw.foodietree.domain.notification.dto.res.MessageDto;
import org.nmfw.foodietree.domain.notification.entity.QNotification;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static org.nmfw.foodietree.domain.notification.entity.QNotification.*;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom{

    private final JPAQueryFactory factory;

    @Override
    public List<MessageDto> findAllByReceiverId(String receiverId) {
        return factory.select(Projections.constructor(MessageDto.class))
                .from(notification)
                .where(
                    notification.receiverId.eq(receiverId)
                    .and(notification.createdAt.lt(LocalDateTime.now().minusDays(3)))
                    .or(notification.isRead.isNull())
                ).fetch();
    }
}
