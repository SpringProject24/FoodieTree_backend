package org.nmfw.foodietree.domain.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.store.dto.request.StoreApprovalReqDto;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.nmfw.foodietree.domain.store.entity.StoreApproval;
import org.nmfw.foodietree.domain.store.repository.StoreApprovalRepository;
import org.nmfw.foodietree.domain.store.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreApprovalService {

    //    private final StoreApprovalMapper storeApprovalMapper;
    private final StoreApprovalRepository storeApprovalRepository;
    private final StoreRepository storeRepository;

    // 등록 요청 내역을 tbl_store_approval에 저장
    public void askStoreApproval(
            StoreApprovalReqDto dto
//            TokenUserInfo userInfo
    ) {
        // userInfo storeId로 Store
//        Store foundStore = storeRepository
//                .findById(userInfo.getUserId())
//                .orElseThrow(throw new NoSuchElementException());

        // 테스트용으로 storeId = 'test@test.com'
        String storeId = "test@test.com";
        Store foundStore = storeRepository
                .findByStoreId(storeId)
                .orElseThrow(() -> new NoSuchElementException("가입한 계정이 아닙니다."));
        log.debug("등록요청: 가게 foundStore: {}", foundStore);

        StoreApproval storeApproval = dto.toEntity();
        storeApproval.setStoreId(storeId);
        StoreApproval saved = storeApprovalRepository.save(storeApproval);
        log.debug("saved storeApproval: {}", saved);
    }

    // 가게 등록 요청이 승인되면 tbl_store에 저장
    public void sendStoreInfo(
            StoreApproval sa
    ) {
        Store updatedStore = sa.updateFromStoreApproval();
        Store saved = storeRepository.save(updatedStore);
        log.info("saved store: {}", saved);

    }
}
