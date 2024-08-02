package org.nmfw.foodietree.domain.product.repository;

import org.nmfw.foodietree.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 모든 제품 정보 조회
    List<Product> findAll();

    // 선호 음식 카테고리별 제품 조회
    List<Product> findByStoreCategoryIn(List<String> preferredFood);

    // 선호 지역별 제품 조회
    List<Product> findByStoreAddressIn(List<String> preferredAreas);

    // 선호 가게별 제품 조회 (LIKE)
    List<Product> findByStoreIdIn(List<String> storeIds);

    // 제품 ID로 조회
    Product findByProductId(Long productId);
}
