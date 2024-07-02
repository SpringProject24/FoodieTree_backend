package org.nmfw.foodietree.domain.store.dto.resp;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Builder
public class StoreReservationDto {

    private String customerId;
    private String nickname;
    private String customerPhoneNumber;
    private int productId;
    private int reservationId;
    private LocalDateTime reservationTime;
    private LocalDateTime cancelReservationAt;
    private LocalDateTime pickedUpAt;
    private LocalDateTime pickupTime;
    private LocalDateTime productUploadDate;
    private int price;
}
