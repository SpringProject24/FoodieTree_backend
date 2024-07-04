package org.nmfw.foodietree.domain.store.dto.resp;

import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreCheckDto {

    private String storeId;
    private String storeName;
    private LocalDateTime pickupTime; // 수정해야함 각 가개별로 픽업시간에 맞게
    private int productCnt;
    private boolean isOpen; // 가게 영업 여부
    private LocalTime closedAt; // 가게 마감 시간 - 상품 자동등록 시 디폴드값
}
