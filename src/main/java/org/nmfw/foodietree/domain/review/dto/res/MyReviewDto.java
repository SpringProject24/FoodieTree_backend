package org.nmfw.foodietree.domain.review.dto.res;

import lombok.*;
import org.nmfw.foodietree.domain.review.entity.Hashtag;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyReviewDto {
    private Long reservationId; // 예약 관리
    private String customerId; // 구매한 customer Id
    private String profileImage; // 구매자 플필 사진
    private String storeId; // store Id email
    private String storeName; // store 이름
    private String storeImg; // 상품 상점 사진
    private StoreCategory category; // 상점 카테고리
    private String address; // 상점 주소
    private int price; // 상점 상품 가격
    private LocalDateTime pickedUpAt; // 상품 픽업 일시
    private Long reviewId; // 리뷰 테이블 아이디
    private Integer reviewScore; // 별점
    private String reviewImg; // 상품 구매 사진
    private String reviewContent; // 리뷰 내용
    private List<Hashtag> hashtags; // 최소 해시태그 3개 선택 이상



}

