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
public class StoreMyPageDto {

    private String storeId;
    private String storeName;
    private String storeImg;
    private String category;
    private String address;
    private String approve;
    private int price;
    private int productCnt;
//    private <StoreReservationDto> storeReservationDto;
}
