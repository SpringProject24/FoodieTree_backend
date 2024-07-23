package org.nmfw.foodietree.domain.store.entity;

import lombok.*;
import org.nmfw.foodietree.domain.store.entity.value.StoreApproveStatus;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@ToString
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id", nullable = false)
    private String storeId; // 가게 아이디

    @Column(name = "password", nullable = false)
    private String password; // 비밀번호

    @Column(name = "category", nullable = false)
    private String category; // 업종 카테고리

    @Column(name = "address")
    private String address; // 가게 주소

    @Column(name = "approve")
    private String approve; // 승인여부

    @Column(name = "warning_count")
    private int warningCount; // 경고 누적 횟수

    @Column(name = "price")
    private int price; // 가격

    @Column(name = "product_cnt")
    private int productCnt; // 상품갯수

    @Column(name = "business_number")
    private String businessNumber; // 사업 연락처

    @Column(name = "store_name")
    private String storeName; // 가게 이름

    @Column(name = "store_image")
    private String storeImage; // 가게 사진

    @Column(name = "store_license_number")
    private String storeLicenseNumber; // 사업자번호

    @Column(name = "open_at")
    private LocalTime openAt; // 오픈시간

    @Column(name = "closed_at")
    private LocalTime closedAt; // 마감시간

    @Column(name = "limit_time")
    private LocalDateTime limitTime; // 제한시간
}
