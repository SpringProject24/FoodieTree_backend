package org.nmfw.foodietree.domain.store.dto.request;

import lombok.*;
import org.nmfw.foodietree.domain.customer.entity.Customer;
import org.nmfw.foodietree.domain.store.entity.Store;

//로그인한 유저의 정보를 담고 있는 dto
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginStoreInfoDto {

    private String storeId;

    public LoginStoreInfoDto(Store store) {
        this.storeId = store.getStoreId();
    }
}
