package org.nmfw.foodietree.domain.customer.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.resp.PreferredFoodDto;
import org.nmfw.foodietree.domain.product.dto.response.ProductDto;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.nmfw.foodietree.domain.product.entity.QProduct.product;
import static org.nmfw.foodietree.domain.store.entity.QStore.store;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FavFoodRepositoryCustomImpl implements FavFoodRepositoryCustom {

    private final JPAQueryFactory factory;

    @Override
    public List<ProductDto> findCategoryByFood(List<StoreCategory> preferredFood) {
        return factory
                .select(Projections.bean(ProductDto.class,
                        store.storeId,
                        product.productId.stringValue().as("productId"),
                        product.pickupTime,
                        product.productUploadDate,
                        store.storeImg,
                        store.storeName,
                        store.category.stringValue().as("category"),
                        store.address,
                        store.price,
                        store.productCnt,
                        store.productImg.as("proImg")
                ))
                .from(product)
                .join(product.store, store)
                .where(store.category.in(preferredFood))
                .groupBy(store.storeName)
                .fetch();
    }
    /*
        <!-- 선호 음식 카테고리별 제품 조회 -->
    <select id="findCategoryByFood" resultType="org.nmfw.foodietree.domain.product.dto.response.ProductDto">
        SELECT
        s.store_id,
        s.store_name,
        s.price,
        s.store_img,
        s.category,
        p.product_id,
        p.pickup_time
        FROM
        tbl_product p
        JOIN
        tbl_store s
        ON
        p.store_id = s.store_id
        WHERE
        s.category IN
        <foreach collection="preferredFood" item="arr" open="(" close=")" separator=",">
            #{arr.preferredFood}
        </foreach>
        GROUP BY
        s.store_name;
    </select>
     */
}
