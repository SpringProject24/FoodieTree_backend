package org.nmfw.foodietree.domain.store.repository;

import org.nmfw.foodietree.domain.customer.entity.FavStore;

import java.util.List;

public interface FavStoreRepositoryCustom {
    List<FavStore> findFavStoresOrReservations(String customerId);
}
