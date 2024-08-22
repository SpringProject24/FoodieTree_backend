package org.nmfw.foodietree.domain.review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.review.dto.res.ReviewSaveDto;
import org.nmfw.foodietree.domain.review.entity.Hashtag;
import org.nmfw.foodietree.domain.review.entity.Review;
import org.nmfw.foodietree.domain.review.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    /**
     *
     * @param reservationId
     * @return boolean
     *
     * 리뷰테이블에 예약번호가 있을 경우 (리뷰를 이미작성한 경우) : true
     * 리뷰 테이블에 예약번호가 없을 경우 (리뷰를 작성하지 않은 경우) : false
     */
    @GetMapping("/check/{reservationId}")
    public ResponseEntity<Boolean> checkReviewReservationId(@PathVariable Long reservationId) {
        boolean isReviewExist = reviewService.isReviewExist(reservationId);
        log.info("리뷰를 이미 작성했나요 ? {}",isReviewExist);
        return ResponseEntity.ok(isReviewExist);
    }

    /**
     *
     * @param reviewSaveDto
     * @param customerId
     * @return
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveReview(@RequestBody ReviewSaveDto reviewSaveDto
                                        , String customerId // 토ㅋ느 정보로 대체
//            , @AuthenticationPrincipal TokenUserInfo tokenUserInfo
    ) {
        // 예약 아이디로 이미 작성된 아이디 인지 확인
        boolean isReviewExist = reviewService.isReviewExist(reviewSaveDto.getReservationId());

        if (isReviewExist) {
            // 이미 리뷰가 작성된 경우
            return ResponseEntity.badRequest().body("이미 작성된 리뷰가 있습니다.");
        } else {

            // 해시태그가 최소 3개 이상인지 확인
            List<Hashtag> hashtags = reviewSaveDto.getHashtags();
            if (hashtags.size() < 3) {
                return ResponseEntity.badRequest().body("최소 3개의 해시태그를 선택해야 합니다.");
            }

            // 리뷰 저장
            Review savedReview = reviewService.saveReview(reviewSaveDto
//                    , tokenUserInfo
            );
            // 로그로 저장한 값 확인
            log.debug("Saved Review: {}", savedReview);

            // 해시태그 저장
            reviewService.saveReviewHashtags(savedReview, hashtags);

            return ResponseEntity.ok(savedReview);
        }
    }
}


