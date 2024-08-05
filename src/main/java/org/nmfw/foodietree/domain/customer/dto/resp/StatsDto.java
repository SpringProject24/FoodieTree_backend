package org.nmfw.foodietree.domain.customer.dto.resp;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Builder
public class StatsDto {

    private int total;
    private double coTwo;
    private int money;
    private String customerId;
}
