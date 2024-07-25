package org.nmfw.foodietree.domain.store.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalTime;

@Mapper
public interface StoreMyPageEditMapper {

    boolean updateStoreInfo(@Param("storeId") String storeId, @Param("type") String type, @Param("value") String value);

    boolean updatePrice(@Param("storeId") String storeId, @Param("price") int price);

    boolean updateOpenAt(@Param("storeId") String storeId, @Param("openAt") LocalTime openAt);

    boolean updateClosedAt(@Param("storeId") String storeId, @Param("closedAt") LocalTime closedAt);

    boolean updateProductCnt(@Param("storeId") String storeId, @Param("productCnt") int productCnt);
}
