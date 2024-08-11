package org.nmfw.foodietree.domain.store.repository;

import org.nmfw.foodietree.domain.customer.entity.FavStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavStoreRepository extends JpaRepository<FavStore, Long>
        , FavStoreRepositoryCustom {
}
