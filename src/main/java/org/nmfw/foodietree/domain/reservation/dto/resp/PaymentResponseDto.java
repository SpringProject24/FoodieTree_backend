package org.nmfw.foodietree.domain.reservation.dto.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter @ToString
public class PaymentResponseDto {
    private String status;
    @JsonProperty("id")
    private String paymentId;
    private Instant paidAt;
    private Amount amount;

    @Getter
    public static class Amount {
        public Integer total;
        public Integer paid;
        public Integer cancelled;
    }
}
