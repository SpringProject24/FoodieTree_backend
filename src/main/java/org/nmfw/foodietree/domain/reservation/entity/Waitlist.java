package org.nmfw.foodietree.domain.reservation.entity;

import lombok.*;
import org.eclipse.jdt.internal.compiler.ast.FalseLiteral;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "waitId")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_waitlist")
// 예약 대기
public class Waitlist {

    @Id // auto increment
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wait_id")
    private Long waitId;            // 대기 id

    @Column(name = "customer_id")
    private String customerId;      // 고객 이메일 계정

    @Column(name = "store_id")
    private String storeId;         // 가게 이메일 계정

    @CreationTimestamp
    @Column(name = "wait_time")
    private LocalDateTime waitTime; // 대기 신청시간

    @Size(max = 1)
    @Column(name = "wait_confirm")
    private String waitConfirm;     //

    @Column(name="cancel_wait_at")
    private LocalDateTime cancelWaitAt; // 예약 대기 취소시간


}
