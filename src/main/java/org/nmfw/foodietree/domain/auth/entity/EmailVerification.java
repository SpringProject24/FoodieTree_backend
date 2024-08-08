package org.nmfw.foodietree.domain.auth.entity;

import lombok.*;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeDto;
import org.nmfw.foodietree.domain.customer.entity.Customer;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "tbl_verification_code")
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = true, length = 50, unique = true)
    private String email;

//    @Column(name = "code", nullable = true, length = 255)
//    private String code;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(name = "email_verified", nullable = true)
    private Boolean emailVerified;

    @Column(name = "user_type", nullable = true, length = 50)
    private String userType;

    public EmailCodeDto toDto() {
        return EmailCodeDto.builder()
                .email(this.email)
                .expiryDate(this.expiryDate)
                .emailVerified(this.emailVerified)
                .userType(this.userType)
                .build();
    }
}