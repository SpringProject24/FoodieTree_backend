package org.nmfw.foodietree.domain.store.dto.resp;

import lombok.*;

import java.time.LocalDateTime;

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
    private LocalDateTime pickupTime = LocalDateTime.now().plusHours(5L); // 수정해야함 각 가개별로 픽업시간에 맞게
    private int productCnt;
}
