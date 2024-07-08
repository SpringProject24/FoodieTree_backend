package org.nmfw.foodietree.domain.store.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalTime;

@Mapper
public interface StoreMyPageEditMapper {

    void updateStoreInfo(@Param("storeId") String storeId, @Param("type") String type, @Param("value") String value);

    void updatePrice(@Param("storeId") String storeId, @Param("price") int price);

    void updateOpenAt(@Param("storeId") String storeId, @Param("openAt") LocalTime openAt);

    void updateClosedAt(@Param("storeId") String storeId, @Param("closedAt") LocalTime closedAt);

    void updateProductCnt(@Param("storeId") String storeId, @Param("productCnt") int productCnt);
}
