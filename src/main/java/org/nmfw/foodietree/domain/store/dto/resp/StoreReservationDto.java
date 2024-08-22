package org.nmfw.foodietree.domain.store.dto.resp;

import lombok.*;
import org.nmfw.foodietree.domain.reservation.entity.ReservationStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
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

    private String paymentId;
    private LocalDateTime paymentTime;

    public StoreReservationDto(String customerId, String profileImage, String nickname, String customerPhoneNumber, Long productId,
                               Long reservationId, LocalDateTime reservationTime, LocalDateTime cancelReservationAt, LocalDateTime pickedUpAt,
                               LocalDateTime pickupTime, LocalDateTime productUploadDate, int price) {
        this.customerId = customerId;
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.customerPhoneNumber = customerPhoneNumber;
        this.productId = productId;
        this.reservationId = reservationId;
        this.reservationTime = reservationTime;
        this.cancelReservationAt = cancelReservationAt;
        this.pickedUpAt = pickedUpAt;
        this.pickupTime = pickupTime;
        this.productUploadDate = productUploadDate;
        this.price = price;
    }
}
