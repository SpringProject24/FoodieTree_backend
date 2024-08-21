package org.nmfw.foodietree.domain.reservation.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Subselect(
    "select "
	+ "    r.*, "
	+ "    ROW_NUMBER() over (PARTITION BY r.product_id order by r.reservation_time desc) as row_num "
	+ "from tbl_reservation r"
)

@Entity
@Immutable
@Synchronize("tbl_reservation")
public class ReservationSubSelect {

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

    @Setter
    @Column(name = "payment_id")
    private String paymentId;

    @Setter
    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    @Setter
    @Column(name = "cancel_payment_at")
    private LocalDateTime cancelPaymentAt;

	@Column(name = "row_num")
	private Long rowNum;
}
