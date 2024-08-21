package org.nmfw.foodietree.domain.review.dto.res;

import lombok.*;
import org.nmfw.foodietree.domain.review.entity.Hashtag;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;

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
    private String storeImg; // 상품 상점 사진
    private Integer reviewScore; // 별점
    private String reviewImg; // 상품 구매 사진
    private String reviewContent; // 리뷰 내용
    private List<Hashtag> hashtags; // 최소 해시태그 3개 선택 이상

    // ReservationDetailDto 에서 값 가져오기
    private String storeName;
    private StoreCategory category;
    private String address;
    private int price;
    private String profileImage;

}

