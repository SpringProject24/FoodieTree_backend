package org.nmfw.foodietree.domain.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.store.dto.resp.StoreMyPageDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreReservationDto;
import org.nmfw.foodietree.domain.store.mapper.StoreMyPageMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StoreMyPageService {

    private final StoreMyPageMapper storeMyPageMapper;

    public StoreMyPageDto getStoreMypageInfo(String storeId) {
        log.info("store my page service");
        return storeMyPageMapper.getStoreMyPageInfo(storeId);
    }

    public List<StoreReservationDto> findReservations(String storeId) {
        log.info("service get store reservations");
        return storeMyPageMapper.findReservations(storeId);
    }
}
