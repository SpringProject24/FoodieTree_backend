package org.nmfw.foodietree.domain.product.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.resp.PreferredFoodDto;
import org.nmfw.foodietree.domain.customer.mapper.CustomerMyPageMapper;
import org.nmfw.foodietree.domain.customer.service.CustomerMyPageService;
import org.nmfw.foodietree.domain.product.dto.response.ProductDto;
import org.nmfw.foodietree.domain.product.dto.response.CategoryByFoodDto;
import org.nmfw.foodietree.domain.product.dto.response.TotalInfoDto;
import org.nmfw.foodietree.domain.product.mapper.ProductMainPageMapper;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

public class ProductMainPageService {
    private final ProductMainPageMapper productMainPageMapper;
    private final CustomerMyPageMapper customerMyPageMapper;
    private final CustomerMyPageService customerMyPageService;


    // product 메인페이지 상품정보 조회 중간 처리
    public TotalInfoDto getProductInfo(HttpServletRequest request, HttpServletResponse response, String customerId) {
        List<String> preferredFood = customerMyPageService.getCustomerInfo(customerId, request, response).getPreferredFood();
        List<String> preferredArea = customerMyPageService.getCustomerInfo(customerId, request, response).getPreferredArea();
        return TotalInfoDto.builder()
                .productDtoList(productMainPageMapper.findAll())
                .preferredFood(preferredFood)
                .preferredArea(preferredArea)
                .build();
    }

    public List<TotalInfoDto> findProductByFood(String customerId){
        List<TotalInfoDto> categoryByFood = productMainPageMapper.findCategoryByFood(customerId);

        return categoryByFood;
    }

    public List<TotalInfoDto> findProductByArea(String customerId){
        List<TotalInfoDto> categoryByArea = productMainPageMapper.findCategoryByArea(customerId);

        return categoryByArea;
    }


//    public List<String> getCategoryByFood(String customerId) {
//        List<String> preferenceFoods = customerMyPageMapper.findPreferenceFoods(customerId);
//        preferenceFoods.forEach(e-> log.info("{}", e));
//        if (preferenceFoods.isEmpty()) {
//            log.info("null");
//            return getProductInfo();
//        }
//        return productMainPageMapper.categoryByFoodList(preferenceFoods);
//    }
//
//    public List<String> getCategoryByArea(String customerId) {
//        List<String> preferenceAreas = customerMyPageMapper.findPreferenceAreas(customerId);
//        if (preferenceAreas.isEmpty()) {
//            return getProductInfo();
//        }
//        return productMainPageMapper.categoryByAreaList(preferenceAreas);
//    }



}
