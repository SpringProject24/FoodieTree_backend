package org.nmfw.foodietree.domain.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.admin.dto.req.ApprovalStatusDto;
import org.nmfw.foodietree.domain.admin.dto.res.StoreApproveDto;
import org.nmfw.foodietree.domain.admin.dto.res.ApprovalCellDto;
import org.nmfw.foodietree.domain.store.dto.resp.ApprovalInfoDto;
import org.nmfw.foodietree.domain.store.entity.StoreApproval;
import org.nmfw.foodietree.domain.store.entity.value.ApproveStatus;
import org.nmfw.foodietree.domain.store.repository.StoreApprovalRepository;
import org.nmfw.foodietree.domain.store.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class AdminApprovalService {

    private final StoreApprovalRepository storeApprovalRepository;

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
//        // 총 개수
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
        // 요청 상태별 count
        Map<ApproveStatus, Long> stats = approvalList.stream().collect(Collectors.groupingBy(
                ApprovalInfoDto::getStatus,
                Collectors.counting()
        ));

        Map<String, Object> map = new HashMap<>();
        map.put("approvals", approvals);
        map.put("stats", stats);

        return map;
    }

    /**
     * 스토어 등록 요청 리스트를 한 번에 승인, 거절 처리
     * @param dto - 액션타입(승인, 거절)과 요청 Id 목록
     * @param userInfo - 관리자 정보를 담은 토큰
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int updateApprovalsStatus(
            ApprovalStatusDto dto,
            TokenUserInfo userInfo
    ) {
        // 관리자가 아닌 경우 BadRequest
//        if(!isAdmin(userInfo)) {
//            throw new RuntimeException("관리자 권한이 없습니다.");
//        }
        ApproveStatus status = ApproveStatus.valueOf(dto.getActionType().toUpperCase());

        List<Long> approvalIdList = dto.getApprovalIdList();
        Long result = storeApprovalRepository.updateApprovalStatus(status, approvalIdList);
        int updateCnt = result.intValue();

        if(status.equals(ApproveStatus.APPROVED)) { // 승인 요청인 경우 store 업데이트
            updateCnt = sendStoreInfo(approvalIdList, userInfo);
        }
        return updateCnt;
    }

    /**
     * 스토어 등록 요청을 승인하면 store 업데이트
     * @param ids - 승인된 요청 Id 목록
     * @param userInfo - 관리자 정보를 담은 토큰
     */
    public int sendStoreInfo(
            List<Long> ids,
            TokenUserInfo userInfo
    ) {
        // 엔터티를 DTO로 변환
        List<StoreApproval> foundApprovals = storeApprovalRepository.findAllByIdInIds(ids);
        List<StoreApproveDto> dtos = foundApprovals.stream()
                .map(StoreApproveDto::new)
                .collect(Collectors.toList());

        // DTO를 사용하여 업데이트 수행
        Long result = storeApprovalRepository.updateStoreInfo(dtos);

        return result.intValue();
    }


    // Token role admin 여부 확인
    public boolean isAdmin(TokenUserInfo userInfo) {
        return userInfo.getRole().equalsIgnoreCase("admin");
    }

    // 한국 시간으로 변환된 LocalDateTime을 UTC로 변환
    public LocalDateTime timeConverter(LocalDateTime time, String type) {
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
