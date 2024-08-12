package org.nmfw.foodietree.domain.store.service.StoreList;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.resp.UpdateAreaDto;
import org.nmfw.foodietree.domain.customer.entity.FavArea;
import org.nmfw.foodietree.domain.customer.repository.FavAreaRepository;
import org.nmfw.foodietree.domain.customer.repository.FavAreaRepositoryCustom;
import org.nmfw.foodietree.domain.customer.service.FavAreaService;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListByEndTimeDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListDto;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;
import org.nmfw.foodietree.domain.store.repository.StoreListRepository;
import org.nmfw.foodietree.domain.store.repository.StoreListRepositoryCustom;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StoreListService {
    private final StoreListRepository storeListRepository;
    private final StoreListRepositoryCustom storeListRepositoryCustom;
    private final FavAreaRepositoryCustom favAreaRepositoryCustom;
    // 모든 가게 리스트 출력
    public List<StoreListDto> getAllStores(String customerId) {
        List<UpdateAreaDto> favouriteAreas = favAreaRepositoryCustom.findFavAreaByCustomerId(customerId);
        return storeListRepositoryCustom.findAllStoresByFavArea(favouriteAreas);
    }

    // 해당 카테고리 별 리스트 출력
    public List<StoreListDto> getStoresByCategory(StoreCategory category) {
        return storeListRepositoryCustom.findStoresByCategory(category);
    }

    // 비회원 메인페이지 가게 리스트 출력
    public List<StoreListDto> getStoresByProductCnt() {
        return storeListRepositoryCustom.findAllStoresByProductCnt();
    }

    //비회원 메인페이지 마감임박 리스트 출력
    public List<StoreListByEndTimeDto> getStoresByProductEndTime() {
        return storeListRepositoryCustom.findAllStoresByProductEndTime();
    }

}

