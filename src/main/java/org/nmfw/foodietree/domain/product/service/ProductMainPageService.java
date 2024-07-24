package org.nmfw.foodietree.domain.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.resp.CustomerFavStoreDto;
import org.nmfw.foodietree.domain.customer.dto.resp.PreferredFoodDto;
import org.nmfw.foodietree.domain.customer.mapper.CustomerMyPageMapper;
import org.nmfw.foodietree.domain.customer.service.CustomerMyPageService;
import org.nmfw.foodietree.domain.product.dto.response.ProductDto;
import org.nmfw.foodietree.domain.product.dto.response.TotalInfoDto;
import org.nmfw.foodietree.domain.product.mapper.ProductMainPageMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductMainPageService {

    private final ProductMainPageMapper productMainPageMapper;
    private final CustomerMyPageService customerMyPageService;

    /**
     * Retrieves product information for the main page based on customer's preferred food.
     *
     * @param request  the HTTP servlet request
     * @param response the HTTP servlet response
     * @param customerId the ID of the customer
     * @return TotalInfoDto containing product information
     */
    public TotalInfoDto getProductInfo(HttpServletRequest request, HttpServletResponse response, String customerId) {
        List<PreferredFoodDto> preferredFood = customerMyPageService.getCustomerInfo(customerId).getPreferredFood();

//        if (preferredFood == null) {
//            log.warn("Preferred food list is null for customerId: {}", customerId);
//            return null; // or handle the case accordingly
//        }

        return TotalInfoDto.builder()
                .productDtoList(productMainPageMapper.findAll())
                .preferredFood(preferredFood)
                .build();
    }

    /**
     * Finds products by customer's preferred food.
     *
     * @param customerId the ID of the customer
     * @param request    the HTTP servlet request
     * @param response   the HTTP servlet response
     * @return a list of TotalInfoDto containing filtered product information
     */
    public List<ProductDto> findProductByFood(String customerId, HttpServletRequest request, HttpServletResponse response) {
        List<PreferredFoodDto> preferredFood = customerMyPageService.getCustomerInfo(customerId).getPreferredFood();
//        if (preferredFood == null) {
//            log.warn("Preferred food list is null for customerId: {}", customerId);
//            return null; // or handle the case accordingly
//        }
        List<ProductDto> categoryByFood = productMainPageMapper.findCategoryByFood(preferredFood);

        for (ProductDto productDto : categoryByFood) {
            LocalDateTime pickupTime = productDto.getPickupTime();
            String formatted = setFormattedPickupTime(pickupTime);

            productDto.setFormattedPickupTime(formatted);
        }
        return categoryByFood;
    }



    public List<ProductDto> findProductByArea(String customerId, HttpServletRequest request, HttpServletResponse response) {
        List<String> preferredArea = customerMyPageService.getCustomerInfo(customerId).getPreferredArea();
//        if (preferredArea == null) {
//            log.warn("Preferred area list is null for customerId: {}", customerId);
//            return null; // or handle the case accordingly
//        }

        List<ProductDto> categoryByArea = productMainPageMapper.findCategoryByArea(customerId);

        for (ProductDto productDto : categoryByArea) {
            LocalDateTime pickupTime = productDto.getPickupTime();
            String formatted = setFormattedPickupTime(pickupTime);

            productDto.setFormattedPickupTime(formatted);
        }

        return categoryByArea;
    }

    public String setFormattedPickupTime(LocalDateTime pickupTime) {
        if (pickupTime == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월 dd일 HH시");
        return pickupTime.format(formatter);
    }

    public List<ProductDto> findProductByLike(String customerId, HttpServletRequest request, HttpServletResponse response) {
        List<CustomerFavStoreDto> favStore = customerMyPageService.getCustomerInfo(customerId).getFavStore();
        List<ProductDto> categoryByLike = productMainPageMapper.findCategoryByLike(customerId);
        for (ProductDto productDto : categoryByLike) {
            LocalDateTime pickupTime = productDto.getPickupTime();
            String formatted = setFormattedPickupTime(pickupTime);
            log.info("리스트 이터레이션중: {}",productDto);

            productDto.setFormattedPickupTime(formatted);
        }
        return categoryByLike;
    }

    public ProductDto getProductById(String productId) {
    return productMainPageMapper.findById(productId);
    }


}
