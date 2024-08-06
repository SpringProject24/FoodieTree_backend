package org.nmfw.foodietree.domain.store.repository;

import org.nmfw.foodietree.domain.store.dto.resp.ApprovalListDto;
import org.nmfw.foodietree.domain.store.entity.StoreApproval;
import org.nmfw.foodietree.domain.store.entity.value.ApproveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreApprovalRepositoryCustom {

    // 등록 요청 상태(PENDING, APPROVED, REJECTED)에 따라 목록 조회
//    Page<ApprovalListDto> findStoreApprovals(Pageable, String sort, ApproveStatus status);
    Page<ApprovalListDto> findApprovalsByStatus(Pageable pageable, ApproveStatus status);

    // 사업자등록번호 검증하지 않은 요청 목록 조회
    List<StoreApproval> findApprovalsByLicenseVerification();

}
