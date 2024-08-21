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
import org.nmfw.foodietree.domain.review.dto.res.ReviewDetailDto;
import org.nmfw.foodietree.domain.review.dto.res.ReviewSaveDto;
import org.nmfw.foodietree.domain.review.entity.Hashtag;
import org.nmfw.foodietree.domain.review.entity.Review;
import org.nmfw.foodietree.domain.review.entity.ReviewHashtag;
import org.nmfw.foodietree.domain.review.repository.ReviewHashtagRepository;
import org.nmfw.foodietree.domain.review.repository.ReviewRepository;

import org.nmfw.foodietree.domain.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//import static sun.awt.image.MultiResolutionCachedImage.map;

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
//            String customerId = customerRepository.findByCustomerId(reviewSaveDto.getCustomerId())
//                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        ReservationDetailDto reservationByReservationId = reservationRepository.findReservationByReservationId(reviewSaveDto.getReservationId());

        // Review 객체 생성
        Review review = Review.builder()
                .reservation(reservation)
                .customerId(customerId)
                .product(product)
                .storeId(product.getStoreId())
                .storeName(reservationByReservationId.getStoreName())
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

    // 예약정보에 담긴 값들 찾기
    public Map<String, Object> findStore(Long reservationId) {

        ReservationDetailDto reservationByReservationId = reservationRepository.findReservationByReservationId(reservationId);

        Map<String, Object> storeDetails = new HashMap<>();
        storeDetails.put("storeName", reservationByReservationId.getStoreName()); // 예약 가게 이름
        storeDetails.put("address", reservationByReservationId.getAddress()); // 예약 가게 주소
        storeDetails.put("customerId", reservationByReservationId.getCustomerId()); // 예약 닉네임
        storeDetails.put("storeImg", reservationByReservationId.getStoreImg()); // 가게 이미지
        storeDetails.put("storeId", reservationByReservationId.getStoreId()); // 가게 아이디
        storeDetails.put("category", reservationByReservationId.getCategory());// 가게 카테고리
        storeDetails.put("", reservationByReservationId.getProfileImage()); // 사용자의 이미지


        return storeDetails;
    }

    /**
     * reservationId가 주어진 조건을 모두 만족하는지 확인
     * @param reservationId
     * @return true if the reservation exists and matches the conditions, otherwise false
     */
    public boolean isReservationValid(Long reservationId) {
        // 구매, 픽업 완료한 예약 건 구분
        // 예약이 존재하고 조건을 모두 만족하면 true 반환
        return reservationRepository.isReservationValid(reservationId);

    }


    // 모든 리뷰 리스트로 찾기
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public List<ReviewDetailDto> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ReviewDetailDto convertToDto(Review review) {
        List<Hashtag> hashtags = review.getHashtags().stream()
                .map(ReviewHashtag::getHashtag) // ReviewHashtag에서 Hashtag 추출
                .collect(Collectors.toList()); // List<Hashtag>로 변환
        return ReviewDetailDto.builder()
                .reservationId(review.getReservation() != null ? review.getReservation().getReservationId() : null)
                .customerId(review.getCustomerId())
                .storeImg(review.getStoreImg())
                .reviewScore(review.getReviewScore())
                .reviewImg(review.getReviewImg())
                .reviewContent(review.getReviewContent())
                .hashtags(hashtags)
                .build();
    }
}
