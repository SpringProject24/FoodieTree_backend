package org.nmfw.foodietree.domain.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.admin.service.AdminService;
import org.nmfw.foodietree.domain.store.dto.resp.ApprovalInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.nmfw.foodietree.domain.auth.security.TokenProvider.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;

    // 스토어 등록 요청 목록을 조회
    @RequestMapping("/approve/page/{pageNo}")
    @GetMapping
    public ResponseEntity<?> getList(
            @PathVariable Integer pageNo,
            @AuthenticationPrincipal final UserDetails userDetails) {

        Map<String,Object> approvalsMap = adminService.getApprovals(pageNo);

        return ResponseEntity.ok().body(approvalsMap);
    }

    // 스토어 등록 요청을 승인
    @RequestMapping("/approve")
    @PostMapping
    public ResponseEntity<?> approveStore(
            @RequestBody String storeId,
            @AuthenticationPrincipal TokenUserInfo userInfo
    ) {

        try {
            adminService.sendStoreInfo(storeId, userInfo);
        } catch (Exception e) { // 예외처리 보완 필요
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().body("");
    }

}
