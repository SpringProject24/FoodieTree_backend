package org.nmfw.foodietree.domain.reservation.dto.resp;

import lombok.*;
import org.nmfw.foodietree.domain.customer.entity.value.PickUpStatus;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationModalDetailDto {

    private int reservationId;
    private int productId;
    private String customerId;
    private LocalDateTime reservationTime; // 고객이 예약한 시간
    private LocalDateTime cancelReservationAt; // 고객이 얘약을 취소한 시간 null 가능, 값이 존재한다면 예약취소 된 것
    private LocalDateTime pickedUpAt; // 고객이 픽업한 시간
    private String storeId;
    private LocalDateTime pickupTime; // 가게에서 지정한 픽업가능 시간
    private String storeName;
    private String category;
    private String address;
    private int price;
    private String storeImg;
    private String nickname;
    private String profileImage;
    private PickUpStatus status; // 고객이 예약을 픽업했는지, 픽업대기중인지, 픽업취소 했는지

    private String  reservationTimeF; // 고객이 예약한 시간
    private String cancelReservationAtF; // 고객이 얘약을 취소한 시간 null 가능, 값이 존재한다면 예약취소 된 것
    private String pickedUpAtF; // 고객이 픽업한 시간
    private String pickupTimeF; // 가게에서 지정한 픽업가능 시간
}
