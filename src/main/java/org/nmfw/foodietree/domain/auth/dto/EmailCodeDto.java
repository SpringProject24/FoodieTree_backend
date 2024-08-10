package org.nmfw.foodietree.domain.auth.dto;

import lombok.*;
import org.nmfw.foodietree.domain.auth.entity.EmailVerification;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailCodeDto{
    private String email; //nullable
    private LocalDateTime expiryDate; // 인증번호 만료기간
    private Boolean emailVerified;
    private String userType;
    private String storeApprove;

}