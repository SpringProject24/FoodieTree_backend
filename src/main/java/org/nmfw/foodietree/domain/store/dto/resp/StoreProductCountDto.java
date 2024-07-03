package org.nmfw.foodietree.domain.store.dto.resp;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Builder
public class StoreProductCountDto {
    private int productCnt;
    private int todayProductCnt;
    private int todayPickedUpCnt;
    private int readyToPickUpCnt;
    private int remainCnt;
}
