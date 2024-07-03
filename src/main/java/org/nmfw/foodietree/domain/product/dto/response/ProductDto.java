package org.nmfw.foodietree.domain.product.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter@ToString @Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private String storeId;
    private String productId;
    private LocalDateTime pickupTime;
    private LocalDateTime productUploadDate;
    private String storeImg;
    private String storeName;
    private String category;
    private String address;
    private int price;
    private int productCnt;
    private String proImage;
}
