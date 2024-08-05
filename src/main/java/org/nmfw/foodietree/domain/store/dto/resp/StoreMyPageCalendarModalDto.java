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
public class StoreMyPageCalendarModalDto {
    private LocalDateTime pickupTime; // 픽업 시간 (마감시간)
    private LocalTime closedAt; // 가게 마감 시간 - 상품 자동등록 시 디폴드값
    private LocalTime openAt; // 가게 오픈 시간
    private int productCnt; // 가게에서 초기에 지정한 상품의 수
    private LocalDateTime cancelByStoreAt; // 가게에서 문을 닫겠다고 당일에 지정했을 때
    private int todayProductCnt; // 오늘 등록된 상품의 수
    private int todayPickedUpCnt; // 오늘 픽업된 상품의 수

    // QueryDSL에서 사용할 생성자
    public StoreMyPageCalendarModalDto(LocalDateTime pickupTime, LocalTime openAt, LocalTime closedAt, int productCnt) {
        this.pickupTime = pickupTime;
        this.openAt = openAt;
        this.closedAt = closedAt;
        this.productCnt = productCnt;
    }
}
