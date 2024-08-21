package org.nmfw.foodietree.domain.store.repository;

import org.nmfw.foodietree.domain.customer.dto.resp.UpdateAreaDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListByEndTimeDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListCo2Dto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListDto;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;

import java.util.List;

public interface StoreListRepositoryCustom {

    List<StoreListDto> findStoresByCategory(StoreCategory category);

    List<StoreListDto> findAllStoresByFavArea(List<UpdateAreaDto> favouriteAreas);

    List<StoreListCo2Dto> findAllStoresByProductCnt();

    List<StoreListByEndTimeDto> findAllStoresByProductEndTime();

    List<StoreListDto> findAllProductsStoreId();

    List<StoreListDto> findAllStoresRandom();

    List<StoreListDto> findCategoryByFood(List<StoreCategory> preferredFood);
}
