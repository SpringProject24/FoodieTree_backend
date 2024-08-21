package org.nmfw.foodietree.domain.review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.security.TokenProvider.TokenUserInfo;
import org.nmfw.foodietree.domain.product.Util.FileUtil;

import org.nmfw.foodietree.domain.review.dto.res.ReviewSaveDto;
import org.nmfw.foodietree.domain.review.entity.Hashtag;
import org.nmfw.foodietree.domain.review.entity.Review;
import org.nmfw.foodietree.domain.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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


    // 이미지 저장 경로
    @Value("${env.upload.path}")
    private String uploadDir;

    /**
     *
     * @param reviewSaveDto
     * @param tokenUserInfo
     * @return 리뷰아이디가 이미있는경우, 없는경우
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveReview(@RequestBody ReviewSaveDto reviewSaveDto, MultipartFile reviewImg, // metadata로 저장되어 사용하지 않음
                                        @AuthenticationPrincipal TokenUserInfo tokenUserInfo) {

//        if (reviewImg != null && !reviewImg.isEmpty()) {
//            // MultipartFile을 사용한 이미지저장
//            String imagePath = reviewService.uploadReviewImage(reviewImg);
//            if (imagePath != null) {
//            reviewSaveDto.setReviewImg(imagePath);
//            }
//            } else //저장 안됨
            if (reviewSaveDto.getReviewImg() != null && !reviewSaveDto.getReviewImg().isEmpty()) {
            // Base64 문자열을 사용한 이미지 저장
            String imagePath = uploadDir + "reviewImg_" + System.currentTimeMillis() + ".jpg";
            FileUtil.saveImageFromBase64(reviewSaveDto.getReviewImg(), imagePath);
            reviewSaveDto.setReviewImg(imagePath);
            }

        // 예약 아이디로 이미 작성된 아이디인지 확인
        boolean isReviewExist = reviewService.isReviewExist(reviewSaveDto.getReservationId());

        if (isReviewExist) {
            // 이미 리뷰가 작성된 경우 bad request
            String errorMessage = "이미 작성된 리뷰가 있습니다.";
            log.warn("Error: {}", errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        } else {
            // 구매, 픽업 완료한 건인지 판단
            boolean isValidReview = reviewService.isReservationValid(reviewSaveDto.getReservationId());
            if(!isValidReview) {
                String errorMessage = "예약아이디가 유효하지 않습니다.";
                log.warn("Error: {}", errorMessage);
                return ResponseEntity.badRequest().body(errorMessage);
            }

            // 별점은 1점 이상인지 확인
            Integer reviewScore = reviewSaveDto.getReviewScore();
            if (reviewScore < 1) {
                String errorMessage = "별점은 최소 1점 이상 이어야 합니다.";
                log.warn("Error: {}", errorMessage);
                return ResponseEntity.badRequest().body(errorMessage);
            }
            // 해시태그가 최소 3개 이상인지 확인
            List<Hashtag> hashtags = reviewSaveDto.getHashtags();
            if (hashtags.size() < 3) {
                String errorMessage = "최소 3개의 해시태그를 선택해야 합니다.";
                log.warn("Error: {}", errorMessage);
                return ResponseEntity.badRequest().body(errorMessage);
            }

            // 리뷰 저장
            Review savedReview = reviewService.saveReview(reviewSaveDto, tokenUserInfo);
            // 로그로 저장한 값 확인
            log.debug("Saved Review: {}", savedReview);

            // 해시태그 저장
            reviewService.saveReviewHashtags(savedReview, hashtags);

            return ResponseEntity.ok(savedReview);
        }
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllReviews () {
        List<Review> all = reviewService.findAll();

        return ResponseEntity.ok(all);
    }

    @GetMapping("/storeInfo")
    public ResponseEntity<?> getStoreInfo (Long reservationId) {
        Map<String, Object> store = reviewService.findStore(reservationId);

        return ResponseEntity.ok(store);
    }

}


