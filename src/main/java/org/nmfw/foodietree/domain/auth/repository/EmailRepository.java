package org.nmfw.foodietree.domain.auth.repository;

import org.apache.ibatis.annotations.Param;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeDto;
import org.nmfw.foodietree.domain.auth.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface EmailRepository extends JpaRepository<EmailVerification, Integer>, EmailRepositoryCustom {
    @Modifying
    @Transactional
    @Query("UPDATE EmailVerification ev SET ev.expiryDate = :expiryDate, ev.emailVerified = :emailVerified WHERE ev.email = :email")
    void updateEmailVerification(@Param("expiryDate") LocalDateTime expiryDate, @Param("emailVerified") boolean emailVerified, @Param("email") String email);

    @Modifying
    @Transactional
    @Query("INSERT INTO EmailVerification (email, code, expiryDate, emailVerified, userType) VALUES (:email, NULL, :expiryDate, :emailVerified, :userType)")
    void saveEmailVerification(@Param("email") String email, @Param("expiryDate") LocalDateTime expiryDate, @Param("emailVerified") boolean emailVerified, @Param("userType") String userType);

    @Query("SELECT COUNT(ev) FROM EmailVerification ev WHERE ev.email = :email")
    int countByEmail(@Param("email") String email);

    @Query("SELECT ev FROM EmailVerification ev WHERE ev.email = :email")
    EmailCodeDto findOneByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("INSERT INTO EmailVerification (storeId, code, expiryDate) VALUES (:storeId, :code, :expiryDate)")
    void saveStoreVerificationCode(@Param("storeId") Long storeId, @Param("code") String code, @Param("expiryDate") LocalDateTime expiryDate);
}
