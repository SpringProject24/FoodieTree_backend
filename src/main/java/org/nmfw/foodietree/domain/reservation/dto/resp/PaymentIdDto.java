package org.nmfw.foodietree.domain.reservation.dto.resp;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentIdDto {

    private String paymentId;
    private Integer price;
}
