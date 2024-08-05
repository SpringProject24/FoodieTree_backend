package org.nmfw.foodietree.domain.reservation.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_reservation")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @CreationTimestamp
    @Column(name = "reservation_time", nullable = false)
    private LocalDateTime reservationTime;

    @Setter
    @Column(name = "cancel_reservation_at")
    private LocalDateTime cancelReservationAt;

    @Setter
    @Column(name = "picked_up_at")
    private LocalDateTime pickedUpAt;

}
