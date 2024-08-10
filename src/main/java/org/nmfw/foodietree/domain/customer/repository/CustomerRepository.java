package org.nmfw.foodietree.domain.customer.repository;

import org.apache.ibatis.annotations.Param;
import org.nmfw.foodietree.domain.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@Transactional
public interface CustomerRepository extends JpaRepository<Customer, Long> ,CustomerRepositoryCustom {

    @Query("SELECT COUNT(s) > 0 FROM Store s WHERE s.storeId = :email")
    Boolean existsInStore(@Param("email") String email);

    @Query("SELECT c.refreshTokenExpireDate FROM Customer c WHERE c.customerId = :email")
    Optional<LocalDateTime> findRefreshDateById(@Param("email") String email);

    @Modifying
    @Query("UPDATE Customer c SET c.refreshTokenExpireDate = :refreshTokenExpireDate WHERE c.customerId = :customerId")
    void updateRefreshTokenExpireDate(@Param("refreshTokenExpireDate") LocalDateTime refreshTokenExpireDate, @Param("customerId") String customerId);

    @Query("SELECT COUNT(c) > 0 FROM Customer c WHERE c.customerId = :email")
    boolean existsByCustomerId(@Param("email") String email);

    @Query
    Optional<Customer> findByCustomerId(@Param("customerId") String customerId);
}
