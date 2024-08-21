package org.nmfw.foodietree.domain.customer.repository;

import org.apache.ibatis.annotations.Param;
import org.nmfw.foodietree.domain.customer.dto.resp.PreferredFoodDto;
import org.nmfw.foodietree.domain.product.dto.response.ProductDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListDto;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;

import java.util.List;

public interface FavFoodRepositoryCustom {

    List<StoreListDto> findCategoryByFood(List<StoreCategory> preferredFood);

    // 최근 리뷰 기준 5개 -> 예약으로 일단 개발
//    List<ProductDto> findByReviews();
}
