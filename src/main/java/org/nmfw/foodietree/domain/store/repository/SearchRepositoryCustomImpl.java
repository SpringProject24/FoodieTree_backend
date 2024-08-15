package org.nmfw.foodietree.domain.store.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.product.entity.QProduct;
import org.nmfw.foodietree.domain.reservation.entity.QReservation;
import org.nmfw.foodietree.domain.store.dto.resp.SearchedStoreListDto;
import org.nmfw.foodietree.domain.store.entity.QStore;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.querydsl.jpa.JPAExpressions.select;
import static org.nmfw.foodietree.domain.product.entity.QProduct.product;
import static org.nmfw.foodietree.domain.reservation.entity.QReservation.reservation;
import static org.nmfw.foodietree.domain.store.entity.QStore.store;

@Repository
@RequiredArgsConstructor
@Slf4j
public class SearchRepositoryCustomImpl implements SearchRepositoryCustom {
    private final JPAQueryFactory factory;

    @Override
    public Page<SearchedStoreListDto> findStores(Pageable pageable, String keyword) {
        QReservation r = reservation;
        QProduct p = product;
        QStore s = store;

        NumberExpression<Integer> currProductCnt = new CaseBuilder()
                .when(r.reservationTime.isNull().and(p.pickupTime.gt(LocalDateTime.now())))
                .then(1)
                .otherwise(0).sum();
        Expression<Integer> cnt = ExpressionUtils.as(currProductCnt, "currProductCnt");
        BooleanExpression expression = s.storeName.contains(keyword).or(s.address.contains(keyword));

        List<SearchedStoreListDto> result = factory
                .select(store, cnt)
                .from(product)
                .leftJoin(reservation).on(p.productId.eq(r.productId))
                .leftJoin(store).on(p.storeId.eq(s.storeId))
                .where(expression)
                .groupBy(p.storeId)
                .having(store.isNotNull())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream().map(t -> SearchedStoreListDto.fromEntity(t.get(store), t.get(cnt)))
                .collect(Collectors.toList());

        List<Store> fetch = factory
                .select(store)
                .from(product)
                .leftJoin(reservation).on(p.productId.eq(r.productId))
                .leftJoin(store).on(p.storeId.eq(s.storeId))
                .where(expression)
                .groupBy(p.storeId)
                .having(store.isNotNull())
                .fetch();

        return new PageImpl<>(result, pageable, fetch.size());
    }
}
