//package org.nmfw.foodietree.domain.product.entity;
//
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//import org.nmfw.foodietree.domain.store.entity.value.ApproveStatus;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//
//
//@Getter
//@ToString
//@EqualsAndHashCode
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//
//@Entity
//@Table(name="tbl_product_approval")
//public class ProductApproval {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "product_approval_id")
//    private Long id; // 상품 등록 요청 PK
//
//    @Setter
//    @Column(name = "product_approval_image", nullable = false)
//    private String proImage; // 상품 이미지 경로
//
//    @Column(name = "product_approval_amount", nullable = false)
//    private Integer productCnt;  // 상품 수량
//    @Column(name = "product_approval_price", nullable = false)
//    private Integer price;  // 상품 가격
//
//    @CreationTimestamp
//    @Column(updatable = false)
//    private LocalDateTime createdAt;
//
//    @UpdateTimestamp
//    private LocalDateTime updatedAt;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false, name = "product_approval_status")
//    @Builder.Default
//    private ApproveStatus status = ApproveStatus.PENDING; // 상품 요청시 PENDING
//
//    @Column(name = "product_approval_email", nullable = false)
//    private String storeId; // 상품 요청 가게 회원의 이메일
//
//
//    // 상품 등록 시 가게 정보 필요 (category 등 정보를 갖고 있음)
//    // 가게당 상품은 하나지만 요청은 여러 번 가능
////    @ManyToOne(fetch = FetchType.LAZY)
////    @JoinColumn(name="idx_store_id")
////    private Store store;
//
//
//}
