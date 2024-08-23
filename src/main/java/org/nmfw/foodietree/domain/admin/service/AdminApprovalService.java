package org.nmfw.foodietree.domain.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.admin.dto.req.ApprovalStatusDto;
import org.nmfw.foodietree.domain.admin.dto.res.StoreApproveDto;
import org.nmfw.foodietree.domain.admin.dto.res.ApprovalCellDto;
import org.nmfw.foodietree.domain.notification.dto.req.NotificationDataDto;
import org.nmfw.foodietree.domain.notification.service.NotificationService;
import org.nmfw.foodietree.domain.store.dto.resp.ApprovalInfoDto;
import org.nmfw.foodietree.domain.store.entity.StoreApproval;
import org.nmfw.foodietree.domain.store.entity.value.ApproveStatus;
import org.nmfw.foodietree.domain.store.repository.StoreApprovalRepository;
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
    private final NotificationService notificationService;

    /**
     * 기간을 기준으로 승인 요청 목록 조회
     * @param start - 시작일
     * @param end - 종료일
     * @param userInfo - 토큰
     * @return 조회 결과(approvals)와 상태별 요청 갯수(stats)
     */
    public Map<String, Object> getApprovals(
            LocalDateTime start
            , LocalDateTime end
            , TokenUserInfo userInfo
    ) {
        if(!isAdmin(userInfo)) {
            throw new SecurityException("관리자 권한이 없습니다.");
        }
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
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateApprovalsStatus(
            ApprovalStatusDto dto,
            TokenUserInfo userInfo
    ) {
        if(!isAdmin(userInfo)) {
            throw new SecurityException("관리자 권한이 없습니다.");
        }
        ApproveStatus status = ApproveStatus.valueOf(dto.getActionType().toUpperCase());

        List<Long> approvalIdList = dto.getApprovalIdList();
        Long result = storeApprovalRepository.updateApprovalStatus(status, approvalIdList);
        int updateCnt = result.intValue();

        List<StoreApproval> list = storeApprovalRepository.findAllByIdInIds(approvalIdList);
        if(status == ApproveStatus.APPROVED) { // 승인 요청인 경우 store 업데이트
            updateCnt = sendStoreInfo(list);
        }
        if(approvalIdList.size() != updateCnt) {
            throw new RuntimeException("전체 " + approvalIdList.size()+ "건 중 "
                    + (approvalIdList.size() - updateCnt) + "건 처리 실패" );
        }
        sendNotifications(list, status, userInfo.getUsername());
    }

    /**
     * 요청 Id 기반으로 각각 알림 전송
     *
     * @param list     - StoreApproval Id 리스트
     * @param status   - APPROVED, REJECTED 중 하나
     * @param adminId  - 관리자 계정
     */
    private void sendNotifications(List<StoreApproval> list, ApproveStatus status, String adminId) {
        List<NotificationDataDto> notificationDataDtoList = list.stream().map(sa -> NotificationDataDto.builder()
                .storeId(sa.getStoreId())
                .storeName(sa.getName())
                .targetId(Collections.singletonList(sa.getId().toString()))
                .build()).collect(Collectors.toList());

        notificationService.sendApprovalResult(notificationDataDtoList, status, adminId);
    }

    /**
     * 스토어 등록 요청을 승인하면 store 업데이트
     * @param foundApprovals - 승인된 요청 목록
     */
    public int sendStoreInfo(List<StoreApproval> foundApprovals) {
        // 엔터티를 DTO로 변환
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
