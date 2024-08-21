package org.nmfw.foodietree.domain.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.security.TokenProvider;
import org.nmfw.foodietree.domain.auth.security.TokenProvider.TokenUserInfo;
import org.nmfw.foodietree.domain.customer.entity.Customer;
import org.nmfw.foodietree.domain.customer.repository.CustomerRepository;
import org.nmfw.foodietree.domain.product.entity.Product;
import org.nmfw.foodietree.domain.product.repository.ProductRepository;
import org.nmfw.foodietree.domain.reservation.entity.Reservation;
import org.nmfw.foodietree.domain.reservation.repository.ReservationRepository;
import org.nmfw.foodietree.domain.review.dto.res.ReviewSaveDto;
import org.nmfw.foodietree.domain.review.entity.Hashtag;
import org.nmfw.foodietree.domain.review.entity.Review;
import org.nmfw.foodietree.domain.review.entity.ReviewHashtag;
import org.nmfw.foodietree.domain.review.repository.ReviewHashtagRepository;
import org.nmfw.foodietree.domain.review.repository.ReviewRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

        private final ReviewRepository reviewRepository;
        private final ReviewHashtagRepository reviewHashtagRepository;
        private final ReservationRepository reservationRepository;
        private final ProductRepository productRepository;
        private final CustomerRepository customerRepository;

        public boolean isReviewExist(Long reservationId) {
            return reviewRepository.existByReservationId(reservationId);
        }

        public Review saveReview(ReviewSaveDto reviewSaveDto
//                                 ,@AuthenticationPrincipal TokenUserInfo tokenUserInfo
        ) {

            // Reservation과 Product 객체를 가져와야 함
            Reservation reservation = reservationRepository.findById(reviewSaveDto.getReservationId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 예약이 존재하지 않습니다."));
            Product product = productRepository.findById(reservation.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 제품이 존재하지 않습니다."));
//            Customer customer = customerRepository.findByCustomerId(tokenUserInfo.getUsername())
            Customer customer = customerRepository.findByCustomerId(reviewSaveDto.getCustomerId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

            // Review 객체 생성
            Review review = Review.builder()
                    .reservation(reservation)
                    .customer(customer)
                    .product(product)
                    .storeName(product.getStoreId())
                    .storeImg(reviewSaveDto.getStoreImg())
                    .reviewScore(reviewSaveDto.getReviewScore())
                    .reviewImg(reviewSaveDto.getReviewImg())
                    .reviewContent(reviewSaveDto.getReviewContent())
                    .build();

            // Review 저장
            Review savedReview = reviewRepository.save(review);

            // 해시태그 저장 로직 호출
            saveReviewHashtags(savedReview, reviewSaveDto.getHashtags());

            return savedReview;
        }

    // 해시태그 저장 로직
    public void saveReviewHashtags(Review review, List<Hashtag> hashtags) {
        for (Hashtag hashtag : hashtags) {
            reviewHashtagRepository.save(ReviewHashtag.builder()
                            .hashtag(hashtag) // 해시태그 저장
                            .review(review) // 리뷰 저장
                    .build());
        }
    }
}
