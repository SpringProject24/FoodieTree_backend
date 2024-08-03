package org.nmfw.foodietree.domain.product.service;

import org.nmfw.foodietree.domain.product.entity.Product;
import org.nmfw.foodietree.domain.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // 모든 제품 정보 조회
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 선호 음식 카테고리별 제품 조회
    public List<Product> getProductsByCategory(List<String> preferredFood) {
        return productRepository.findByStoreCategoryIn(preferredFood);
    }

    // 선호 지역별 제품 조회
    public List<Product> getProductsByArea(List<String> preferredAreas) {
        return productRepository.findByStoreAddressIn(preferredAreas);
    }

    // 선호 가게별 제품 조회 (LIKE)
    public List<Product> getProductsByStoreIds(List<String> storeIds) {
        return productRepository.findByStoreIdIn(storeIds);
    }

    // 제품 ID로 조회
    public Product getProductById(Integer productId) {
        return productRepository.findByProductId(productId);
    }
}
