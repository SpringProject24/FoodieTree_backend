package org.nmfw.foodietree.domain.store.dto.resp;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Builder
public class StoreHolidayDto {
    private String storeId;
    private String holidays;

    public StoreHolidayDto(String storeId, LocalDate holidays) {
        this.storeId = storeId;
        this.holidays = holidays.toString();
    }
}
