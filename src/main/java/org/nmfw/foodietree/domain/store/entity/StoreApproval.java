package org.nmfw.foodietree.domain.store.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.nmfw.foodietree.domain.store.entity.value.ApproveStatus;
import org.nmfw.foodietree.domain.store.entity.value.CategoryConverter;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter
@ToString
//        (exclude = "store")
@EqualsAndHashCode(of="id")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name="tbl_store_approval")
public class StoreApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_approval_id")
    private Long id; // 가게 등록 요청 PK

    @Column(name="store_approval_license", nullable = false)
    private String license; // 가게 사업자등록번호

    @Column(name="store_approval_name", nullable = false)
    private String name; // 가게 상호명

    @Column(name="store_approval_address", nullable = false)
    private String address; // 가게 주소

    @Column(name = "store_approval_contact", nullable = false)
    private String contact; // 가게 연락처

    @Convert(converter = CategoryConverter.class)
    @Column(name = "store_approval_category", nullable = false)
    private StoreCategory category; // 업종

    @Enumerated(EnumType.STRING)
    @Column(name = "store_approval_status", nullable = false)
    @Builder.Default       // 등록 요청 상태
    private ApproveStatus status = ApproveStatus.PENDING;

    @Setter
    @Column(name = "store_approval_image")
    private String proImage; // 상품 이미지 경로

    @Column(name = "store_approval_amount")
    private Integer productCnt;  // 상품 수량

    @Column(name = "store_approval_price")
    private Integer price;  // 상품 가격

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt; // 생성시간

    @UpdateTimestamp
    private LocalDateTime updatedAt; // 수정시간

    @Column(name = "store_approval_email", nullable = false)
    private String storeId; // 가게 회원의 이메일

    @Enumerated(EnumType.STRING)
    @Column(name = "license_verification") // 사업자등록번호 검증 상태
    @Builder.Default
    private ApproveStatus licenseVerification = ApproveStatus.PENDING;

    // StoreApproval 승인되면 Store setter 후 리턴
    public Store updateFromStoreApproval(Store store) {
        store.setCategory(category);
        store.setAddress(address);
        store.setApprove(status);
        store.setStoreContact(contact);
        store.setStoreName(name);
        store.setStoreLicenseNumber(license);
        store.setProductCnt(productCnt);
        store.setPrice(price);
        store.setApprove(ApproveStatus.APPROVED);
        return store;
    }

}
