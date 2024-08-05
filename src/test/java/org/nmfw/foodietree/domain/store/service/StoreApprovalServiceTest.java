package org.nmfw.foodietree.domain.store.service;

import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.store.dto.request.StoreApprovalReqDto;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.nmfw.foodietree.domain.store.entity.StoreApproval;
import org.nmfw.foodietree.domain.store.entity.value.ApproveStatus;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;
import org.nmfw.foodietree.domain.store.repository.StoreApprovalRepository;
import org.nmfw.foodietree.domain.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class StoreApprovalServiceTest {

    @Autowired
    StoreApprovalService storeApprovalService;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private StoreApprovalRepository storeApprovalRepository;

    @Test
    public void sendStoreInfo() {
        Store saved = storeRepository.save(Store.builder().storeId("test@test.com").build());

        StoreApprovalReqDto dto = StoreApprovalReqDto.builder()
                .storeLicenseNumber("12334567891")
                .storeContact("03112345678")
                .storeName("테스트")
                .category(StoreCategory.한식.toString())
                .address("서울시 종로구")
                .build();

        storeApprovalService.askStoreApproval(dto);

        List<StoreApproval> all = storeApprovalRepository.findAll();
        int size = all.size();
        StoreApproval storeApproval = all.get(size - 1);

        storeApprovalService.sendStoreInfo(storeApproval);
    }

}