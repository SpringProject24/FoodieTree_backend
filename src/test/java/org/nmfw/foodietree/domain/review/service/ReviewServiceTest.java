package org.nmfw.foodietree.domain.review.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.auth.security.TokenProvider;
import org.nmfw.foodietree.domain.review.dto.res.ReviewSaveDto;
import org.nmfw.foodietree.domain.review.entity.Hashtag;
import org.nmfw.foodietree.domain.review.entity.Review;
import org.nmfw.foodietree.domain.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.nmfw.foodietree.domain.auth.security.TokenProvider.*;

@SpringBootTest
@Transactional
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    // 이미 있는 리뷰를 저장하려고 할 때
    @Test

    @DisplayName("중복 확인하기")

    public void testSaveAndRetrieveExistReview() {
        // Given
        ReviewSaveDto reviewSaveDto = ReviewSaveDto.builder()
                .customerId("sinyunjong@gmail.com")

                .reservationId(616L)
                .storeImg("store_img.jpg")
                .reviewScore(5)
                .reviewImg("review_img.jpg")
                .reviewContent("Great experience!")
                .hashtags(Arrays.asList(Hashtag.FAST_SERVICE, Hashtag.PLEASANT_SURPRISE, Hashtag.EASY_TO_EAT))
                .build();


        Long reservationId = reviewSaveDto.getReservationId();

        System.out.println("*****************************************");
        System.out.println("reservationId = " + reservationId);
        System.out.println("*****************************************");

        // When
        // 중복 조회 확인
        boolean isReviewExist = reviewService.isReviewExist(reservationId);

        //then
        assertThat(isReviewExist).isTrue();
    }

    @Test
    @DisplayName("새로운 리뷰를 등록하려고 할 때 ")
    public void testSaveAndRetrieveNewReview() {
        // Given
        ReviewSaveDto reviewSaveDto = ReviewSaveDto.builder()
                .customerId("sinyunjong@gmail.com")
                .reservationId(44L)
                .storeImg("store_img.jpg")
                .reviewScore(1)
                .reviewImg("review_img.jpg")
                .reviewContent("이게뭐야")
                .hashtags(Arrays.asList(Hashtag.FAST_SERVICE, Hashtag.PLEASANT_SURPRISE, Hashtag.EASY_TO_EAT))
                .build();

        //when
        Review savedReview = reviewService.saveReview(reviewSaveDto

                , new TokenUserInfo("customer", "sinyunjong@gmail.com", LocalDateTime.of(2024, 8, 30, 12, 12))

        );

        //then
        assertThat(savedReview).isNotNull();
        assertThat(savedReview.getReviewScore()).isEqualTo(1);
        assertThat(savedReview.getReviewContent()).isEqualTo("이게뭐야");
    }


}