package org.nmfw.foodietree.domain.reservation.repository;

import java.util.List;
import org.nmfw.foodietree.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository
        extends JpaRepository<Reservation, Long>,
        ReservationRepositoryCustom {
	List<Reservation> findByCustomerId(String customerId);
	List<Reservation> findByPaymentId(String paymentId);
}
