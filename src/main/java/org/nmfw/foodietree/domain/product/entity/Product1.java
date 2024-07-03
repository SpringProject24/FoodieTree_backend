package org.nmfw.foodietree.domain.product.entity;

import lombok.*;

import java.time.LocalDateTime;

@Setter@Getter@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Product1 {

    private String productId;
    private String storeId;
    private LocalDateTime pickupTime;

}
