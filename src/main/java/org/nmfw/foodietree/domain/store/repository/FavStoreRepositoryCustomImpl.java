package org.nmfw.foodietree.domain.store.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.nmfw.foodietree.domain.customer.entity.FavStore;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.nmfw.foodietree.domain.customer.entity.QFavStore.favStore;

@Repository
@RequiredArgsConstructor
public class FavStoreRepositoryCustomImpl implements FavStoreRepositoryCustom {
    private final JPAQueryFactory factory;
    @Override
    public List<FavStore> findFavStoresOrReservations(String customerId) {
        factory
                .select(favStore)
                .from()
        return List.of();
    }
}
