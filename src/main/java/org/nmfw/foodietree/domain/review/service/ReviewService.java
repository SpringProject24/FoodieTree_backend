package org.nmfw.foodietree.domain.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.security.TokenProvider.TokenUserInfo;
import org.nmfw.foodietree.domain.customer.dto.resp.UpdateDto;
import org.nmfw.foodietree.domain.customer.repository.CustomerRepository;
import org.nmfw.foodietree.domain.product.Util.FileUtil;
import org.nmfw.foodietree.domain.product.entity.Product;
import org.nmfw.foodietree.domain.product.repository.ProductRepository;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationDetailDto;

import org.nmfw.foodietree.domain.reservation.entity.Reservation;
import org.nmfw.foodietree.domain.reservation.repository.ReservationRepository;
import org.nmfw.foodietree.domain.reservation.service.ReservationService;

import org.nmfw.foodietree.domain.review.dto.res.MyReviewDto;
import org.nmfw.foodietree.domain.review.dto.res.ReviewDetailDto;
import org.nmfw.foodietree.domain.review.dto.res.MyReviewDto;

import org.nmfw.foodietree.domain.review.dto.res.ReviewSaveDto;
import org.nmfw.foodietree.domain.review.entity.Hashtag;
import org.nmfw.foodietree.domain.review.entity.Review;
import org.nmfw.foodietree.domain.review.entity.ReviewHashtag;
import org.nmfw.foodietree.domain.review.repository.ReviewHashtagRepository;
import org.nmfw.foodietree.domain.review.repository.ReviewRepository;

