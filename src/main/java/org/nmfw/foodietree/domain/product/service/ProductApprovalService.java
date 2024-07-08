package org.nmfw.foodietree.domain.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.product.dto.response.ProductApprovalDto;
import org.nmfw.foodietree.domain.product.entity.ProductApproval;
import org.nmfw.foodietree.domain.product.mapper.ProductApprovalMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductApprovalService {

    private final ProductApprovalMapper productApprovalMapper;

    public boolean productColumnApproval(ProductApprovalDto dto, String profilePath) {
        // dto를 엔터티로 변환
        ProductApproval productApproval = dto.toEntity();
        productApproval.setProImage(profilePath); // 상품 사진 경로 엔터티로 설정



        return productApprovalMapper.productColumn(productApproval);
    }

    public boolean storeColumnApproval(ProductApprovalDto productDto) {
        ProductApproval productApproval = productDto.toEntity();

        return productApprovalMapper.storeColumn(productApproval);
    }
}
