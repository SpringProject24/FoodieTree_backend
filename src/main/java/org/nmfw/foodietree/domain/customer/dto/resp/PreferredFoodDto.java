package org.nmfw.foodietree.domain.customer.dto.resp;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.entity.value.PreferredFoodCategory;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Builder
public class PreferredFoodDto {
//    private PreferredFoodCategory foodCategoryName;
    private String foodImage;
    private String preferredFood;
}
