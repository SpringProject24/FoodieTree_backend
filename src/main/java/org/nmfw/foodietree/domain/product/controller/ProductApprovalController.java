package org.nmfw.foodietree.domain.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.product.dto.request.ProductApprovalReqDto;
import org.nmfw.foodietree.domain.product.service.ProductApprovalService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
@Slf4j
public class ProductApprovalController {

    private final ProductApprovalService productApprovalService;

    // 상품 등록 요청
    @ResponseBody
    @PostMapping(value="/approval/p")
    public ResponseEntity<?> approveProduct(
            @Validated @RequestPart(value = "productInfo") ProductApprovalReqDto dto
          , @Validated @RequestPart(value = "productImage") MultipartFile productImage
//        , @AuthenticationPrincipal TokenUserInfo userInfo
    ) {
        // 컨트롤러의 Validation 예외처리는 ExceptionAdvisor.java
        // userInfo는 service에서 처리

        dto.setProductImage(productImage);

        // 상품 등록 요청 처리 (tbl_product_approval)
        try {
            productApprovalService.askProductApproval(
                    dto
//                    , userInfo
            );
        } catch (Exception e) { // 등록 요청 실패, 예외처리 보완 필요
            e.printStackTrace();
//            throw new RuntimeException(e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        // 등록 요청 성공
        return ResponseEntity.ok().body("");
    }

}
