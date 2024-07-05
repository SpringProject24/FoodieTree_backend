package org.nmfw.foodietree.domain.reservation.dto.resp;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class ReservationStatusDto {
    private LocalDateTime reservationTime;
    private LocalDateTime pickupTime;
    private LocalDateTime cancelReservationAt;
    private LocalDateTime pickedUpAt;
}
