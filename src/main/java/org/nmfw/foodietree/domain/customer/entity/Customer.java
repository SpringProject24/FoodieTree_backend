package org.nmfw.foodietree.domain.customer.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Customer {

    //고객 id
    private String customerId;

    //고객 비밀번호
    @Setter
    private String customerPassword;

    //고객 이름
    private String nickName;

    //고객 전화번호
    private String customerPhoneNumber;

    //고객 프로필이미지
    private String profileImage;

    //고객 세션아이디
//    @Setter
    private String sessionId;

    private LocalDateTime limitTime;

}
