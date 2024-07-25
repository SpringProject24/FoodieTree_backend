package org.nmfw.foodietree.domain.store.repository;

import org.nmfw.foodietree.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreListRepository extends JpaRepository<Store, String>, StoreListRepositoryCustom{



}
