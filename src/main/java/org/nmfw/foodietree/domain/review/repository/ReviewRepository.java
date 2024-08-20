package org.nmfw.foodietree.domain.review.repository;
import org.nmfw.foodietree.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    boolean existByReservationId(Long reservationId);

    List<Review> findAll();
}
