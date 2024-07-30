package org.nmfw.foodietree.domain.store.dto.resp;

import lombok.*;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;

@Setter @Getter @ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreApprovalDto {

    private String storeId;
    private String storeName;
    private String address;
    private StoreCategory category;
    private String storeContact;
    private String storeLicenseNumber;

}
