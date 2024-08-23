package org.nmfw.foodietree.domain.store.repository;

import org.apache.ibatis.annotations.Param;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long>, SearchRepositoryCustom {
    Optional<Store> findByStoreId(String storeId);

    @Modifying
    @Transactional
    @Query("UPDATE Store s SET s.refreshTokenExpireDate = :refreshTokenExpireDate WHERE s.storeId = :storeId")
    void updateRefreshTokenExpireDate(
            @Param("refreshTokenExpireDate") LocalDateTime refreshTokenExpireDate,
            @Param("storeId") String storeId
    );

    @Query("SELECT COUNT(c) > 0 FROM Store c WHERE c.storeId = :email")
    boolean existsByStoreId(@Param("email") String email);

}
