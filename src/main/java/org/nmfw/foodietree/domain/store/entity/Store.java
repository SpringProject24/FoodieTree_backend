package org.nmfw.foodietree.domain.store.entity;

import lombok.*;
import org.nmfw.foodietree.domain.store.entity.value.StoreApproveStatus;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;

@Getter @ToString
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store {
    private String storeId;
    @Setter
    private String password;
    private String category;
    private String address;
    private StoreApproveStatus approve;
    private int warningCount;
    private int price;
    private int productCnt;
    private String businessNumber;
    private String storeName;
    private String storeImage;
    private String storeLicenseNumber;
}