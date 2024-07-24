package org.nmfw.foodietree.domain.product.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private String storeId;
    private String productId;
    private LocalDateTime productUploadDate;
    private String storeImg;
    private String storeName;
    private String category;
    private String address;
    private int price;
    private int productCnt;
    private String proImage;
    @Setter
    private LocalDateTime pickupTime;
    private String formattedPickupTime;

}
