package org.nmfw.foodietree.domain.review.entity;

import lombok.*;
import org.nmfw.foodietree.domain.product.entity.Product;
import org.nmfw.foodietree.domain.reservation.entity.Reservation;
import org.nmfw.foodietree.domain.store.entity.Store;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tbl_review")
@ToString(exclude =  "product")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "reviewId") // ID 기반으로 비교
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId; // 리뷰 아이디

    @Setter
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "reservation_id", nullable = false)
    @Column(name = "reservation_id", nullable = false)
    private Long reservationId;

    @Setter
//    @ManyToOne(fetch = FetchType.LAZY)
    //    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    @Column(name = "customer_id", nullable = false)
    private String customerId;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
//     private Customer customer;


    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "store_id", nullable = false)
    private String storeId; // 예약 상품의 상점 아이디

    @Column(name = "store_name", nullable = false)
    private String storeName; // 예약 상품의 상점 이름

    @Column(name = "store_img", nullable = true)
    private String storeImg; // 상점 이미지

    @Column(name = "address", nullable = true)
    private String address;

    @Column(name = "review_score", nullable = true)
    private Integer reviewScore; // 별점 최대 5점

    @Column(name = "review_img", nullable = true)
    private String reviewImg; // 단일 리뷰 이미지

    @Column(name = "review_content", columnDefinition = "TEXT")
    private String reviewContent; // 리뷰 내용

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewHashtag> hashtags; // 리뷰에서 선택한 해시태그 (최소 3개 최대 제한 없음)

    public Store getStore(){
        return this.product.getStore();
    }
}

