package org.nmfw.foodietree.domain.customer.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListDto;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.nmfw.foodietree.domain.store.entity.QStore.store;

@Repository
@RequiredArgsConstructor
public class FavFoodRepositoryCustomImpl implements FavFoodRepositoryCustom {

    private final JPAQueryFactory factory;

    @Override
    public List<StoreListDto> findCategoryByFood(List<StoreCategory> preferredFood) {
        return factory
                .select(Projections.bean(StoreListDto.class,
                        store.storeId,
                        store.storeName,
                        store.category.stringValue().as("category"),
                        store.address,
                        store.price,
                        store.storeImg,
                        store.productCnt,
                        store.openAt,
                        store.closedAt,
                        store.limitTime,
                        store.emailVerified,
                        store.productImg
                ))
                .from(store)
                .where(store.category.in(preferredFood))
                .fetch();
    }
}
