package org.nmfw.foodietree.domain.store.openapi;

import lombok.*;

@Getter @Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenseDto {
    private String b_no;
    private String b_stt;
    private String b_stt_cd;
    private String tax_type;
    private String tax_type_cd;
    private String end_dt;
    private String utcc_yn;
    private String tax_type_change_dt;
    private String invoice_apply_dt;
    private String rbf_tax_type;
    private String rbf_tax_type_cd;

}
