package org.nmfw.foodietree.domain.store.openapi;

import lombok.*;

import java.util.List;

@Getter @Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenseResDto {

    private String status_code;
    private int match_cnt;
    private int request_cnt;
    private List<LicenseDto> data;

}
