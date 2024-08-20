package org.nmfw.foodietree.domain.store.service.StoreList;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.resp.PreferredFoodDto;
import org.nmfw.foodietree.domain.customer.dto.resp.UpdateAreaDto;
import org.nmfw.foodietree.domain.customer.entity.FavArea;
import org.nmfw.foodietree.domain.customer.entity.FavFood;
import org.nmfw.foodietree.domain.customer.entity.value.PreferredFoodCategory;
import org.nmfw.foodietree.domain.customer.repository.*;
import org.nmfw.foodietree.domain.customer.service.FavAreaService;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListByEndTimeDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListCo2Dto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListDto;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;
import org.nmfw.foodietree.domain.store.repository.StoreListRepository;
import org.nmfw.foodietree.domain.store.repository.StoreListRepositoryCustom;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StoreListService {
    private final StoreListRepository storeListRepository;
    private final StoreListRepositoryCustom storeListRepositoryCustom;
    private final FavAreaRepositoryCustom favAreaRepositoryCustom;
    private final FavStoreRepository favStoreRepository;
    private final FavFoodRepository favFoodRepository;

    // 모든 가게 리스트 출력
    public List<StoreListDto> getAllStores(String customerId) {
        return storeListRepositoryCustom.findAllProductsStoreId();
    }

    // 해당 카테고리 별 리스트 출력
    public List<StoreListDto> getStoresByCategory(StoreCategory category) {
        return storeListRepositoryCustom.findStoresByCategory(category);
    }

    // 비회원 메인페이지 가게 리스트 출력
    public List<StoreListCo2Dto> getStoresByProductCnt() {
        return storeListRepositoryCustom.findAllStoresByProductCnt();
    }

    //비회원 메인페이지 마감임박 리스트 출력
    public List<StoreListByEndTimeDto> getStoresByProductEndTime() {
        return storeListRepositoryCustom.findAllStoresByProductEndTime();
    }

    // 지역별 가게 리스트 출력
    public List<StoreListDto> getStoresByAddress(String address) {
        List<Store> stores = storeListRepository.findByAddressContaining(address);
        // DTO 변환을 fromEntity 메서드를 사용하여 직접 구현
        return stores.stream()
                .map(StoreListDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<StoreListDto> getFavStoresAndOrders3(String customerId) {
        // 찜한 가게 리스트
        List<StoreListDto> favStoresList = favStoreRepository.findFavStoresByCustomerId(
                customerId, "favStore");
        // 3회 이상 주문한 가게 리스트
        List<StoreListDto> orders = favStoreRepository.findFavStoresByCustomerId(
                customerId, "orders_3");
        favStoresList.addAll(orders);
        return favStoresList;
    }

    // 선호 카테고리에 해당하는 가게
    public List<StoreListDto> findStoresByPreferredFood(String customerId, HttpServletRequest request, HttpServletResponse response) {
        List<FavFood> favFoods = favFoodRepository.findAllByCustomerId(customerId);

        List<StoreListDto> storesByFood = new ArrayList<>();
        if (favFoods == null || favFoods.isEmpty()) {
            log.warn("Preferred food list is empty for customerId: {}", customerId);
            // 선호 음식이 없으면 랜덤으로 모든 가게 리스트 조회
            storesByFood = storeListRepositoryCustom.findAllStoresRandom();
        } else {
            List<StoreCategory> categories = favFoods.stream()
                    .map(favFood -> {
                        try {
                            // PreferredFoodCategory를 StoreCategory로 변환
                            PreferredFoodCategory preferredCategory = PreferredFoodCategory.fromKoreanName(favFood.getPreferredFood());
                            return StoreCategory.valueOf(preferredCategory.name()); // StoreCategory는 Enum에 정의된 값을 반환
                        } catch (IllegalArgumentException e) {
                            log.error("Invalid PreferredFoodCategory value: {}", favFood.getPreferredFood(), e);
                            return null; // 예외를 처리하고 null을 반환
                        }
                    })
                    .filter(Objects::nonNull) // null 값 필터링
                    .collect(Collectors.toList());

            storesByFood = storeListRepositoryCustom.findCategoryByFood(categories);
        }

        return storesByFood;
    }
}

