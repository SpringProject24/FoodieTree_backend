package org.nmfw.foodietree.domain.reservation.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.nmfw.foodietree.domain.product.entity.QProduct;
import org.nmfw.foodietree.domain.reservation.dto.resp.PaymentIdDto;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationDetailDto;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationFoundStoreIdDto;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationStatusDto;
import org.nmfw.foodietree.domain.reservation.entity.QReservation;
import org.nmfw.foodietree.domain.store.dto.resp.StoreReservationDto;
import org.nmfw.foodietree.domain.store.entity.QStore;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.List;

import static org.nmfw.foodietree.domain.customer.entity.QCustomer.customer;
import static org.nmfw.foodietree.domain.product.entity.QProduct.product;
import static org.nmfw.foodietree.domain.reservation.entity.QReservation.reservation;
import static org.nmfw.foodietree.domain.store.entity.QStore.store;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryCustomImpl implements ReservationRepositoryCustom{

    private final JPAQueryFactory factory;

    // 예약 취소
    @Override
    public void cancelReservation(Long reservationId) {
        factory.update(reservation)
                .set(reservation.cancelReservationAt, LocalDateTime.now())
                .where(reservation.reservationId.eq(reservationId))
                .execute();
    }
    // 픽업 완료
    @Override
    public void completePickup(Long reservationId) {
        factory.update(reservation)
                .set(reservation.pickedUpAt, LocalDateTime.now())
                .where(reservation.reservationId.eq(reservationId))
                .execute();
    }
    // customerId로 예약 목록 조회
    @Override
    public List<ReservationDetailDto> findReservationsByCustomerId(String customerId) {
        return factory
                .select(Projections.bean(
                        ReservationDetailDto.class,
                        reservation.reservationId.as("reservationId"),
                        reservation.customerId.as("customerId"),
                        reservation.productId.as("productId"),
                        reservation.reservationTime.as("reservationTime"),
                        reservation.cancelReservationAt.as("cancelReservationAt"),
                        reservation.pickedUpAt.as("pickedUpAt"),
                        product.storeId.as("storeId"),
                        product.pickupTime.as("pickupTime"),
                        store.storeName.as("storeName"),
                        store.category.as("category"),
                        store.address.as("address"),
                        store.price.as("price"),
                        store.storeImg.as("storeImg"),
                        customer.nickname.as("nickname")))
                .from(reservation)
                .join(product).on(reservation.productId.eq(product.productId))
                .join(store).on(product.storeId.eq(store.storeId))
                .join(customer).on(reservation.customerId.eq(customer.customerId))
                .where(reservation.customerId.eq(customerId))
//                .orderBy(product.pickupEndTime.desc())
                .orderBy(product.pickupTime.desc())
                .fetch();
    }

    // 예약 시간 조회
    @Override
    public ReservationStatusDto findTimeByReservationId(Long reservationId) {
        return factory
                .select(Projections.constructor(
                        ReservationStatusDto.class,
                        reservation.reservationTime,
                        reservation.cancelReservationAt,
                        reservation.pickedUpAt,
                        product.pickupTime
//                        product.pickupStartTime,
//                        product.pickupEndTime
                ))
                .from(reservation)
                .join(product).on(reservation.productId.eq(product.productId))
                .where(reservation.reservationId.eq(reservationId))
                .fetchOne();
    }

    // 예약 상세 조회
    @Override
    public ReservationDetailDto findReservationByReservationId(Long reservationId) {
        return factory
                .select(Projections.bean(
                        ReservationDetailDto.class,
                        reservation.reservationId.as("reservationId"),
                        reservation.customerId.as("customerId"),
                        reservation.productId.as("productId"),
                        reservation.reservationTime.as("reservationTime"),
                        reservation.cancelReservationAt.as("cancelReservationAt"),
                        reservation.pickedUpAt.as("pickedUpAt"),
                        product.storeId.as("storeId"),
                        product.pickupTime.as("pickupTime"),
                        store.storeName.as("storeName"),
                        store.category.as("category"),
                        store.address.as("address"),
                        store.price.as("price"),
                        store.storeImg.as("storeImg"),
                        customer.nickname.as("nickname")))
                .from(reservation)
                .join(product).on(reservation.productId.eq(product.productId))
                .join(store).on(product.storeId.eq(store.storeId))
                .join(customer).on(reservation.customerId.eq(customer.customerId))
                .where(reservation.reservationId.eq(reservationId))
                .fetchOne();
    }

    // 예약 가능 제품 조회
    @Override
    public List<ReservationFoundStoreIdDto> findByStoreIdLimit(String storeId, int cnt) {
        QReservation r = reservation;
        QProduct p = product;

        BooleanExpression condition = p.storeId.eq(storeId)
            .and(p.pickupTime.gt(LocalDateTime.now()))
            .and(r.reservationTime.isNull()
				.or(
					r.paymentId.isNull()
						.and(r.reservationTime.lt(LocalDateTime.now().minusMinutes(5)))
				)
			);

        return factory
                .select(Projections.constructor(
                        ReservationFoundStoreIdDto.class,
                        product.storeId,
                        product.productId))
                .from(product)
                .leftJoin(reservation).on(p.productId.eq(r.productId))
                .where(condition)
                .limit(cnt)
                .fetch();
    }

    // 가게 예약리스트 조회
    @Override
    public List<StoreReservationDto> findReservations(String storeId) {
        return factory
                .select(Projections.bean(
                                StoreReservationDto.class,
                                customer.customerId.as("customerId"),
                                customer.profileImage.as("profileImage"),
                                customer.nickname.as("nickname"),
                                customer.customerPhoneNumber.as("customerPhoneNumber"),
                                product.productId.as("productId"),
                                reservation.reservationId.as("reservationId"),
                                reservation.reservationTime.as("reservationTime"),
                                reservation.cancelReservationAt.as("cancelReservationAt"),
                                reservation.pickedUpAt.as("pickedUpAt"),
                                product.pickupTime.as("pickupTime"),
//                    product.pickupStartTime.as("pickupStartTime"),
//                    product.pickupEndTime.as("pickupEndTime"),
                                product.productUploadDate.as("productUploadDate"),
                                store.price.as("price"),
                                store.openAt.as("openAt"),
                                store.closedAt.as("closedAt"))
                )
                .from(reservation)
                .join(product).on(reservation.productId.eq(product.productId))
                .join(store).on(product.storeId.eq(store.storeId))
                .join(customer).on(reservation.customerId.eq(customer.customerId))
                .where(store.storeId.eq(storeId))
//                .orderBy(product.pickupEndTime.desc())
                .orderBy(product.pickupTime.desc())
                .fetch();
    }

    @Override
    public List<PaymentIdDto> findByPaymentIdForPrice(String paymentId) {
        return factory
                .select(Projections.constructor(PaymentIdDto.class,
                        reservation.paymentId,
                        store.price
                ))
                .from(reservation)
                .innerJoin(product).on(reservation.productId.eq(product.productId))
                .innerJoin(store).on(product.storeId.eq(store.storeId))
                .where(reservation.paymentId.eq(paymentId))
                .fetch();
    }
}
