package org.nmfw.foodietree.domain.reservation.repository;

import org.nmfw.foodietree.domain.reservation.entity.Waitlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitlistRepository extends JpaRepository<Waitlist, Long> {
}
