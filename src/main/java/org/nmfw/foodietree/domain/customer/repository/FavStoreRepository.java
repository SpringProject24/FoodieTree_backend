package org.nmfw.foodietree.domain.customer.repository;

import org.nmfw.foodietree.domain.customer.entity.FavStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavStoreRepository extends JpaRepository<FavStore, Long> {
}
