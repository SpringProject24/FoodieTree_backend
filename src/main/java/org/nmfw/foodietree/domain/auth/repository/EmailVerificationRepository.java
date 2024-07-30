package org.nmfw.foodietree.domain.auth.repository;

import org.nmfw.foodietree.domain.auth.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Integer> {
    Optional<EmailVerification> findByCustomerId(String customerId);
    Optional<EmailVerification> findByStoreId(String storeId);
    Optional<EmailVerification> findByCode(String code);
}
