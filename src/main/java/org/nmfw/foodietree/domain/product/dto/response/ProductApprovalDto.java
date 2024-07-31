package org.nmfw.foodietree.domain.product.dto.response;

import lombok.*;
import org.nmfw.foodietree.domain.product.entity.ProductApproval;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;
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
    private StoreCategory category;
    private String price;
    private int productCnt;

    public ProductApproval toEntity(){

        return ProductApproval.builder()
//                .id(this.storeId)
//                .category(this.category)
                .price(Integer.parseInt(this.price))
//                .amount(this.productCnt)
                .build();
    }

}
