package org.nmfw.foodietree.domain.product.dto.response;

import lombok.*;
import org.nmfw.foodietree.domain.product.entity.ProductApproval;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Random;
import java.util.UUID;

@Setter @Getter @ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductApprovalDto {

    private int productId;
    private String storeId;
    private MultipartFile proImage;
    private String category;
    private int price;
    private int productCnt;

    public ProductApproval toEntity(){

        Random random = new Random();

        return ProductApproval.builder()
                .storeId(this.storeId)
                .category(this.category)
                .price(this.price)
                .productCnt(this.productCnt)
                .build();
    }

}
