package org.nmfw.foodietree.domain.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.admin.service.AdminService;
import org.nmfw.foodietree.domain.store.service.StoreApprovalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.nmfw.foodietree.domain.auth.security.TokenProvider.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;
    private final StoreApprovalService storeApprovalService;

    // 승인 요청
    @RequestMapping("/approve")
    @PostMapping
    public ResponseEntity<?> approveStore(
            @RequestBody String storeId,
            @AuthenticationPrincipal TokenUserInfo userInfo
    ) {

        try {
            storeApprovalService.sendStoreInfo(storeId, userInfo);
        } catch (Exception e) { // 예외처리 보완 필요
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().body("");
    }

}
