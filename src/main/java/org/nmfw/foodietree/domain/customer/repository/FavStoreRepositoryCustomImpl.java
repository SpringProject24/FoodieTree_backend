package org.nmfw.foodietree.domain.customer.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.nmfw.foodietree.domain.product.entity.QProduct;
import org.nmfw.foodietree.domain.reservation.entity.QReservation;
import org.nmfw.foodietree.domain.reservation.entity.QReservationSubSelect;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListDto;
import org.nmfw.foodietree.domain.store.entity.QStore;
import org.nmfw.foodietree.global.utils.QueryDslUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static org.nmfw.foodietree.domain.customer.entity.QFavStore.favStore;
import static org.nmfw.foodietree.domain.product.entity.QProduct.product;
import static org.nmfw.foodietree.domain.reservation.entity.QReservation.reservation;
import static org.nmfw.foodietree.domain.reservation.entity.QReservationSubSelect.reservationSubSelect;
import static org.nmfw.foodietree.domain.store.entity.QStore.store;

@Repository
@RequiredArgsConstructor
public class FavStoreRepositoryCustomImpl implements FavStoreRepositoryCustom {

    private final JPAQueryFactory factory;

    @Override
    public List<StoreListDto> findFavStoresByCustomerId(String customerId, String type) {
        QReservationSubSelect r = reservationSubSelect;
        QProduct p = product;
        QStore s = store;
        JPQLQuery<String> inTarget = null;

        if (type.equals("favStore")) {
            inTarget = select(favStore.storeId)
                    .from(favStore)
                    .innerJoin(store).on(favStore.storeId.eq(store.storeId))
                    .where(favStore.customerId.eq(customerId));
        } else if (type.equals("orders_3")) {
            inTarget = select(product.storeId)
                    .from(reservationSubSelect)
                    .innerJoin(product).on(r.productId.eq(product.productId))
                    .where(r.customerId.eq(customerId).and(r.pickedUpAt.isNotNull()))
                    .groupBy(product.storeId)
                    .having(product.storeId.count().goe(3));
        }

        Expression<Integer> cnt = QueryDslUtils.getCurrProductCntExpression(p, r);

        return factory
                .select(store, cnt)
                .from(store)
                .leftJoin(product).on(s.storeId.eq(p.storeId))
                .leftJoin(reservationSubSelect).on(p.productId.eq(r.productId))
                .where(r.rowNum.isNull().or(r.rowNum.eq(1L)))
                .groupBy(s.storeId)
                .having(s.storeId.in(inTarget))
                .fetch()
                .stream()
                .map(tuple -> StoreListDto.fromEntity(tuple.get(store), tuple.get(cnt)))
                .collect(Collectors.toList());
    }
}
