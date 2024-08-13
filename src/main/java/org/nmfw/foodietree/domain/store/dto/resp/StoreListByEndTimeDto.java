package org.nmfw.foodietree.domain.store.dto.resp;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.nmfw.foodietree.domain.store.entity.Store;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class StoreListByEndTimeDto {
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
    private String productImg;
    private LocalDateTime pickupStartTime;
    private LocalDateTime pickupEndTime;
    private LocalDateTime productUploadDate;
    private String cancelByStore;
    private String remainingTime; // 마감까지 남은 시간

}
