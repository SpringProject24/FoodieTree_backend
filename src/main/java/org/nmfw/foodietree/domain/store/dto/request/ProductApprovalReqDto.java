package org.nmfw.foodietree.domain.store.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.Range;
//import org.nmfw.foodietree.domain.product.entity.ProductApproval;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder

// 상품 등록 요청 DTO
public class ProductApprovalReqDto {

    @Value("${file.upload.root-path}")
    private String rootPath;

    @NotNull
    private int price;

    @NotNull
    @Range(min=1, max=50)
    private int productCnt;

    @Setter
    private MultipartFile productImage;

    private String storeId; // 임시로 테스트를 위해 추가
}
