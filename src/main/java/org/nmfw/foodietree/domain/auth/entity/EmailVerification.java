package org.nmfw.foodietree.domain.auth.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "tbl_verification_code")
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = true, length = 50)
    private String customerId;

    @Column(name = "store_id", nullable = true, length = 50)
    private String storeId;

    @Column(name = "code", nullable = true, length = 255)
    private String code;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(name = "email_verified", nullable = true)
    private boolean emailVerified;

    @Column(name = "user_type", nullable = true, length = 50)
    private String userType;

    @Builder
    public EmailVerification(long id, String customerId, String storeId, String code, LocalDateTime expiryDate, boolean emailVerified, String userType) {
        this.id = id;
        this.customerId = customerId;
        this.storeId = storeId;
        this.code = code;
        this.expiryDate = expiryDate;
        this.emailVerified = emailVerified;
        this.userType = userType;
    }
}