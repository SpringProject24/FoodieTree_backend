package org.nmfw.foodietree.domain.reservation.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Builder
public class PaymentCancelDto {
	private Integer amount;
	private String reason;
}
