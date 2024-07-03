package org.nmfw.foodietree.domain.product.Service;

import lombok.RequiredArgsConstructor;
import org.nmfw.foodietree.domain.store.dto.resp.StoreCheckDto;
import org.nmfw.foodietree.domain.store.mapper.StoreMyPageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductAutoUpdate {

    private final StoreMyPageMapper storeMyPageMapper;

//    @Scheduled(cron = "0 0 0 * * *") // 매일 00시에 실행
//    @Scheduled(cron = "0 8 9 * * *") // 매일 9시 8분에 실행 테스트용
    public void updateProducts() {
        List<StoreCheckDto> stores = storeMyPageMapper.getAllStore();

        boolean isOpen = false;

        if (isOpen){
            for (StoreCheckDto store : stores) {
                int count = store.getProductCnt();
                for (int i = 0; i < count; i++) {
                    storeMyPageMapper.updateProductAuto(store.getStoreId(), store.getPickupTime().toString());
                }
            }
        }

    }
}
