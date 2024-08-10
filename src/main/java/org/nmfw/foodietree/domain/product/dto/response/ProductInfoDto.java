package org.nmfw.foodietree.domain.product.dto.response;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Builder
public class ProductInfoDto {
    private LocalDateTime pickupTime;
    private LocalDateTime productUploadDate;
//    private LocalDateTime cancelByStore;

    private LocalDateTime reservationTime;
    private LocalDateTime cancelReservationAt;
    private LocalDateTime pickedUpAt;
}
