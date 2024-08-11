package org.nmfw.foodietree.domain.customer.repository;

import org.apache.ibatis.annotations.Param;
import org.nmfw.foodietree.domain.customer.dto.resp.PreferredFoodDto;
import org.nmfw.foodietree.domain.product.dto.response.ProductDto;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;

import java.util.List;

public interface FavFoodRepositoryCustom {

    List<ProductDto> findCategoryByFood(List<StoreCategory> preferredFood);
}
