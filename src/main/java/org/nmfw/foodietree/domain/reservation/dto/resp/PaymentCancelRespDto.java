package org.nmfw.foodietree.domain.reservation.dto.resp;

import java.time.Instant;
import lombok.Getter;
import lombok.ToString;

@Getter @ToString
public class PaymentCancelRespDto {
    private Cancellation cancellation;

    @Getter @ToString
    public static class Cancellation {
        public String status;
        public String id;
        public Integer totalAmount;
        public String reason;
        public Instant cancelledAt;
    }
}
