package org.nmfw.foodietree.domain.customer.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.product.dto.response.ProductDto;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.nmfw.foodietree.domain.product.entity.QProduct.product;
import static org.nmfw.foodietree.domain.reservation.entity.QReservation.reservation;
import static org.nmfw.foodietree.domain.store.entity.QStore.store;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FavFoodRepositoryCustomImpl implements FavFoodRepositoryCustom {

    private final JPAQueryFactory factory;

    @Override
    public List<ProductDto> findCategoryByFood(List<StoreCategory> preferredFood) {
        return selectProductDto()
                .where(store.category.in(preferredFood))
                .groupBy(store.storeName)
                .fetch();
    }

    @Override // review 테이블로 변경 필요
    public List<ProductDto> findByReviews() {

        // 1. 최근 5개의 distinct storeId 조회
        List<String> recentStoreIds = factory
                .select(store.storeId)
                .from(reservation)
                .join(product).on(reservation.productId.eq(product.productId))  // reservation에서 product와 조인
                .join(store).on(product.storeId.eq(store.storeId))
                .groupBy(store.storeId)
                .orderBy(reservation.reservationId.desc()) // 최근 예약 기준 정렬
                .limit(5)
                .fetch();
        log.debug("storeIds {}", recentStoreIds);
        // 2. 해당 productId에 대한 ProductDto 리스트 조회
        return selectProductDto()
                .where(product.store.storeId.in(recentStoreIds))
                .fetch();
    }

    // ProductDto 조회 쿼리 분리
    public JPAQuery<ProductDto> selectProductDto() {
        return factory
                .select(Projections.bean(ProductDto.class,
                        store.storeId,
                        product.productId.stringValue().as("productId"),
                        product.pickupTime,
                        product.productUploadDate,
                        store.storeImg,
                        store.storeName,
                        store.category.stringValue().as("category"),
                        store.address,
                        store.price,
                        store.productCnt,
                        store.productImg.as("proImg")
                ))
                .from(product)
                .join(product.store, store);
    }
}
