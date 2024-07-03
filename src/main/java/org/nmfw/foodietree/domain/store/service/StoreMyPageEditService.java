package org.nmfw.foodietree.domain.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.store.dto.resp.StoreMyPageDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreStatsDto;
import org.nmfw.foodietree.domain.store.mapper.StoreMyPageEditMapper;
import org.nmfw.foodietree.domain.store.mapper.StoreMyPageMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StoreMyPageEditService {

    private final StoreMyPageService storeMyPageService;
    private final StoreMyPageEditMapper storeMyPageEditMapper;
    private final StoreMyPageMapper storeMyPageMapper;

    public StoreMyPageDto getStoreMyPageInfo(String storeId) {
        log.info("store my page service");
        return storeMyPageMapper.getStoreMyPageInfo(storeId);
    }

    public StoreStatsDto getStats(String storeId) {
        log.info("get stats");
        return storeMyPageService.getStats(storeId);
    }

    public void updateStoreInfo() {
        log.info("update store info");
    }

    public void updatePrice() {
        log.info("update price");
    }

    public void updateOpenAt() {
        log.info("update open at");
    }

    public void updateClosedAt() {
        log.info("update closed at");
    }

    public void updateProductCnt() {
        log.info("update product cnt");
    }
}
