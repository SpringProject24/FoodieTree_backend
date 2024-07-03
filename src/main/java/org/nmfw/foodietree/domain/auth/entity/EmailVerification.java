package org.nmfw.foodietree.domain.auth.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EmailVerification {
    private int id;
    private String customerId;
    private String code;
    private LocalDateTime expiryDate;
}
