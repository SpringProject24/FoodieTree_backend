package org.nmfw.foodietree.domain.auth.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.nmfw.foodietree.domain.customer.entity.Customer;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "tbl_verification_code")
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = true, length = 50, unique = true)
    private String email;

    @Column(name = "code", nullable = true, length = 255)
    private String code;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(name = "email_verified", nullable = true)
    private boolean emailVerified;

    @Column(name = "user_type", nullable = true, length = 50)
    private String userType;


    @Builder
    public EmailVerification(Long id, String email, String code, LocalDateTime expiryDate, boolean emailVerified, String userType) {

        this.id = id;
        this.email = email;
        this.code = code;
        this.expiryDate = expiryDate;
        this.emailVerified = emailVerified;
        this.userType = userType;
    }
}