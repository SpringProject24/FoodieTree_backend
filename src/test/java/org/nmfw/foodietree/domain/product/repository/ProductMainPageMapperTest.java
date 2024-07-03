package org.nmfw.foodietree.domain.product.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.product.dto.response.ProductDto;
import org.nmfw.foodietree.domain.product.mapper.ProductMainPageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ProductMainPageMapperTest {

    private static final Logger log = LoggerFactory.getLogger(ProductMainPageMapperTest.class);
    @Autowired
    ProductMainPageMapper productMainPageMapper;



}