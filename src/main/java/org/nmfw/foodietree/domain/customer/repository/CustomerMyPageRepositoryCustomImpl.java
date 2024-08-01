package org.nmfw.foodietree.domain.customer.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.resp.CustomerMyPageDto;
import org.nmfw.foodietree.domain.customer.dto.resp.PreferredFoodDto;
import org.nmfw.foodietree.domain.customer.dto.resp.CustomerFavStoreDto;
import org.nmfw.foodietree.domain.customer.entity.*;
import org.nmfw.foodietree.domain.product.entity.QProduct;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationDetailDto;
import org.nmfw.foodietree.domain.store.entity.QFoodCategory;
import org.nmfw.foodietree.domain.store.entity.QStore;
import org.nmfw.foodietree.domain.reservation.entity.QReservation;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomerMyPageRepositoryCustomImpl implements CustomerMyPageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public CustomerMyPageDto findCustomerDetails(String customerId) {
        QCustomer customer = QCustomer.customer;

        Customer customerEntity = queryFactory.selectFrom(customer)
                .where(customer.customerId.eq(customerId))
                .fetchOne();

        List<String> preferenceAreas = findPreferenceAreas(customerId);
        List<PreferredFoodDto> preferenceFoods = findPreferenceFoods(customerId);
        List<CustomerFavStoreDto> favStores = findFavStores(customerId);

        return CustomerMyPageDto.builder()
                .customerId(customerEntity.getCustomerId())
                .nickname(customerEntity.getNickname())
                .profileImage(customerEntity.getProfileImage())
                .customerPhoneNumber(customerEntity.getCustomerPhoneNumber())
                .preferredFood(preferenceFoods)
                .preferredArea(preferenceAreas)
                .favStore(favStores)
                .build();
    }


    @Override
    public List<String> findPreferenceAreas(String customerId) {
        QFavArea favArea = QFavArea.favArea;
        return queryFactory.select(favArea.preferredArea)
                .from(favArea)
                .where(favArea.customerId.eq(customerId))
                .fetch();
    }

    @Override
    public List<PreferredFoodDto> findPreferenceFoods(String customerId) {
        QFavFood favFood = QFavFood.favFood;
        QFoodCategory foodCategory = QFoodCategory.foodCategory;
        return queryFactory.select(
                        Projections.constructor(PreferredFoodDto.class,
                                foodCategory.foodImage,
                                favFood.preferredFood))
                .from(favFood)
                .join(foodCategory).on(favFood.preferredFood.eq(foodCategory.foodName))
                .where(favFood.customerId.eq(customerId))
                .fetch();
    }

    @Override
    public List<CustomerFavStoreDto> findFavStores(String customerId) {
        QFavStore favStore = QFavStore.favStore;
        QStore store = QStore.store;
        return queryFactory.select(
                        Projections.constructor(CustomerFavStoreDto.class,
                                favStore.customerId,
                                favStore.storeId,
                                store.storeName,
                                store.storeImg))
                .from(favStore)
                .join(store).on(favStore.storeId.eq(store.storeId))
                .where(favStore.customerId.eq(customerId))
                .fetch();
    }

    @Override
    public List<ReservationDetailDto> findReservationsByCustomerId(String customerId) {
        QReservation reservation = QReservation.reservation;
        QProduct product = QProduct.product;
        QStore store = QStore.store;
        QCustomer customer = QCustomer.customer;

        return queryFactory.select(
                        Projections.constructor(ReservationDetailDto.class,
                                reservation.reservationId,
                                reservation.productId,
                                reservation.customerId,
                                reservation.reservationTime,
                                reservation.cancelReservationAt,
                                reservation.pickedUpAt,
                                product.storeId,
                                product.pickupTime,
                                store.storeName,
                                store.category,
                                store.address,
                                store.price,
                                store.storeImg,
                                customer.nickname,
                                customer.profileImage,
                                reservation.status
                        ))
                .from(reservation)
                .join(product).on(reservation.productId.eq(product.productId))
                .join(store).on(product.storeId.eq(store.storeId))
                .join(customer).on(reservation.customerId.eq(customer.customerId))
                .where(reservation.customerId.eq(customerId))
                .orderBy(product.pickupTime.desc())
                .fetch();
    }
}
