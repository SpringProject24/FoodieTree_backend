package org.nmfw.foodietree.domain.customer.repository;

import org.nmfw.foodietree.domain.customer.dto.resp.PreferredFoodDto;
import org.nmfw.foodietree.domain.customer.entity.FavFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavFoodRepository
        extends JpaRepository<FavFood, Long>, FavFoodRepositoryCustom {

    // 사용자가 저장한 FavFood 조회
    List<FavFood> findAllByCustomerId(String customerId);

    void deleteByCustomerIdAndPreferredFood(String customerId, String preferredFood);


}
