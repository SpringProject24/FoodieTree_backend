package org.nmfw.foodietree.domain.store.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.store.dto.resp.StoreApprovalDto;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class StoreApprovalMapperTest {

    @Autowired
    private StoreApprovalMapper storeApprovalMapper;
    @Autowired
    private StoreMapper storeMapper;

    @BeforeEach
    public void setUp(){
        Store store = Store.builder()
                .storeId("testStoreId")
                .password("12341234")
                .build();
        storeMapper.storeSave(store);
    }

    @Test
    public void testSelectStoreById() {
        StoreApprovalDto store = storeApprovalMapper.selectStoreById("testStoreId");
        assertNotNull(store);
    }

    @Test
    public void testUpdateStoreInfo() {
        StoreApprovalDto store = StoreApprovalDto.builder()
                .storeId("testStoreId")
                .storeName("Updated Store")
                .address("456 Updated Address")
                .category(StoreCategory.CAFE)
                .businessNumber("111111111")
                .storeLicenseNumber("222222222")
                .build();

        storeApprovalMapper.updateStoreInfo(store);

        StoreApprovalDto updatedStore = storeApprovalMapper.selectStoreById("testStoreId");
        assertEquals("Updated Store", updatedStore.getStoreName());
        assertEquals("456 Updated Address", updatedStore.getAddress());
        assertEquals(StoreCategory.CAFE.getFoodType(), updatedStore.getCategory());
        assertEquals("111111111", updatedStore.getBusinessNumber());
        assertEquals("222222222", updatedStore.getStoreLicenseNumber());
    }
}
