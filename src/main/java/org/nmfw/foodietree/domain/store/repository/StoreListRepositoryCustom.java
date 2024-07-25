package org.nmfw.foodietree.domain.store.repository;

import org.nmfw.foodietree.domain.store.entity.Store;

import java.util.List;

public interface StoreListRepositoryCustom {

    List<Store> findStoresByCategory(String category);

}
