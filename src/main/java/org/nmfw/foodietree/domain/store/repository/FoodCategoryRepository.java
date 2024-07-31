package org.nmfw.foodietree.domain.store.repository;

import org.nmfw.foodietree.domain.store.entity.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodCategoryRepository extends JpaRepository<FoodCategory, Long> {
}
