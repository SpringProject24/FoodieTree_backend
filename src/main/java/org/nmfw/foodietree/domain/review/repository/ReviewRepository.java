package org.nmfw.foodietree.domain.review.repository;
import org.nmfw.foodietree.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
    boolean existsByReservationIdAndCustomerId(Long reservationId, String customerId);
}
