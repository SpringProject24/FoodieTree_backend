package org.nmfw.foodietree.domain.customer.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.customer.dto.resp.PreferredFoodDto;
import org.nmfw.foodietree.domain.customer.service.CustomerMyPageService;
import org.nmfw.foodietree.domain.product.dto.response.ProductDto;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class FavFoodRepositoryCustomImplTest {
    @Autowired
    FavFoodRepository favFoodRepository;
    @Autowired
    CustomerMyPageService customerMyPageService;

//    @Test
//    @DisplayName("findCategoryByFood 테스트")
//    void findByCategoryTest() {
//        String customerId = "summer7@gmail.com";
//        List<PreferredFoodDto> preferredFood = customerMyPageService.getCustomerInfo(customerId).getPreferredFood();
//        List<StoreCategory> categories = preferredFood.stream()
//                .map(preferredFoodDto -> StoreCategory.fromString(preferredFoodDto.getPreferredFood()))
//                .collect(Collectors.toList()); // 한식, 중식, 일식
//        List<ProductDto> categoryByFood = favFoodRepository.findCategoryByFood(categories);
//        assertNotNull(categoryByFood);
//    }
//    @Test
//    @DisplayName("findByReviews 테스트 - 최근 예약 productId 5개로 상품조회")
//    void findByReviewsTest() {
//        List<ProductDto> byReviews = favFoodRepository.findByReviews();
//
//        assertNotNull(byReviews);
//    }

}