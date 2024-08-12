package org.nmfw.foodietree.domain.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.store.dto.request.ProductApprovalReqDto;
import org.nmfw.foodietree.domain.store.dto.request.StoreApprovalReqDto;
import org.nmfw.foodietree.domain.store.entity.StoreApproval;
import org.nmfw.foodietree.domain.store.service.StoreApprovalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.NoSuchElementException;

import static org.nmfw.foodietree.domain.auth.security.TokenProvider.*;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
@Slf4j
public class StoreApprovalController {

    private final StoreApprovalService storeApprovalService;

    // 가게 디테일 등록 요청
    @PostMapping("/approval")
    public ResponseEntity<?> approveStore(
            @Valid @RequestBody StoreApprovalReqDto dto
        , @AuthenticationPrincipal TokenUserInfo userInfo
    ) {
        // Validation 예외처리는 ExceptionAdvisor.java
        log.info("Request to approveStore : {}", dto.toString());

        // 가게 등록 요청 처리 (tbl_store_approval)
        try {
            storeApprovalService.askStoreApproval(
                    dto, userInfo
            );
        } catch (NoSuchElementException e) { // 등록 요청 실패
//            throw new RuntimeException(e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        // 등록 요청 성공
        return ResponseEntity.ok().body("Approved Store");
    }

    // 상품 디테일 등록 요청
    @ResponseBody
    @PostMapping(value="/approval/p")
    public ResponseEntity<?> approveProduct(
            @Validated @RequestPart(value = "productInfo") ProductApprovalReqDto dto
            , @Validated @RequestPart(value = "productImage") MultipartFile productImage
        , @AuthenticationPrincipal TokenUserInfo userInfo
    ) {
        // 컨트롤러의 Validation 예외처리는 ExceptionAdvisor.java
        // userInfo는 service에서 처리

        dto.setProductImage(productImage);

        // 상품 등록 요청 처리 (tbl_product_approval)
        try {
            storeApprovalService.askProductApproval(
                    dto, userInfo
            );
        } catch (NoSuchElementException e) { // 등록 요청 실패, 예외처리 보완 필요
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        // 등록 요청 성공
        return ResponseEntity.ok().body("");
    }

}
