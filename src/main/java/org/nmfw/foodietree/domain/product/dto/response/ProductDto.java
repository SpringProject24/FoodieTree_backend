package org.nmfw.foodietree.domain.product.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
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

    // Constructor to copy another ProductDto instance
    public ProductDto(ProductDto p) {
        this.storeId = p.storeId;
        this.productId = p.productId;
        this.productUploadDate = p.productUploadDate;
        this.storeImg = p.storeImg;
        this.storeName = p.storeName;
        this.category = p.category;
        this.address = p.address;
        this.price = p.price;
        this.productCnt = p.productCnt;
        this.proImage = p.proImage;
        this.pickupTime = p.pickupTime;
    }

    // Method to get formatted pickup time
    public String getFormattedPickupTime() {
        if (pickupTime == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
        return pickupTime.format(formatter);
    }
}
