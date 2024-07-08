package org.nmfw.foodietree.domain.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.resp.CustomerMyPageDto;
import org.nmfw.foodietree.domain.product.dto.response.ProductDto;
import org.nmfw.foodietree.domain.product.dto.response.TotalInfoDto;
import org.nmfw.foodietree.domain.product.service.ProductMainPageService;
import org.nmfw.foodietree.domain.customer.service.CustomerMyPageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductMainPageController {

    private final ProductMainPageService productMainPageService;
    private final CustomerMyPageService customerMyPageService;

    @GetMapping("/main")
    public String mainpageMain(HttpSession session,
                               Model model,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        log.info("/product/mainpage-main GET");

        String customerId = "test@gmail.com";
        // 1. 세션이나 요청에서 고객 ID 가져오기 (하드코딩된 값 대신)
//        if (customerId == null) {
//            log.warn("Customer ID not found in session");
//            return "redirect:/login"; // 고객 ID가 없으면 로그인 페이지로 리디렉션
//        }
//
//        try {
//            // 2. 제품 정보 조회
//            TotalInfoDto totalInfo = productMainPageService.getProductInfo(request, response, customerId);
//            if (totalInfo != null) {
//                totalInfo.getProductDtoList().forEach(e -> log.info("{}", e));
//                model.addAttribute("productTotal", totalInfo);
//            } else {
//                log.warn("No product information found for customerId: {}", customerId);
//            }

            // 3. 선호 음식 기반 제품 조회
            List<ProductDto> productByFood = productMainPageService.findProductByFood(customerId, request, response);
            productByFood.forEach(e -> {
            });
            model.addAttribute("findByFood", productByFood);

            // 4. 선호 지역 기반 제품 조회
            List<ProductDto> productByArea = productMainPageService.findProductByArea(customerId, request, response);
            model.addAttribute("findByArea", productByArea);

            List<ProductDto> productByLike = productMainPageService.findProductByLike(customerId, request, response);
            model.addAttribute("findByLike",productByLike);

        // 5. 고객 정보 조회
            CustomerMyPageDto customerMyPageDto = customerMyPageService.getCustomerInfo(customerId, request, response);
            if (customerMyPageDto != null) {
                model.addAttribute("customerMyPageDto", customerMyPageDto);
            } else {
                log.warn("No customer information found for customerId: {}", customerId);
            }
//
//        } catch (Exception e) {
//            log.error("Error occurred while processing /product/mainpage-main", e);
//            model.addAttribute("errorMessage", "An error occurred while retrieving product information.");
//            return "error";
//        }

        // 6. JSP 파일로 이동
        return "product/main";
    }
}
