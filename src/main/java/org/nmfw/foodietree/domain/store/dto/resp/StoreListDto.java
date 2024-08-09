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
    private Integer price;
    private String storeImg;
    private Integer productCnt;
    private LocalTime openAt;
    private LocalTime closedAt;
    private LocalDateTime limitTime;
    private Boolean emailVerified;

    public static StoreListDto fromEntity(Store store) {

        return StoreListDto.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .category(store.getCategory().getFoodType())
                .address(store.getAddress())
                .price(store.getPrice())
                .storeImg(store.getStoreImg())
                .productCnt(store.getProductCnt())
                .openAt(store.getOpenAt())
                .closedAt(store.getClosedAt())
                .limitTime(store.getLimitTime())
                .emailVerified(store.getEmailVerified())
                .build();

    }
}
