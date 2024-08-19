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
public class ReviewSaveDto {
    private Long reservationId; // 예약 관리
    private String customerId; // 구매한 customer Id
    private String storeImg; // 상품 상점 사진
    private Integer reviewScore; // 별점
    private String reviewImg; // 상품 구매 사진
    private String reviewContent; // 리뷰 내용
    private List<Hashtag> hashtags; // 최소 해시태그 3개 선택 이상
}
