package org.nmfw.foodietree.domain.product.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.customer.mapper.CustomerMyPageMapper;
import org.nmfw.foodietree.domain.product.dto.response.ProductDto;
import org.nmfw.foodietree.domain.product.dto.response.TotalInfoDto;
import org.nmfw.foodietree.domain.product.mapper.ProductMainPageMapper;
import org.nmfw.foodietree.domain.product.service.ProductMainPageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ProductMainPageMapperTest {

    @Autowired
    private CustomerMyPageMapper customerMyPageMapper;
    @Autowired
    private ProductMainPageMapper productMainPageMapper;
    @Autowired
    private ProductMainPageService productMainPageService;

//    @Test
//    @DisplayName("선호지역에 따른상품 조회")
//    void findPreferenceAreaTest() {
//        //given
//        String customerId = "test@gmail.com";
//        List<TotalInfoDto> categoryByArea = productMainPageMapper.findCategoryByArea(customerId);
//
//        System.out.println("\n\n\n");
//        categoryByArea.forEach(System.out::println);
//        System.out.println("\n\n\n");
//
//    }

    @Test
    @DisplayName("선호지역에 따른상품 조회")
    void findPreferenceFoodTest() {
        //given
        String customerId = "test@gmail.com";


        System.out.println("\n\n\n");

        System.out.println("\n\n\n");

    }




}