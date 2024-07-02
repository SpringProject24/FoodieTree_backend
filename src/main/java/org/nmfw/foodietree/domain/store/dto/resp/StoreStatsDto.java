package org.nmfw.foodietree.domain.store.dto.resp;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class StoreStatsDto {
    private int total;
    private double coTwo;
    private int customerCnt;
}
