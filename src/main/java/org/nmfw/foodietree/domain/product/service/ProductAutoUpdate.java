package org.nmfw.foodietree.domain.product.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.store.dto.resp.StoreCheckDto;
import org.nmfw.foodietree.domain.store.mapper.StoreMyPageMapper;
import org.nmfw.foodietree.domain.store.service.StoreMyPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductAutoUpdate {

    private final StoreMyPageMapper storeMyPageMapper;
    private final StoreMyPageService storeMyPageService;
    boolean isHoliday;

//    @Scheduled(cron = "0 0 0 * * *") // 매일 00시에 실행
    public void updateProducts() {
        List<StoreCheckDto> stores = storeMyPageMapper.getAllStore();
        LocalDate today = LocalDate.now();
        for (StoreCheckDto store : stores) {
            isHoliday = storeMyPageService.checkHoliday(store.getStoreId(), today.toString());
            if (isHoliday) {
                continue;
            }
            String closedAt = LocalDate.now() + " " + store.getClosedAt().toString();
            int count = store.getProductCnt();
            for (int i = 0; i < count; i++) {
                storeMyPageMapper.updateProductAuto(store.getStoreId(), closedAt);
            }
        }

    }
}
