package org.nmfw.foodietree.domain.product.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
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
    private List<String> preferredArea;
    private List<String> preferredFood;

}
