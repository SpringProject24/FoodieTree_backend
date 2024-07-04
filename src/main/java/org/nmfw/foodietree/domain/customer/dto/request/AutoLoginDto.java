package org.nmfw.foodietree.domain.customer.dto.request;

import lombok.*;

import java.time.LocalDateTime;
@Setter @Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoLoginDto {

    private String sessionId; // 자동로그인 쿠키값
    private LocalDateTime limitTime; // 만료시간
    private String customerId; // 계정명

}
