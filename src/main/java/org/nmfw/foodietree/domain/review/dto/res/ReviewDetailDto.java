package org.nmfw.foodietree.domain.review.dto.res;

import lombok.*;
import org.nmfw.foodietree.domain.review.entity.Hashtag;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDetailDto {
    private Long reservationId; // 예약 관리
    private String customerId; // 구매한 customer Id
    private String storeImg; // 상품 상점 사진
    private int reviewScore; // 별점
    private String reviewImg; // 상품 구매 사진
    private String reviewContent; // 리뷰 내용
    private List<Hashtag> hashtags; // 최소 해시태그 3개 선택 이상

    private String storeId;
    private String storeName;
    private String address;



    public ReviewDetailDto(long reservationId, String customerId, String storeImg, int reviewScore, String reviewImg, String reviewContent, String storeId, String storeName, String address) {
        this.reservationId = reservationId;
        this.customerId = customerId;
        this.storeImg = storeImg;
        this.reviewScore = reviewScore;
        this.reviewImg = reviewImg;
        this.reviewContent = reviewContent;
        this.hashtags = hashtags;
        this.storeId = storeId;
        this.storeName = storeName;
        this.address = address;
    }
//    public ReviewDetailDto(Long reservationId, String customerId, String storeImg,
//                           int reviewScore, String reviewImg, String reviewContent,
//                           Long storeId, String storeName, String address) {
//        this.reservationId = reservationId;
//        this.customerId = customerId;
//        this.storeImg = storeImg;
//        this.reviewScore = reviewScore;
//        this.reviewImg = reviewImg;
//        this.reviewContent = reviewContent;
//        this.storeId = storeId;
//        this.storeName = storeName;
//        this.address = address;
//    }
}
