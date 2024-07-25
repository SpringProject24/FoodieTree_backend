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
    private String customerId; //nullable
    private String storeId; //nullable
    private String code; //추후 refresh token 도입예정
    @Setter
    private LocalDateTime expiryDate;
    @Setter
    private boolean emailVerified; //email verified 대신 refresh token 으로 대체하기


    public EmailVerification toEntity() {
        return EmailVerification.builder()
                .customerId(this.customerId)
                .storeId(this.storeId)
                .code(this.code)
                .expiryDate(this.expiryDate)
                .emailVerified(this.emailVerified)
                .build();
    }

}
