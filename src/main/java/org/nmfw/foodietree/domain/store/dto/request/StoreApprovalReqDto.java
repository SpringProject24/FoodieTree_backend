package org.nmfw.foodietree.domain.store.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.nmfw.foodietree.domain.store.entity.StoreApproval;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;

import javax.validation.constraints.NotBlank;

@Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreApprovalReqDto {

    @JsonProperty(value = "bizLicenseNum")
    @NotBlank
    private String storeLicenseNumber;

    @JsonProperty(value = "bizName")
    @NotBlank
    private String storeName;

    @JsonProperty(value = "bizAddress")
    @NotBlank
    private String address;

    @JsonProperty(value = "bizPhoneNum")
    @NotBlank
    private String storeContact; // businessNumber?

    @JsonProperty(value = "bizCategory")
    @NotBlank
    private String category;


    public StoreApproval toEntity() {

        return  StoreApproval.builder()
                .license(storeLicenseNumber)
                .name(storeName)
                .address(address)
                .contact(storeContact)
                .category(StoreCategory.valueOf(category))
                .build();
    }
}
