package org.nmfw.foodietree.domain.product.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.nmfw.foodietree.domain.store.entity.Store;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString(exclude = "store")
@EqualsAndHashCode(of = "productId")
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name="tbl_product")
public class Product {

    @Id // auto increment
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId; // 상품 id

    private String productImage; // 상품 이미지

    private LocalDateTime pickupTime; // 마감시간

    @CreationTimestamp
    private LocalDateTime productUploadDate; // 상품 등록 시간

    private LocalDateTime canceledByStoreAt; // 가게에서 취소한 시간

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idx_store_id")
    private Store store;                 // 가게


}
