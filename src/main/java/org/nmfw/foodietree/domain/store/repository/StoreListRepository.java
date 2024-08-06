package org.nmfw.foodietree.domain.store.repository;

import org.nmfw.foodietree.domain.store.entity.Store;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreListRepository extends JpaRepository<Store, String>{

//    @Query("SELECT new org.nmfw.foodietree.domain.store.dto.resp.StoreListDto(" +
//            "s.storeId, s.storeName, s.category, s.address, s.price, s.storeImg, " +
//            "s.productCnt, s.openAt, s.closedAt, s.limitTime, s.emailVerified) " +
//            "FROM Store s WHERE s.category = :category")
    List<Store> findByCategory(String category);

}
