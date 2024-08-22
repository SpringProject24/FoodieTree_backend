package org.nmfw.foodietree.domain.review.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.entity.QCustomer;
import org.nmfw.foodietree.domain.product.entity.QProduct;
import org.nmfw.foodietree.domain.reservation.entity.QReservation;
import org.nmfw.foodietree.domain.reservation.entity.Reservation;
import org.nmfw.foodietree.domain.review.dto.res.MyReviewDto;
import org.nmfw.foodietree.domain.review.entity.Hashtag;
import org.nmfw.foodietree.domain.review.entity.QReview;
import org.nmfw.foodietree.domain.review.entity.QReviewHashtag;
import org.nmfw.foodietree.domain.store.entity.QStore;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ReviewRepositoryCustomImpl  implements ReviewRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager em;

    @Override
    public boolean existByReservationId(Long reservationId) {
        QReview review = QReview.review;

        // 조건 생성: reservationId가 null이 아닌 경우 해당 reservationId와 일치하는 리뷰가 있는지 확인
        // BooleanBuilder를 사용하여 조건을 동적으로 생성
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(review.reservationId.eq(reservationId));

        // 쿼리 실행
        long count = jpaQueryFactory
                .selectFrom(review)
                .where(builder)
                .fetchCount();

        return count > 0;
    }

    @Override
    public boolean existsByReservationId(Long reservationId) {
        QReview review = QReview.review;

        long count = jpaQueryFactory
                .selectFrom(review)
                .where(review.reservationId.eq(reservationId))
                .fetchCount();

        return count > 0;
    }

    @Override
    public List<MyReviewDto> findEnableReviewsByCustomerId(String customerId) {

        QReservation reservation = QReservation.reservation;
        QProduct product = QProduct.product;
        QStore store = QStore.store;
        QCustomer customer = QCustomer.customer;
        QReview review = QReview.review;

        // 구매, 픽업 완료한 건인지 판단

        return jpaQueryFactory
                .select(Projections.bean(
                        MyReviewDto.class,
                        reservation.reservationId.as("reservationId"),
                        reservation.customerId.as("customerId"),
                        customer.profileImage.as("profileImage"),
                        store.storeId.as("storeId"),
                        store.storeName.as("storeName"),
                        store.storeImg.as("storeImg"),
                        store.category.as("category"),
                        store.address.as("address"),
                        store.price.as("price"),
                        reservation.pickedUpAt.as("pickedUpAt"),
                        review.reviewId.as("reviewId"),
                        review.reviewScore.as("reviewScore"),
                        review.reviewImg.as("reviewImg"),
                        review.reviewContent.as("reviewContent")
//                        ExpressionUtils.as(JPAExpressions.select(reviewHashtag.hashtag)
//                                        .from(reviewHashtag)
//                                        .where(reviewHashtag.review.eq(review)),
//                                "hashtags")
                ))
                .from(reservation)
                .join(customer).on(reservation.customerId.eq(customer.customerId))
                .join(product).on(reservation.productId.eq(product.productId))
                .join(store).on(product.storeId.eq(store.storeId)) // storeId를 사용한 Store와의 조인
                .leftJoin(review).on(review.reservationId.eq(reservation.reservationId))
                .where(reservation.customerId.eq(customerId)
                        .and(reservation.cancelReservationAt.isNull()) // 취소되지 않은 예약
                        .and(reservation.pickedUpAt.isNotNull())) // 픽업 완료된 예약
                .fetch();
    }

    // 구매, 픽업 완료한 예약 건인지 예약 아이디로 판단
    @Override
    public boolean isReservationValid(Long reservationId) {
        QReservation reservation = QReservation.reservation;

        Reservation result = jpaQueryFactory
                .selectFrom(reservation)
                .where(reservation.reservationId.eq(reservationId)
                        .and(reservation.cancelReservationAt.isNull()) // 취소가 되지 않은 건
                        .and(reservation.pickedUpAt.isNotNull())) // 픽업이 완료된 건
                .fetchOne();

        // null 값이 아닐때만 true 반환
        return result != null;
    }

    @Override
    public Map<Long, List<Hashtag>> findHashtagsByReviewIds(List<Long> reviewIds) {
        QReviewHashtag reviewHashtag = QReviewHashtag.reviewHashtag;

        return jpaQueryFactory
                .select(reviewHashtag.review.reservationId, reviewHashtag.hashtag)
                .from(reviewHashtag)
                .where(reviewHashtag.review.reservationId.in(reviewIds))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(reviewHashtag.review.reservationId),
                        Collectors.mapping(tuple -> tuple.get(reviewHashtag.hashtag), Collectors.toList())
                ));
    }

}

