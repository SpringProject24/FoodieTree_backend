package org.nmfw.foodietree.domain.review.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.review.entity.QReview;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ReviewRepositoryCustomImpl  implements ReviewRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager em;

    @Override
    public boolean existByReservationId(Long reservationId) {
        QReview review = QReview.review;

        // BooleanBuilder를 사용하여 조건을 동적으로 생성
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(review.reservation.reservationId.eq(reservationId));

        // 쿼리 실행
        long count = jpaQueryFactory
                .selectFrom(review)
                .where(builder)
                .fetchCount();

        return count > 0;
    }

}

