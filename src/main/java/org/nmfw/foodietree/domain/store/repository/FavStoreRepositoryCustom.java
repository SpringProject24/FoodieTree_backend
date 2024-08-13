package org.nmfw.foodietree.domain.store.repository;

import org.nmfw.foodietree.domain.customer.entity.FavStore;

import java.util.List;
import org.nmfw.foodietree.domain.store.dto.resp.FavStoreDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListDto;

public interface FavStoreRepositoryCustom {
    List<StoreListDto> findFavStoresByCustomerId(String customerId);
    List<FavStoreDto> findFavStoresByOrders(String customerId);

}
