package org.nmfw.foodietree.domain.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.product.entity.Product;
import org.nmfw.foodietree.domain.product.repository.ProductRepository;
import org.nmfw.foodietree.domain.store.dto.resp.StoreCheckDto;
import org.nmfw.foodietree.domain.store.mapper.StoreMyPageMapper;
import org.nmfw.foodietree.domain.store.repository.StoreMyPageRepository;
import org.nmfw.foodietree.domain.store.repository.StoreRepository;
import org.nmfw.foodietree.domain.store.service.StoreMyPageService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductAutoUpdate {

    private final StoreMyPageService storeMyPageService;
    private final ProductRepository productRepository;
    private final StoreMyPageRepository storeMyPageRepository;
    boolean isHoliday;

//    @Scheduled(cron = "0 0 0 * * *") // 매일 00시에 실행
    public void updateProducts() {
        List<StoreCheckDto> stores = storeMyPageRepository.getAllStore();
        LocalDate today = LocalDate.now();
        for (StoreCheckDto store : stores) {
            isHoliday = storeMyPageService.checkHoliday(store.getStoreId(), today.toString());
            if (isHoliday) {
                continue;
            }
            LocalDateTime pickupDateTime = today.atTime(store.getClosedAt());
            int count = store.getProductCnt();
            for (int i = 0; i < count; i++) {
                productRepository.save(Product.builder()
                                .storeId(store.getStoreId())
                                .pickupTime(pickupDateTime)
                                .productUploadDate(LocalDateTime.now())
                        .build());
            }
        }

    }
}
