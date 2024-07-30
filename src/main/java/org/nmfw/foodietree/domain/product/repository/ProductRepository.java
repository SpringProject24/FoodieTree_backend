package org.nmfw.foodietree.domain.product.repository;

import org.nmfw.foodietree.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
