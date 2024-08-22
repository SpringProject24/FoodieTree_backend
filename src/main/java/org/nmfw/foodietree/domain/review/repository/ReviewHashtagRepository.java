package org.nmfw.foodietree.domain.review.repository;

import org.nmfw.foodietree.domain.review.entity.Review;
import org.nmfw.foodietree.domain.review.entity.ReviewHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewHashtagRepository extends JpaRepository<ReviewHashtag, Long>, ReviewRepositoryCustom {
    boolean existByReservationId(Long reservationId);
}