import org.nmfw.foodietree.domain.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewHashtagRepository reviewHashtagRepository;
    private final ReservationRepository reservationRepository;
    private final ProductRepository productRepository;


    // 이미지 저장 경로
    @Value("${env.upload.path}")
    private String uploadDir;

    public boolean isReviewExist(Long reservationId) {
        return reviewRepository.existByReservationId(reservationId);
    }

    public String uploadReviewImage(MultipartFile reviewImg) {
        return FileUtil.uploadFile(uploadDir, reviewImg);
    }


    public Review saveReview(ReviewSaveDto reviewSaveDto
            , @AuthenticationPrincipal TokenUserInfo tokenUserInfo
    ) {
        // Reservation과 Product 객체를 가져와서 각각의 id(기본키)  조회
        Reservation reservation = reservationRepository.findById(reviewSaveDto.getReservationId())
                .orElseThrow(() -> new IllegalArgumentException("해당 예약이 존재하지 않습니다."));
        Product product = productRepository.findById(reservation.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("해당 제품이 존재하지 않습니다."));
        String customerId = tokenUserInfo.getUsername(); // token 처리

        ReservationDetailDto reservationByReservationId = reservationRepository.findReservationByReservationId(reviewSaveDto.getReservationId());

        // Review 객체 생성
        Review review = Review.builder()
                .reservationId(reservation.getReservationId())
                .customerId(customerId)
                .storeId(reservationByReservationId.getStoreId()) //storeId
                .storeName(reservationByReservationId.getStoreName())
                .storeImg(reservationByReservationId.getStoreImg())
                .address(reservationByReservationId.getAddress())
                .reviewScore(reviewSaveDto.getReviewScore())
                .reviewImg(reviewSaveDto.getReviewImg())
                .reviewContent(reviewSaveDto.getReviewContent())
                .build();

        // Review 저장
        Review savedReview = reviewRepository.save(review);

        // 해시태그 저장
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

    // 예약정보에 담긴 값들 찾기
    public Map<String, Object> findStore(Long reservationId) {

        ReservationDetailDto reservationByReservationId = reservationRepository.findReservationByReservationId(reservationId);

        Map<String, Object> storeDetails = new HashMap<>();

        storeDetails.put("reservationId", reservationId);
        storeDetails.put("customerId", reservationByReservationId.getCustomerId()); // 구매한 customer Id
        storeDetails.put("profileImg", reservationByReservationId.getProfileImage()); // 구매자 플필 사진
        storeDetails.put("storeId", reservationByReservationId.getStoreId()); // 가게 아이디
        storeDetails.put("storeName", reservationByReservationId.getStoreName()); // 예약 가게 이름
        storeDetails.put("storeImg", reservationByReservationId.getStoreImg()); // 상품 상점 사진
        storeDetails.put("category", reservationByReservationId.getCategory());// 가게 카테고리
        storeDetails.put("address", reservationByReservationId.getAddress()); // address 가져오기
        storeDetails.put("price", reservationByReservationId.getPrice());// 상품 가격
        storeDetails.put("pickedUpAt", reservationByReservationId.getPickupTime());// 상품 픽업 일시


        return storeDetails;
    }

    /**
     * reservationId가 주어진 조건을 모두 만족하는지 확인
     *
     * @param reservationId
     * @return true if the reservation exists and matches the conditions, otherwise false
     */
    public boolean isReservationValid(Long reservationId) {
        // 구매, 픽업 완료한 예약 건 구분
        // 예약이 존재하고 조건을 모두 만족하면 true 반환
        return reviewRepository.isReservationValid(reservationId);

    }


    // 모든 리뷰 리스트로 찾기
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public List<ReviewDetailDto> getAllReviews() {
//        List<Review> reviews = reviewRepository.findAll();
        // DB에서 모든 리뷰를 내림차순으로 가져오기
        List<Review> reviews = reviewRepository.findAll(Sort.by(Sort.Order.desc("reviewId")));

        // Review 엔티티를 ReviewDetailDto로 변환
        return reviews.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ReviewDetailDto convertToDto(Review review) {
        List<Hashtag> hashtags = review.getHashtags().stream()
                .map(ReviewHashtag::getHashtag) // ReviewHashtag에서 Hashtag 추출
                .collect(Collectors.toList()); // List<Hashtag>로 변환
        return ReviewDetailDto.builder()
                .reservationId(review.getReservationId() != null ? review.getReservationId() : null)
                .customerId(review.getCustomerId())
                .storeImg(review.getStoreImg())
                .reviewScore(review.getReviewScore())
                .reviewImg(review.getReviewImg())
                .reviewContent(review.getReviewContent())
                .storeId(review.getStoreId())
                .storeName(review.getStoreName())
                .storeImg(review.getStoreImg())
                .address(review.getAddress())
                .hashtags(hashtags)
                .build();
    }
    // Method to find reviews and combine with hashtags
    @Transactional(readOnly = true)
    public List<MyReviewDto> findEnableReviewsByCustomerId(String customerId) {
        // Step 1: Fetch reviews
        List<MyReviewDto> reviews = reviewRepository.findEnableReviewsByCustomerId(customerId);

        // Extract review IDs
        List<Long> reviewIds = reviews.stream()
                .map(MyReviewDto::getReservationId)
                .collect(Collectors.toList());

        // Step 2: Fetch hashtags
        Map<Long, List<Hashtag>> hashtagsByReviewId = reviewRepository.findHashtagsByReviewIds(reviewIds);

        // Combine results
        reviews.forEach(review -> review.setHashtags(hashtagsByReviewId.getOrDefault(review.getReservationId(), List.of())));

        // Step 3: Sort reviews
        return reviews.stream()
                .sorted(Comparator.comparing(MyReviewDto::getPickedUpAt, Comparator.reverseOrder())
                        .thenComparing(review -> review.getReviewId() == null ? 1 : 0)
                        .thenComparing(MyReviewDto::getReservationId))
                .collect(Collectors.toList());
    }

    // Update existing method to use the new method for fetching reviews
    public List<MyReviewDto> findEnableReview(String customerId) {
        return findEnableReviewsByCustomerId(customerId);
    }
}

