package org.nmfw.foodietree.domain.customer.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDetail {
    //tbl_reservation
    private int reservationId;
    private String customerId;
    private String productId;
    private LocalDateTime reservationTime;
    private LocalDateTime cancelReservationAt;
    private LocalDateTime pickedUpAt;
    // tbl_product
    private String storeId;
    private LocalDateTime pickupTime;
    // tbl_store
    private String storeName;
    private String category;
    private String address;
    private int price;
    private String storeImg;
    //tbl_customer
    private String nickname;
}
