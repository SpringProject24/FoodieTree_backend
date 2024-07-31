package org.nmfw.foodietree.domain.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.product.controller.ProductApprovalController;
import org.nmfw.foodietree.domain.store.dto.request.StoreApprovalReqDto;
import org.nmfw.foodietree.domain.store.service.StoreApprovalService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
@Slf4j
public class StoreApprovalController {

    private final StoreApprovalService storeApprovalService;

    // 가게 등록 요청
    @PostMapping("/approval")
    public ResponseEntity<?> approveStore(
            @Valid @RequestBody StoreApprovalReqDto dto
//        , @AuthenticationPrincipal TokenUserInfo userInfo
    ) {
        // Validation 예외처리는 ExceptionAdvisor.java
        log.info("Request to approveStore : {}", dto.toString());

        // 가게 등록 요청 처리 (tbl_store_approval)
        try {
            storeApprovalService.askStoreApproval(
                    dto
    //                , userInfo
            );
        } catch (NoSuchElementException e) { // 등록 요청 실패
//            throw new RuntimeException(e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        // 등록 요청 성공
        return ResponseEntity.ok().body("");
    }
}
