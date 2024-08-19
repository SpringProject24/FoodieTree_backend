package org.nmfw.foodietree.domain.review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.security.TokenProvider.TokenUserInfo;
import org.nmfw.foodietree.domain.review.dto.res.ReviewSaveDto;
import org.nmfw.foodietree.domain.review.entity.Review;
import org.nmfw.foodietree.domain.review.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/save")
    public ResponseEntity<?> saveReview(@RequestBody ReviewSaveDto reviewSaveDto,
                                        @AuthenticationPrincipal TokenUserInfo tokenUserInfo) {
        // 예약 아이디와 고객 아이디를 이용하여 리뷰가 이미 존재하는지 확인
        boolean isReviewExist = reviewService.isReviewExist(reviewSaveDto.getReservationId());

        if (isReviewExist) {
            // 이미 리뷰가 작성된 경우
            return ResponseEntity.badRequest().body("이미 작성된 리뷰가 있습니다.");
        } else {
            // 리뷰 작성 로직
            Review savedReview = reviewService.saveReview(reviewSaveDto, tokenUserInfo);
            return ResponseEntity.ok(savedReview);
        }
    }
}

