package org.nmfw.foodietree.domain.customer.dto.resp;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.entity.value.PreferredFoodCategory;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Builder
public class CustomerMyPageDto {
    private String customerId;
    private String nickname;
    private String profileImage;
    private String customerPhoneNumber;
    private List<String> preferredArea;
    private List<PreferredFoodDto> preferredFood;
    private List<CustomerFavStoreDto> favStore;
}

