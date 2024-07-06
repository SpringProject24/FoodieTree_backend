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



    // Method to get formatted pickup time
//    public String setFormattedPickupTime(LocalDateTime pickupTime) {
//        if (pickupTime == null) {
//            return "";
//        }
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
//        return pickupTime.format(formatter);
//    }
}
