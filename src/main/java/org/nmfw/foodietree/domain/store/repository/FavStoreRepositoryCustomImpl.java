package org.nmfw.foodietree.domain.store.repository;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.nmfw.foodietree.domain.customer.entity.FavStore;
import org.nmfw.foodietree.domain.store.dto.resp.FavStoreDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListDto;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static org.nmfw.foodietree.domain.customer.entity.QFavStore.favStore;
import static org.nmfw.foodietree.domain.store.entity.QStore.store;

@Repository
@RequiredArgsConstructor
public class FavStoreRepositoryCustomImpl implements FavStoreRepositoryCustom {

	private final JPAQueryFactory factory;

	@Override
	public List<StoreListDto> findFavStoresByCustomerId(String customerId) {
		JPQLQuery<String> inTarget = select(favStore.storeId)
			.from(favStore)
			.innerJoin(store).on(favStore.storeId.eq(store.storeId))
			.where(favStore.customerId.eq(customerId));
		return factory
			.select(store, )
			.fetch()
			.stream()
			.map(e -> StoreListDto.fromEntity(e.get(store)))
			.collect(Collectors.toList());
	}

	@Override
	public List<FavStoreDto> findFavStoresByOrders(String customerId) {
		return List.of();
	}
}
