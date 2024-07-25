package org.nmfw.foodietree.domain.auth.dto;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailCodeCustomerDto {
        private String customerId;
        private String role;
        private Date refreshTokenExpireDate;
}
