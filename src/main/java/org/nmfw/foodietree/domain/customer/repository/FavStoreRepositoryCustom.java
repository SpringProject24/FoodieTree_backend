package org.nmfw.foodietree.domain.customer.repository;

import java.util.List;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListDto;

public interface FavStoreRepositoryCustom {
    List<StoreListDto> findFavStoresByCustomerId(String customerId, String type);
}
