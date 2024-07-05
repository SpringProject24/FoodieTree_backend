package org.nmfw.foodietree.domain.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.store.dto.resp.StoreMyPageDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreStatsDto;
import org.nmfw.foodietree.domain.store.mapper.StoreMyPageEditMapper;
import org.nmfw.foodietree.domain.store.mapper.StoreMyPageMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
@Slf4j
@RequiredArgsConstructor
public class StoreMyPageEditService {

    private final StoreMyPageService storeMyPageService;
    private final StoreMyPageEditMapper storeMyPageEditMapper;
    private final StoreMyPageMapper storeMyPageMapper;
    private final PasswordEncoder encoder;


    public StoreMyPageDto getStoreMyPageInfo(String storeId) {
        log.info("store my page service");
        return storeMyPageMapper.getStoreMyPageInfo(storeId);
    }

    public StoreStatsDto getStats(String storeId) {
        log.info("get stats");
        return storeMyPageService.getStats(storeId);
    }

    public void updateStoreInfo(String storeId, String type, String value) {
        log.info("update store info");
        storeMyPageEditMapper.updateStoreInfo(storeId, type, value);
    }

    public void updatePrice(String storeId, int price) {
        log.info("update price");
        storeMyPageEditMapper.updatePrice(storeId, price);
    }

    public void updateOpenAt(String storeId, String time) {
        log.info("update open at");
        time = time.replaceAll("\"", "");
        LocalTime openAt = LocalTime.parse(time);
        storeMyPageEditMapper.updateOpenAt(storeId, openAt);
    }

    public void updateClosedAt(String storeId, String time) {
        log.info("update closed at");
        // 시간 문자열에서 따옴표 등을 제거
        time = time.replaceAll("\"", "");

        try {
            // 시간 문자열을 LocalTime으로 파싱
            LocalTime closedAt = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));

            // Mapper를 통해 업데이트 실행
            storeMyPageEditMapper.updateClosedAt(storeId, closedAt);

        } catch (DateTimeParseException e) {
            log.error("Failed to parse time string: {}", time);
            throw new IllegalArgumentException("Invalid time format. Use HH:mm format.", e);
        }
    }

    public void updateProductCnt(String storeId, int cnt) {
        log.info("update product cnt");
        storeMyPageEditMapper.updateProductCnt(storeId, cnt);
    }

    public boolean updateStorePw(String storeId, String newPassword) {
        String encodedPw = encoder.encode(newPassword);
        storeMyPageEditMapper.updateStoreInfo(storeId,"password", encodedPw);
        return true;
    }
}
