package org.nmfw.foodietree.domain.reservation.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.nmfw.foodietree.domain.product.entity.QProduct;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationDetailDto;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationFoundStoreIdDto;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationStatusDto;
import org.nmfw.foodietree.domain.reservation.entity.QReservation;
import org.nmfw.foodietree.domain.store.dto.resp.StoreReservationDto;
import org.nmfw.foodietree.domain.store.entity.QStore;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
                .select(Projections.constructor(
                        ReservationDetailDto.class,
                        reservation.reservationId,
                        reservation.customerId,
                        reservation.productId,
                        reservation.reservationTime,
                        reservation.cancelReservationAt,
                        reservation.pickedUpAt,
                        product.storeId,
                        product.pickupStartTime,
                        product.pickupEndTime,
                        store.storeName,
                        store.category,
                        store.address,
                        store.price,
                        store.storeImg,
                        customer.nickname))
                .from(reservation)
                .join(product).on(reservation.productId.eq(product.productId))
                .join(store).on(product.storeId.eq(store.storeId))
                .join(customer).on(reservation.customerId.eq(customer.customerId))
                .where(reservation.customerId.eq(customerId))
                .orderBy(product.pickupEndTime.desc())
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
                        product.pickupStartTime,
                        product.pickupEndTime
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
                .select(Projections.constructor(
                        ReservationDetailDto.class,
                        reservation.reservationId,
                        reservation.productId,
                        reservation.customerId,
                        reservation.reservationTime,
                        reservation.cancelReservationAt,
                        reservation.pickedUpAt,
                        product.storeId,
                        product.pickupStartTime,
                        product.pickupEndTime,
                        store.storeName,
                        store.category,
                        store.address,
                        store.price,
                        store.storeImg,
                        customer.nickname,
                        customer.profileImage))
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

        // TIMESTAMPDIFF(SECOND, NOW(), pickup_time)
        NumberExpression<Long> secLeft = Expressions.numberTemplate(Long.class,
                "TIMESTAMPDIFF(SECOND, {0}, {1})", Expressions.currentTimestamp(), product.pickupEndTime);

        return factory
                .select(Projections.constructor(
                        ReservationFoundStoreIdDto.class,
                        product.storeId,
                        product.productId,
                        secLeft))
                .from(product)
                .where(product.storeId.eq(storeId)
                        .and(product.pickupEndTime.gt(LocalTime.now()))
                        .and(product.cancelByStore.isNull()))
                .limit(cnt)
                .fetch();
    }

    // 가게 예약리스트 조회
    @Override
    public List<StoreReservationDto> findReservations(String storeId) {
        return factory
                .select(Projections.constructor(
                        StoreReservationDto.class,
                        customer.customerId,
                        customer.profileImage,
                        customer.nickname,
                        customer.customerPhoneNumber,
                        product.productId,
                        reservation.reservationId,
                        reservation.reservationTime,
                        reservation.cancelReservationAt,
                        reservation.pickedUpAt,
                        product.pickupStartTime,
                        product.pickupEndTime,
                        product.productUploadDate,
                        store.price,
                        store.openAt,
                        store.closedAt))
                .from(reservation)
                .join(product).on(reservation.productId.eq(product.productId))
                .join(store).on(product.storeId.eq(store.storeId))
                .join(customer).on(reservation.customerId.eq(customer.customerId))
                .where(store.storeId.eq(storeId))
                .orderBy(product.pickupEndTime.desc())
                .fetch();
    }
}
