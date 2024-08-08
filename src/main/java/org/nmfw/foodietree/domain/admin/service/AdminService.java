package org.nmfw.foodietree.domain.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.admin.dto.res.ApprovalCellDto;
import org.nmfw.foodietree.domain.auth.security.TokenProvider;
import org.nmfw.foodietree.domain.store.dto.resp.ApprovalInfoDto;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.nmfw.foodietree.domain.store.entity.StoreApproval;
import org.nmfw.foodietree.domain.store.entity.value.ApproveStatus;
import org.nmfw.foodietree.domain.store.repository.StoreApprovalRepository;
import org.nmfw.foodietree.domain.store.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.nmfw.foodietree.domain.auth.security.TokenProvider.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminService {

    private final StoreApprovalRepository storeApprovalRepository;
    private final StoreRepository storeRepository;

    // 페이징 된 요청 리스트
//    public Map<String, Object> getApprovals(
//            int page,
//            TokenUserInfo userInfo
//    ) {
////        if(!isAdmin(userInfo)) {
////            throw new RuntimeException("관리자 권한이 없습니다.");
////        }
//        Pageable pageable = PageRequest.of(page, 10);
//
//        ApproveStatus approveStatus = ApproveStatus.APPROVED;
//        Page<ApprovalInfoDto> approvalsPage = storeApprovalRepository.findApprovalsByStatus(pageable, approveStatus);
//        List<ApprovalInfoDto> approvals = approvalsPage.getContent();
//
//        // 총 이벤트 개수
//        long totalElements = approvalsPage.getTotalElements();
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("approvals", approvals);
//        map.put("totalCount", totalElements);
//
//        return map;
//    }
    // 기간 기준으로 필터링 된 요청 리스트
    public Map<String, Object> getApprovals(
            LocalDateTime start
            , LocalDateTime end
//            , TokenUserInfo userInfo
    ) {
//        if(!isAdmin(userInfo)) {
//            throw new RuntimeException("관리자 권한이 없습니다.");
//        }
        LocalDateTime startDate = timeConverter(start, "start");
        LocalDateTime endDate = timeConverter(end, "end");

        List<ApprovalInfoDto> approvalList = storeApprovalRepository.findAllByDate(startDate, endDate);
        List<ApprovalCellDto> approvals = new ArrayList<>();
        for (ApprovalInfoDto approval : approvalList) {
            ApprovalCellDto approvalCell = new ApprovalCellDto(approval);
            approvals.add(approvalCell);
        }
        // 총 이벤트 개수
//        long totalElements = storeApprovalRepository.count();

        Map<String, Object> map = new HashMap<>();
        map.put("approvals", approvals);
//        map.put("totalCount", totalElements);

        return map;
    }

    /**
     * 스토어 등록 요청을 승인하면 store 업데이트
     * @param storeId - 승인된 스토어 계정
     * @param userInfo - 관리자 정보를 담은 토큰
     */
    public void sendStoreInfo(
            String storeId,
            TokenUserInfo userInfo
    ) {
        // 관리자가 아닌 경우 BadRequest
//        if(!isAdmin(userInfo)) {
//            throw new RuntimeException("관리자 권한이 없습니다.");
//        }

        StoreApproval foundApproval = storeApprovalRepository.findByStoreId(storeId);

        Store foundStore = storeRepository.findByStoreId(foundApproval.getStoreId())
                .orElseThrow(()->new NoSuchElementException(storeId + " : 회원 가입 내역이 없습니다."));

        if(foundApproval.getStatus() == ApproveStatus.APPROVED
                || foundApproval.getStatus() == ApproveStatus.REJECTED) {
            throw new RuntimeException("이미 검토한 요청입니다.");
        }
        // store approve 상태를 APPROVED, storeApproval 정보 setter로 업데이트
        Store updatedStore = foundApproval.updateFromStoreApproval(foundStore);

        Store saved = storeRepository.save(updatedStore);
        log.info("saved store: {}", saved);
    }

    // Token role 확인
    public boolean isAdmin(TokenUserInfo userInfo) {
        return userInfo.getRole().equalsIgnoreCase("admin");
    }

    //
    public LocalDateTime timeConverter(LocalDateTime time, String type) {
        // 한국 시간으로 변환된 LocalDateTime을 UTC로 변환
        ZoneId koreaZoneId = ZoneId.of("Asia/Seoul");
        LocalDateTime timeUtc = ZonedDateTime.of(time, koreaZoneId).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();

        if(type.equalsIgnoreCase("start")) { // 시작일의 0시 0분
            timeUtc = timeUtc.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        } else if(type.equalsIgnoreCase("end")){ // 종료일의 11시 59분 59초
            timeUtc = timeUtc.plusDays(1).withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);
        }

        return timeUtc;
    }

}
