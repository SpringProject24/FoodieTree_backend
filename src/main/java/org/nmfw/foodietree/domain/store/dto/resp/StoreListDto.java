package org.nmfw.foodietree.domain.store.dto.resp;

import lombok.Builder;
import lombok.Getter;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
public class StoreListDto {
    private String storeId;
    private String storeName;
    private String category;
    private String address;
    private int price;
    private String storeImage;
    private int productCnt;
    private LocalTime openAt;
    private LocalTime closedAt;
    private LocalDateTime limitTime;

    public static StoreListDto fromEntity(Store store) {
//        StoreCategory storeCategory = StoreCategory.fromString(String.valueOf(store.getCategory()));


        return StoreListDto.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .category(store.getCategory())
                .address(store.getAddress())
                .price(store.getPrice())
                .storeImage(store.getStoreImage())
                .productCnt(store.getProductCnt())
                .openAt(store.getOpenAt())
                .closedAt(store.getClosedAt())
                .limitTime(store.getLimitTime())
                .build();

    }
}
