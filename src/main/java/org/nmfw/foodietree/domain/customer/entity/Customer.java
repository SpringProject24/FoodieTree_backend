package org.nmfw.foodietree.domain.customer.entity;

import lombok.*;
import org.nmfw.foodietree.domain.auth.entity.EmailVerification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;


@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "idxCustomerId")
@Table(name = "tbl_customer")
public class Customer {

    @Id // auto increment
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx_customer_id", nullable = false)
    private Long idxCustomerId;

    @Column(name = "customer_id", unique = true)
    private String customerId;

    @Column(name = "customer_password")
    private String customerPassword;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "customer_phone_number")
    private String customerPhoneNumber;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "limit_time")
    private LocalDateTime limitTime;

    @Column(name = "refresh_token_expire_date", nullable = true)
    private LocalDateTime refreshTokenExpireDate;

    @Column(name = "user_type", nullable = true)
    private String userType;

    @Column(name = "email_verified", nullable = true)
    private Boolean emailVerified;

}
