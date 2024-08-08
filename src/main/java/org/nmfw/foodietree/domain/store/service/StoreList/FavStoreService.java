package org.nmfw.foodietree.domain.store.service.StoreList;

import org.nmfw.foodietree.domain.customer.entity.FavStore;
import org.nmfw.foodietree.domain.customer.repository.FavStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavStoreService {

    @Autowired
    private FavStoreRepository favStoreRepository;

    public void toggleFavorite(String customerId, String storeId) {
        if (favStoreRepository == null) {
            throw new IllegalStateException("FavStoreRepository is not initialized");
        }
        FavStore existingFavStore = favStoreRepository.findByCustomerIdAndStoreId(customerId, storeId);

        if (existingFavStore == null) {
            // 새로운 찜 상태 저장
            FavStore favStore = FavStore.builder()
                    .customerId(customerId)
                    .storeId(storeId)
                    .build();
            favStoreRepository.save(favStore);
        } else {
            // 기존 찜 상태 삭제
            favStoreRepository.delete(existingFavStore);
        }
    }

    public List<FavStore> getFavoritesByCustomerId(String customerId) {
        if (favStoreRepository == null) {
            throw new IllegalStateException("FavStoreRepository is not initialized");
        }
        return favStoreRepository.findByCustomerId(customerId);
    }
}
