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
import org.nmfw.foodietree.domain.reservation.entity.QReservationSubSelect;
import org.nmfw.foodietree.domain.store.dto.resp.SearchedStoreListDto;
import org.nmfw.foodietree.domain.store.entity.QStore;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.nmfw.foodietree.global.utils.QueryDslUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.querydsl.jpa.JPAExpressions.select;
import static org.nmfw.foodietree.domain.product.entity.QProduct.product;
import static org.nmfw.foodietree.domain.reservation.entity.QReservationSubSelect.reservationSubSelect;
import static org.nmfw.foodietree.domain.store.entity.QStore.store;

@Repository
@RequiredArgsConstructor
@Slf4j
public class SearchRepositoryCustomImpl implements SearchRepositoryCustom {
    private final JPAQueryFactory factory;

    @Override
    public Page<SearchedStoreListDto> findStores(Pageable pageable, String keyword) {
        QReservationSubSelect r = reservationSubSelect;
        QProduct p = product;
        QStore s = store;
        Expression<Integer> cnt = QueryDslUtils.getCurrProductCntExpression(p, r);
        BooleanExpression expression = s.storeName.contains(keyword).or(s.address.contains(keyword));

        List<SearchedStoreListDto> result = factory
                .select(store, cnt, p.productId)
                .from(store)
                .leftJoin(product).on(s.storeId.eq(p.storeId))
                .leftJoin(reservationSubSelect).on(p.productId.eq(r.productId))
                .where(expression.and(r.rowNum.isNull().or(r.rowNum.eq(1L))))
                .groupBy(s.storeId)
                .having(store.isNotNull())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream().map(t -> SearchedStoreListDto.fromEntity(t.get(store), t.get(cnt), t.get(p.productId)))
                .collect(Collectors.toList());

        List<Store> fetch = factory
                .select(store)
                .from(store)
                .leftJoin(product).on(s.storeId.eq(p.storeId))
                .leftJoin(reservationSubSelect).on(p.productId.eq(r.productId))
                .where(expression)
                .groupBy(s.storeId)
                .having(store.isNotNull())
                .fetch();

        return new PageImpl<>(result, pageable, fetch.size());
    }
}
