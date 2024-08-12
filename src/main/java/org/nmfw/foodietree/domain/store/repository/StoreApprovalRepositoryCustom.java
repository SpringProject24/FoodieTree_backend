package org.nmfw.foodietree.domain.store.repository;

import org.nmfw.foodietree.domain.admin.dto.res.StoreApproveDto;
import org.nmfw.foodietree.domain.store.dto.resp.ApprovalInfoDto;
import org.nmfw.foodietree.domain.store.entity.StoreApproval;
import org.nmfw.foodietree.domain.store.entity.value.ApproveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface StoreApprovalRepositoryCustom {

    // 등록 요청 상태(PENDING, APPROVED, REJECTED)에 따라 목록 조회
//    Page<ApprovalListDto> findStoreApprovals(Pageable, String sort, ApproveStatus status);
    Page<ApprovalInfoDto> findApprovalsByStatus(Pageable pageable, ApproveStatus status);

    // 사업자등록번호 검증하지 않은 요청 목록 조회
    List<StoreApproval> findApprovalsByLicenseVerification();

    // 가게 이메일로 등록 요청 조회
    ApprovalInfoDto findApprovalsByStoreId(String storeId);

    // 기간 기준으로 등록 요청 조회
    List<ApprovalInfoDto> findAllByDate(LocalDateTime startDate, LocalDateTime endDate);

    // 요청 id 리스트로 요청 status를 bulk update
    Long updateApprovalStatus(ApproveStatus status, List<Long> ids);

    // 요청 id 리스트로 엔터티 조회
    List<StoreApproval> findAllByIdInIds(List<Long> ids);

    // storeApproval 정보를 store 에 추가 (update)
    Long updateStoreInfo(List<StoreApproveDto> approvals);

}
