package org.nmfw.foodietree.domain.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.store.dto.resp.StoreApprovalDto;
import org.nmfw.foodietree.domain.store.mapper.StoreApprovalMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreApprovalService {

    private final StoreApprovalMapper storeApprovalMapper;

    public void registerStoreInfo(StoreApprovalDto storeApprovalDto) {
        StoreApprovalDto store = storeApprovalMapper.selectStoreById(storeApprovalDto.getStoreId());
        if (store != null) {
            store.setStoreName(storeApprovalDto.getStoreName());
            store.setAddress(storeApprovalDto.getAddress());
            store.setCategory(storeApprovalDto.getCategory());
            store.setBusinessNumber(storeApprovalDto.getBusinessNumber());
            store.setStoreLicenseNumber(storeApprovalDto.getStoreLicenseNumber());
            storeApprovalMapper.updateStoreInfo(store);
        } else {
            log.error("해당 storeId를 가진 업체가 존재하지 않습니다: {}", storeApprovalDto.getStoreId());
            // 예외 처리 또는 다른 로직 추가
        }
    }
}
