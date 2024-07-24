package org.nmfw.foodietree.domain.product.dto.response;

import lombok.*;
import org.nmfw.foodietree.domain.customer.dto.resp.PreferredFoodDto;

import java.util.List;

@Getter
@ToString
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TotalInfoDto {
    List<ProductDto> productDtoList;
    private List<String> preferredArea;
    private List<PreferredFoodDto> preferredFood;
}
