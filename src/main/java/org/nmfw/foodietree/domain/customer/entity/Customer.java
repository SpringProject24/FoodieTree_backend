package org.nmfw.foodietree.domain.customer.entity;

import lombok.*;
import org.nmfw.foodietree.domain.auth.entity.EmailVerification;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "tbl_customer")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx_customer_id")
    private Long id;

    @Column(name = "customer_id", nullable = false, unique = true)
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

    @Column(name = "refresh_token_expire_date")
    private LocalDateTime refreshTokenExpireDate;

    @Column(name = "user_type")
    private String userType;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmailVerification> emailVerifications;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavFood> favFoods;
}
