package org.nmfw.foodietree.domain.review.dto.res;

import lombok.*;
import org.nmfw.foodietree.domain.review.entity.Hashtag;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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
    private String customerId; // 구매한 customer Id email
    private String storeId; // 구매한 물건의 상점 Id email
    private String storeName; // 구매한 물건의 상점 이름
    private String storeImg; // 상품 상점 사진
    private String address; // store 위치

    private Integer reviewScore; // 별점
    private String reviewImg; // 상품 구매 사진
    private String reviewContent; // 리뷰 내용
    private List<Hashtag> hashtags; // 최소 해시태그 3개 선택 이상
    private LocalDateTime paymentTime; //결제 시간 정보

}
