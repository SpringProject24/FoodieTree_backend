package org.nmfw.foodietree.domain.store.dto.resp;

import lombok.*;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class StoreListCo2Dto {
    private String storeId;
    private String storeName;
    private String category;
    private String address;
    private Integer price;
    private String storeImg;
    @Setter
    private Integer productCnt;
    private LocalTime openAt;
    private LocalTime closedAt;
    private LocalDateTime limitTime;
    private Boolean emailVerified;
    private String productImg;
    private double coTwo;
}
