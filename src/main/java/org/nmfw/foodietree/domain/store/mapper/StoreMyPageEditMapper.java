package org.nmfw.foodietree.domain.store.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreMyPageEditMapper {

    void updateStoreInfo();

    void updatePrice();

    void updateOpenAt();

    void updateClosedAt();

    void updateProductCnt();
}
