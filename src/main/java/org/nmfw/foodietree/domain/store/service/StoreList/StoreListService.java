package org.nmfw.foodietree.domain.store.service.StoreList;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.resp.UpdateAreaDto;
import org.nmfw.foodietree.domain.customer.entity.FavArea;
import org.nmfw.foodietree.domain.customer.repository.FavAreaRepository;
import org.nmfw.foodietree.domain.customer.repository.FavAreaRepositoryCustom;
import org.nmfw.foodietree.domain.customer.service.FavAreaService;
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
        return storeListRepositoryCustom.findAllProductsStoreId();
    }

    // 해당 카테고리 별 리스트 출력
    public List<StoreListDto> getStoresByCategory(StoreCategory category) {
        return storeListRepositoryCustom.findStoresByCategory(category);
    }

    // 지역별 가게 리스트 출력
    public List<StoreListDto> getStoresByAddress(String address) {
        List<Store> stores = storeListRepository.findByAddressContaining(address);
        // DTO 변환을 fromEntity 메서드를 사용하여 직접 구현
        return stores.stream()
                .map(StoreListDto::fromEntity)
                .collect(Collectors.toList());
    }
}

