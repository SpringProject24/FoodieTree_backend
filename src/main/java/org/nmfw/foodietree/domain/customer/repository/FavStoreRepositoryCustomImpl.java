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
import org.nmfw.foodietree.domain.store.dto.resp.StoreListDto;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static org.nmfw.foodietree.domain.customer.entity.QFavStore.favStore;
import static org.nmfw.foodietree.domain.product.entity.QProduct.product;
import static org.nmfw.foodietree.domain.reservation.entity.QReservation.reservation;
import static org.nmfw.foodietree.domain.store.entity.QStore.store;

@Repository
@RequiredArgsConstructor
public class FavStoreRepositoryCustomImpl implements FavStoreRepositoryCustom {

	private final JPAQueryFactory factory;

	@Override
	public List<StoreListDto> findFavStoresByCustomerId(String customerId, String type) {

		JPQLQuery<String> inTarget = null;

		if (type.equals("favStore")) {
			inTarget = select(favStore.storeId)
				.from(favStore)
				.innerJoin(store).on(favStore.storeId.eq(store.storeId))
				.where(favStore.customerId.eq(customerId));
		} else if (type.equals("orders_3")) {
			inTarget = select(product.storeId)
				.from(reservation)
				.innerJoin(product).on(reservation.productId.eq(product.productId))
				.where(reservation.customerId.eq(customerId).and(reservation.pickedUpAt.isNotNull()))
				.groupBy(product.storeId)
				.having(product.storeId.count().goe(3));
		}

		NumberExpression<Integer> currProductCnt = new CaseBuilder()
			.when(reservation.reservationTime.isNull().and(product.pickupTime.gt(LocalDateTime.now())))
			.then(1)
			.otherwise(0).sum();

		Expression<Integer> cnt = ExpressionUtils.as(currProductCnt, "currProductCnt");

		return factory
			.select(store, cnt)
			.from(product)
			.leftJoin(reservation).on(product.productId.eq(reservation.productId))
			.leftJoin(store).on(product.storeId.eq(store.storeId))
			.groupBy(product.storeId)
			.having(product.storeId.in(inTarget))
			.fetch()
			.stream()
			.map(tuple -> StoreListDto.fromEntity(tuple.get(store), tuple.get(cnt)))
			.collect(Collectors.toList());
	}
}
