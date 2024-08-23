package org.nmfw.foodietree.domain.review.repository;


import org.nmfw.foodietree.domain.review.dto.res.MyReviewDto;
import org.nmfw.foodietree.domain.review.entity.Hashtag;

import java.util.List;
import java.util.Map;

public interface ReviewRepositoryCustom {

    boolean existByReservationId(Long reservationId);

    boolean existsByReservationId(Long reservationId);

    List<MyReviewDto> findEnableReviewsByCustomerId(String customerId);

    // 구매, 픽업 완료한 예약 건인지 예약 아이디로 판단
    boolean isReservationValid(Long reservationId);
    Map<Long, List<Hashtag>> findHashtagsByReviewIds(List<Long> reviewIds);
}
