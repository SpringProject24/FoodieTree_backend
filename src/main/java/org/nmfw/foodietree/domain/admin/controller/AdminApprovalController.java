package org.nmfw.foodietree.domain.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.admin.dto.req.ApprovalStatusDto;
import org.nmfw.foodietree.domain.admin.service.AdminApprovalService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.nmfw.foodietree.domain.auth.security.TokenProvider.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminApprovalController {

    private final AdminApprovalService adminApprovalService;

    // 스토어 등록 요청 목록을 조회 (서버에서 페이징)
    @GetMapping(value = "/approve/page/{pageNo}")
    public ResponseEntity<?> getPagedList(
            @PathVariable Integer pageNo,
            @AuthenticationPrincipal TokenUserInfo userInfo
    ) {

//        Map<String, Object> approvalsMap = adminService.getApprovals(pageNo, userInfo);
//        return ResponseEntity.ok().body(approvalsMap);
        return ResponseEntity.ok().body("");
    }

    // 스토어 등록 요청 목록을 생성일시 기준 조회
    @GetMapping(value = "/approve")
    public ResponseEntity<?> getListByDate(
            @RequestParam(value = "start", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,
            @RequestParam(value = "end", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end
//          , @AuthenticationPrincipal TokenUserInfo userInfo
    ) {
        if (start == null) {start = LocalDateTime.of(2024, 7, 1, 0, 0, 0);
        }
        if (end == null) {
            end = LocalDateTime.now();
        }
        log.info("관리자 - 승인요청 : 시작일 {} / 종료일 {}", start, end );

        Map<String, Object> approvalsMap = adminApprovalService.getApprovals(
                start
                , end
//                , userInfo
        );
        log.info("조회 결과: {}", approvalsMap.get("approvals").toString());

        return ResponseEntity.ok().body(approvalsMap);
    }


    // 스토어 등록 요청 목록을 관리자가 검토 후 승인 또는 거절 처리
    @RequestMapping("/approve")
    @PostMapping
    public ResponseEntity<?> approveStore(
            @RequestBody ApprovalStatusDto dto
            , @AuthenticationPrincipal TokenUserInfo userInfo
    ) {
//        TokenUserInfo userInfo = null;
        Map<String, Object> result;
        try {
            result = adminApprovalService.updateApprovalsStatus(dto, userInfo);
        } catch (Exception e) { // 예외처리 보완 필요
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body(Map.of("result", result));
    }

}
