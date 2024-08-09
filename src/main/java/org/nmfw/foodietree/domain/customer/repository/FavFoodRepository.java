package org.nmfw.foodietree.domain.customer.repository;

import org.nmfw.foodietree.domain.customer.entity.FavFood;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavFoodRepository extends JpaRepository<FavFood, Long> {

    void deleteByCustomerIdAndPreferredFood(String customerId, String preferredFood);
}
