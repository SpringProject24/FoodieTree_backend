package org.nmfw.foodietree.domain.reservation.dto.resp;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.reservation.entity.ReservationStatus;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Builder
public class ReservationDetailDto {

    private Long reservationId;
    private Long productId;
    private String customerId;
    private LocalDateTime reservationTime; // 고객이 예약한 시간
    private LocalDateTime cancelReservationAt; // 고객이 얘약을 취소한 시간 null 가능, 값이 존재한다면 예약취소 된 것
    private LocalDateTime pickedUpAt; // 고객이 픽업한 시간
    private String storeId;
    private LocalDateTime pickupTime; // 가게에서 지정한 픽업시간
//    private LocalDateTime pickupStartTime; // 가게에서 지정한 픽업가능 시작시간
//    private LocalDateTime pickupEndTime; // 가게에서 지정한 픽업가능 마감시간
    private String storeName;
    private StoreCategory category;
    private String address;
    private int price;
    private String storeImg;
    private String nickname;
    private String profileImage;
    private ReservationStatus status;

    // 시간 관련 필드 포멧팅
    private String reservationTimeF;
    private String cancelReservationAtF;
    private String pickedUpAtF;
    private String pickupTimeF;
    private String pickupStartTimeF;
    private String pickupEndTimeF;

    public void setCategory(String category) {
        this.category = StoreCategory.fromString(category);
    }

    // 명시적으로 모든 매개변수를 포함하는 생성자 추가
    public ReservationDetailDto(long reservationId, long productId, String customerId, LocalDateTime reservationTime,
                                LocalDateTime cancelReservationAt, LocalDateTime pickedUpAt, String storeId,
                                LocalDateTime pickupTime, String storeName, StoreCategory category, String address, int price,
                                String storeImg, String nickname, String profileImage) {
        this.reservationId = reservationId;
        this.productId = productId;
        this.customerId = customerId;
        this.reservationTime = reservationTime;
        this.cancelReservationAt = cancelReservationAt;
        this.pickedUpAt = pickedUpAt;
        this.storeId = storeId;
        this.pickupTime = pickupTime;
        this.storeName = storeName;
        this.category = category;
        this.address = address;
        this.price = price;
        this.storeImg = storeImg;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    // 포멧팅 함수
    public void formatTimes() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월dd일 HH시mm분");
        if (reservationTime != null) {
            this.reservationTimeF = reservationTime.format(formatter);
        }
        if (cancelReservationAt != null) {
            this.cancelReservationAtF = cancelReservationAt.format(formatter);
        }
        if (pickedUpAt != null) {
            this.pickedUpAtF = pickedUpAt.format(formatter);
        }
        if (pickupTime != null) {
            this.pickupTimeF = pickupTime.format(formatter);
        }
//        if (pickupStartTime != null) {
//            this.pickupStartTimeF = pickupStartTime.format(formatter);
//        }
//        if (pickupEndTime != null) {
//            this.pickupEndTimeF = pickupEndTime.format(formatter);
//        }
    }
}
