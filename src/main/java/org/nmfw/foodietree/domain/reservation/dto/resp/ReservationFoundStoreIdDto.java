package org.nmfw.foodietree.domain.reservation.dto.resp;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationFoundStoreIdDto {
    private String storeId;
    private Long productId;
//    private Long secLeft;
}
