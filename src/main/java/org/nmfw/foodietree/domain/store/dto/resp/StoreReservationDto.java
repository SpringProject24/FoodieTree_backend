package org.nmfw.foodietree.domain.store.dto.resp;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.reservation.entity.ReservationStatus;

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
    private String profileImage;
    private String nickname;
    private String customerPhoneNumber;
    private Long productId;
    private Long reservationId;
    private LocalDateTime reservationTime;
    private LocalDateTime cancelReservationAt;
    private LocalDateTime pickedUpAt;
    private LocalDateTime pickupTime;
//    private LocalDateTime pickupStartTime;
//    private LocalDateTime pickupEndTime;
    private LocalDateTime productUploadDate;
    private int price;
    private ReservationStatus status;

    private String reservationTimeF;
    private String cancelReservationAtF;
    private String pickedUpAtF;
    private String pickupTimeF;
    private String productUploadDateF;
}
