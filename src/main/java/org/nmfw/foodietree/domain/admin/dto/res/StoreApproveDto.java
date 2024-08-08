package org.nmfw.foodietree.domain.admin.dto.res;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.nmfw.foodietree.domain.store.entity.StoreApproval;
import org.nmfw.foodietree.domain.store.entity.value.ApproveStatus;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public class StoreApproveDto {

    private String storeId;
    private String name;
    private ApproveStatus status;
    private StoreCategory category;
    private String address;
    private String contact;
    private String license;
    private int productCnt;
    private int price;

    public StoreApproveDto(StoreApproval sa) {
        this.storeId = sa.getStoreId();
        this.name = sa.getName();
        this.status = sa.getStatus();
        this.category = sa.getCategory();
        this.address = sa.getAddress();
        this.contact = sa.getContact();
        this.license = sa.getLicense();
        this.productCnt = sa.getProductCnt();
        this.price = sa.getPrice();
    }
}
