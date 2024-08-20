package org.nmfw.foodietree.domain.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.security.TokenProvider.TokenUserInfo;
import org.nmfw.foodietree.domain.customer.entity.Customer;
import org.nmfw.foodietree.domain.customer.repository.CustomerRepository;
import org.nmfw.foodietree.domain.product.entity.Product;
import org.nmfw.foodietree.domain.product.repository.ProductRepository;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationDetailDto;
import org.nmfw.foodietree.domain.reservation.entity.Reservation;
import org.nmfw.foodietree.domain.reservation.repository.ReservationRepository;
import org.nmfw.foodietree.domain.review.dto.res.ReviewSaveDto;
import org.nmfw.foodietree.domain.review.entity.Hashtag;
import org.nmfw.foodietree.domain.review.entity.Review;
import org.nmfw.foodietree.domain.review.entity.ReviewHashtag;
import org.nmfw.foodietree.domain.review.repository.ReviewHashtagRepository;
import org.nmfw.foodietree.domain.review.repository.ReviewRepository;
import org.nmfw.foodietree.domain.store.repository.StoreRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final StoreRepository storeRepository;

    public boolean isReviewExist(Long reservationId) {
        return reviewRepository.existByReservationId(reservationId);
    }

    public Review saveReview(ReviewSaveDto reviewSaveDto
            , @AuthenticationPrincipal TokenUserInfo tokenUserInfo
    ) {

        // Reservation과 Product 객체를 가져와야 함
        Reservation reservation = reservationRepository.findById(reviewSaveDto.getReservationId())
                .orElseThrow(() -> new IllegalArgumentException("해당 예약이 존재하지 않습니다."));
        Product product = productRepository.findById(reservation.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("해당 제품이 존재하지 않습니다."));
        String customerId = tokenUserInfo.getUsername();
//            String customerId = customerRepository.findByCustomerId(reviewSaveDto.getCustomerId())
//                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        // Review 객체 생성
        Review review = Review.builder()
                .reservation(reservation)
                .customerId(customerId)
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

    // 모든 리뷰 리스트로 찾기
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    // 예약정보에 담긴 값들 찾기
    public Map<String, Object> findStore(Long reservationId) {

        ReservationDetailDto reservationByReservationId = reservationRepository.findReservationByReservationId(reservationId);

        Map<String, Object> storeDetails = new HashMap<>();
        storeDetails.put("storeName", reservationByReservationId.getStoreName()); // 예약 가게 이름
        storeDetails.put("address", reservationByReservationId.getAddress()); // 예약 가게 주소
        storeDetails.put("nickname", reservationByReservationId.getNickname()); // 예약 닉네임
        storeDetails.put("storeImg", reservationByReservationId.getStoreImg()); // 가게 이미지
        storeDetails.put("storeId", reservationByReservationId.getStoreId()); // 가게 아이디
        storeDetails.put("category", reservationByReservationId.getCategory());// 가게 카테고리
        storeDetails.put("", reservationByReservationId.getProfileImage()); // 사용자의 이미지


        return storeDetails;
    }
}
