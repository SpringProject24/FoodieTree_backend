package org.nmfw.foodietree.domain.product.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMainPageMapper {

    List<String> findAll();

    List<String> categoryByFoodList(@Param("category") List<String> category);

    List<String> categoryByAreaList(@Param("Area") List<String> preferenceAreas);


}
