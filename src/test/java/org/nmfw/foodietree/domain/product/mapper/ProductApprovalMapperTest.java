package org.nmfw.foodietree.domain.product.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.nmfw.foodietree.domain.product.entity.ProductApproval;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@Transactional
public class ProductApprovalMapperTest {

    @Autowired
    private ProductApprovalMapper productApprovalMapper;

    @Test
    void testProductColumn() {
        // Given
        Random random = new Random();
        ProductApproval productApproval = ProductApproval.builder()
                .productId(random.nextInt())
                .storeId("qwer@qwer.com")
                .proImage("test-image-path")
                .productCnt(10)
                .category("test-category")
                .price(1000)
                .build();

        // When
        boolean result = productApprovalMapper.productColumn(productApproval);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void testStoreColumn() {
        // Given
        Random random = new Random();
        ProductApproval productApproval = ProductApproval.builder()
                .productId(random.nextInt())
                .storeId("qwer@qwer.com")
                .proImage("test-image-path")
                .productCnt(10)
                .category("test-category")
                .price(1000)
                .build();

        // When
        boolean result = productApprovalMapper.storeColumn(productApproval);

        // Then
        assertThat(result).isTrue();
    }
}
