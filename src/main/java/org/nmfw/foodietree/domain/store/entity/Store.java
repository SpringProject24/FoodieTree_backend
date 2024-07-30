package org.nmfw.foodietree.domain.store.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.nmfw.foodietree.domain.product.entity.Product;
import org.nmfw.foodietree.domain.store.entity.value.ApproveStatus;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@ToString(exclude = {"products"})
@EqualsAndHashCode(of = "idxStoreId")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name="tbl_store")
public class Store {
  
    @Id // auto increment
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx_store_id", nullable = false)
    private Long idxStoreId;

    // 가게 계정 == 이메일, (유니크)
    @Column(name = "store_id", unique = true)
    private String storeId;

//    @Setter
//    @Column(name = "password", nullable = false)
//    private String password;

    @Enumerated(EnumType.STRING)
    private StoreCategory category; // 업종 카테고리

    @Enumerated(EnumType.STRING)
    private ApproveStatus approve; // 승인여부

    @Column(name = "address")
    private String address; // 가게 주소

    @Column(name = "warning_count")
    private Integer warningCount; // 경고 누적 횟수

    @Column(name = "price")
    private Integer price; // 가격

    @Column(name = "product_cnt")
    private Integer productCnt; // 상품갯수
    
    @Column(name = "store_contact")
    private String storeContact;  // 가게 연락처

    @Column(name = "store_name")
    private String storeName; // 가게 이름

    @Column(name = "store_img")
    private String storeImg; // 가게 사진

    @Column(name = "store_license_number")
    private String storeLicenseNumber; // 사업자번호

    @Column(name = "open_at")
    private LocalTime openAt; // 오픈시간

    @Column(name = "closed_at")
    private LocalTime closedAt; // 마감시간

    @Column(name = "limit_time")
    private LocalDateTime limitTime; // 제한시간

    @Column(name = "refresh_token_expire_date")
    private LocalDateTime refreshTokenExpireDate;

    @Column(name = "user_type")
    private String userType;

    @Column(name = "email_verified", nullable = true)
    private boolean emailVerified;
  
    @OneToMany(mappedBy = "store",
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}
    )
    private List<Product> products = new ArrayList<>();  // products 연관관계

    // 연관된 product 추가 메서드
    public void addProduct(Product product) {
        products.add(product);
        product.setStore(this);
    }

    // 연관된 product 제거 메서드
    public void removeProduct(Product product) {
        products.remove(product);
        product.setStore(null);
    }

  
}

