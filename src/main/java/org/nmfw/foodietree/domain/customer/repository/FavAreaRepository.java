package org.nmfw.foodietree.domain.customer.repository;

import org.nmfw.foodietree.domain.customer.entity.FavArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface FavAreaRepository extends JpaRepository<FavArea, Long> {
    void deleteByCustomerIdAndPreferredAreaAndAlias(String customerId, String preferredArea, String alias);

    List<FavArea> findByCustomerId(String customerId);
}
