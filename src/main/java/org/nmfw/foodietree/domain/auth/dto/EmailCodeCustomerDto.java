package org.nmfw.foodietree.domain.auth.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailCodeCustomerDto {
        private String customerId;
        private String userType;
        @Setter
        private LocalDateTime refreshTokenExpireDate;
}
