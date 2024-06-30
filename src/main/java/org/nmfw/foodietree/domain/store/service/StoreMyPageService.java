package org.nmfw.foodietree.domain.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.store.dto.resp.StoreMyPageDto;
import org.nmfw.foodietree.domain.store.mapper.StoreMyPageMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StoreMyPageService {

    private final StoreMyPageMapper storeMyPageMapper;

    public StoreMyPageDto getStoreMypageInfo(String storeId) {
        log.info("store my page service");
        return storeMyPageMapper.getStoreMyPageInfo(storeId);
    }
}
