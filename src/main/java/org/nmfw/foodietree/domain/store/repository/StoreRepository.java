package org.nmfw.foodietree.domain.store.repository;

import org.nmfw.foodietree.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByStoreId(String storeId);
}
