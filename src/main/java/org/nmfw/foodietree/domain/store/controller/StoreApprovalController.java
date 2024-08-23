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
    public ResponseEntity<String> approveStore(
            @Valid @RequestBody StoreApprovalReqDto dto
        , @AuthenticationPrincipal TokenUserInfo userInfo
    ) {
        log.info("Request to approveStore : {}", dto.toString());

        // 가게 등록 요청 처리 (tbl_store_approval)
        try {
            storeApprovalService.askStoreApproval(
                    dto, userInfo
            );
        } catch (NoSuchElementException e) { // 등록 요청 실패
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        return ResponseEntity.ok().body("Approved Store");
    }

    // 상품 디테일 등록 요청
    @ResponseBody
    @PostMapping(value="/approval/p")
    public ResponseEntity<String> approveProduct(
            @Validated @RequestPart(value = "productInfo") ProductApprovalReqDto dto
            , @Validated @RequestPart(value = "productImage") MultipartFile productImage
        , @AuthenticationPrincipal TokenUserInfo userInfo
    ) {
        dto.setProductImage(productImage);
        // 상품 등록 요청 처리 (tbl_product_approval)
        try {
            storeApprovalService.askProductApproval(
                    dto, userInfo
            );
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok().body("");
    }
    // 등록 상태 확인
    @GetMapping("/check/approval")
    public ResponseEntity<String> checkStoreApproval(
            @AuthenticationPrincipal TokenUserInfo userInfo
    ) {
        String approvalState = storeApprovalService.findCurrentApprovalState(userInfo);
        return ResponseEntity.ok().body(approvalState);
    }

}
