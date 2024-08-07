package org.nmfw.foodietree.domain.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.nmfw.foodietree.domain.auth.security.TokenProvider.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminService {

    private final StoreApprovalRepository storeApprovalRepository;
    private final StoreRepository storeRepository;

    public Map<String, Object> getApprovals(int page) {
        Pageable pageable = PageRequest.of(page, 10);

        ApproveStatus approveStatus = ApproveStatus.APPROVED;
        Page<ApprovalInfoDto> approvalsPage = storeApprovalRepository.findApprovalsByStatus(pageable, approveStatus);
        List<ApprovalInfoDto> approvals = approvalsPage.getContent();

        // 총 이벤트 개수
        long totalElements = approvalsPage.getTotalElements();

        Map<String, Object> map = new HashMap<>();
        map.put("approvals", approvals);
        map.put("totalCount", totalElements);

        return map;
    }
    public Map<String, Object> getApprovals() {

        List<ApprovalInfoDto> approvals = storeApprovalRepository.findAllByDate();

        // 총 이벤트 개수
        long totalElements = storeApprovalRepository.count();

        Map<String, Object> map = new HashMap<>();
        map.put("approvals", approvals);
        map.put("totalCount", totalElements);

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
        if(!userInfo.getRole().equals("ADMIN")) {
            throw new RuntimeException("관리자 권한이 없습니다.");
        }

        StoreApproval foundApproval = storeApprovalRepository.findByStoreId(storeId);

        Store foundStore = storeRepository.findByStoreId(foundApproval.getStoreId())
                .orElseThrow(()->new NoSuchElementException("존재하지 않는 스토어입니다."));

        if(foundApproval.getStatus() == ApproveStatus.APPROVED
                || foundApproval.getStatus() == ApproveStatus.REJECTED) {
            throw new RuntimeException("이미 검토한 요청입니다.");
        }
        // store approve 상태를 APPROVED, storeApproval 정보 setter로 업데이트
        Store updatedStore = foundApproval.updateFromStoreApproval(foundStore);

        Store saved = storeRepository.save(updatedStore);
        log.info("saved store: {}", saved);
    }

}
