package org.nmfw.foodietree.domain.review.entity;

import lombok.*;
import org.nmfw.foodietree.domain.customer.entity.Customer;
import org.nmfw.foodietree.domain.product.entity.Product;
import org.nmfw.foodietree.domain.reservation.entity.Reservation;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tbl_review")
@ToString(exclude = {"customer", "reservation", "product"})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId; // 리뷰 아이디

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "store_name", nullable = false)
    private String storeName; // 예약 상품의 상점 이름

    @Column(name = "store_img")
    private String storeImg; // 상점 이미지

    @Column(name = "review_score", nullable = false)
    private Integer reviewScore; // 별점 최대 5점

    @Column(name = "review_img")
    private String reviewImg; // 단일 리뷰 이미지

    @Column(name = "review_content", columnDefinition = "TEXT")
    private String reviewContent; // 리뷰 내용

//    @ElementCollection(targetClass = Hashtag.class)
//    @CollectionTable(name = "review_hashtags", joinColumns = @JoinColumn(name = "review_id"))
//    @Enumerated(EnumType.STRING)
//    @Column(name = "review_hashTag")
//    // 리뷰와 해시태그의 양방향 연관 관계 설정
//    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Hashtag> hashtags; // 리뷰에서 선택한 해시태그 (최소 3개 최대 제한 없음)
@OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
private List<ReviewHashtag> hashtags; // 리뷰에서 선택한 해시태그 (최소 3개 최대 제한 없음)
}

