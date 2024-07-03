package org.nmfw.foodietree.domain.product.entity;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;


@Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductApproval {
    private int productId;
    private String storeId;

    @Setter
    private String proImage;

    private int productCnt;
    private String category;
    private int price;


}
