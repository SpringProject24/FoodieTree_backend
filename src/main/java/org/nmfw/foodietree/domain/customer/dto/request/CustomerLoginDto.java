package org.nmfw.foodietree.domain.customer.dto.request;

import lombok.*;

@Setter @Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class CustomerLoginDto {

    private String customerId;
    private String customerPassword;
    private boolean autoLogin; //자동로그인체크여부

}
