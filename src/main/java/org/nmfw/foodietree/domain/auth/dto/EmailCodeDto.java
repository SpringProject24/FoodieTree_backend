package org.nmfw.foodietree.domain.auth.dto;

import lombok.*;
import org.nmfw.foodietree.domain.auth.entity.EmailVerification;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailCodeDto{
    private String customerId;
    private String code;
    private LocalDateTime expiryDate;

    public EmailVerification toEntity() {
        return EmailVerification.builder()
                .customerId(this.customerId)
                .code(this.code)
                .expiryDate(this.expiryDate)
                .build();
    }
}
