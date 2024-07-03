package org.nmfw.foodietree.domain.product.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nmfw.foodietree.domain.product.dto.response.ProductDto;
import org.nmfw.foodietree.domain.product.dto.response.TotalInfoDto;
import org.nmfw.foodietree.domain.product.entity.Product;

import java.util.List;

@Mapper
public interface ProductMainPageMapper {

    List<ProductDto> findAll();

    List<TotalInfoDto> findCategoryByFood(String customerId);

    List<TotalInfoDto> findCategoryByArea(String customerId);





    ProductDto findOne (String productId);

    boolean save(ProductDto productDto);

    List<String> categoryByFoodList(@Param("category") List<String> category);

    List<String> categoryByAreaList(@Param("Area") List<String> preferenceAreas);

    ProductDto product();


}
