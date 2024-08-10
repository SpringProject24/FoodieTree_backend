//package org.nmfw.foodietree.domain.store.repository;
//
//import org.nmfw.foodietree.domain.customer.entity.FavStore;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface FavStoreRepository extends JpaRepository<FavStore, Long> {
//    // 사용자가 누른 StoreId 조회
//    FavStore findByCustomerIdAndStoreId(String customerId, String storeId);
//    // 사용자의 찜 가게 상태 조회
//    List<FavStore> findByCustomerId(String customerId);
//}
