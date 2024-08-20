package org.nmfw.foodietree.domain.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.security.TokenProvider;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListByEndTimeDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListCo2Dto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListDto;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;
import org.nmfw.foodietree.domain.store.service.StoreList.StoreListService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.nmfw.foodietree.domain.auth.security.TokenProvider.*;

@RestController
@RequestMapping("/storeLists")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class StoreListController {

    private final StoreListService storeListService;

    // Store 전체 조회 요청!
    @GetMapping
    public ResponseEntity<List<StoreListDto>> getAllStores(@AuthenticationPrincipal TokenUserInfo userInfo) {
        String customerId = userInfo.getUsername();
        List<StoreListDto> storeListDto = storeListService.getAllStores(customerId);
        return ResponseEntity.ok().body(storeListDto);
    }

    // 카테고리 별 Store 조회 요청!
    @GetMapping("/category")
    public ResponseEntity<List<StoreListDto>> getStoresByCategory(@RequestParam("category") String category) {
        StoreCategory storeCategory = StoreCategory.valueOf(category.toUpperCase());
        List<StoreListDto> storeListDto = storeListService.getStoresByCategory(storeCategory);
        return ResponseEntity.ok().body(storeListDto);
    }


    // co2를 가장 많이 줄인 순
    @GetMapping("/by-product-count")
    public ResponseEntity<List<StoreListCo2Dto>> getStoresByProductCnt() {
        List<StoreListCo2Dto> storeListCo2Dto = storeListService.getStoresByProductCnt();
        return ResponseEntity.ok().body(storeListCo2Dto);
    }

    // 상품 시간이 현재로부터 제일 가까운 순 (마감임박)
    @GetMapping("/by-product-end-time")
    public ResponseEntity<List<StoreListByEndTimeDto>> getStoresByProductEndTime() {
        List<StoreListByEndTimeDto> storeListDto = storeListService.getStoresByProductEndTime();
        return ResponseEntity.ok().body(storeListDto);
    }

    // 지역별 Store 조회 요청!
    @GetMapping("/address")
    public ResponseEntity<List<StoreListDto>> getStoresByAddress(@RequestParam("address") String address) {
        List<StoreListDto> storeListDto = storeListService.getStoresByAddress(address);
        return ResponseEntity.ok().body(storeListDto);
    }

    @GetMapping("/fav")
    public ResponseEntity<?> getFavStoresAndOrders3(@AuthenticationPrincipal TokenUserInfo tokenUserInfo) {
        String customerId = tokenUserInfo.getUsername();
//        String customerId = "thdghtjd115@gmail.com";
        List<StoreListDto> storeListDtos = storeListService.getFavStoresAndOrders3(customerId);
        return ResponseEntity.ok().body(storeListDtos);
    }

    // 선호 카테고리에 해당하는 가게 조회
    @GetMapping("/favCategory")
    public ResponseEntity<List<StoreListDto>> getStoresByPreferredFood(@AuthenticationPrincipal TokenUserInfo tokenUserInfo) {
        String customerId = tokenUserInfo.getUsername();
        List<StoreListDto> storeListDtos = storeListService.findStoresByPreferredFood(customerId, null, null);
        return ResponseEntity.ok().body(storeListDtos);
    }

}

