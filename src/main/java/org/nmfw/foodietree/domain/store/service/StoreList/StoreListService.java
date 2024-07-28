package org.nmfw.foodietree.domain.store.service.StoreList;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListDto;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.nmfw.foodietree.domain.store.repository.StoreListRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StoreListService {

    private final StoreListRepository storeListRepository;

    // 모든 가게 리스트 출력
    public List<StoreListDto> getAllStores() {
        List<Store> stores = storeListRepository.findAll();
        return stores.stream()
                .map(StoreListDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 해당 카테고리 별 리스트 출력
    public List<StoreListDto> getStoresByCategory(String category) {
        List<Store> stores = storeListRepository.findByCategory(category);
        return stores.stream()
                .map(StoreListDto::fromEntity)
                .collect(Collectors.toList());
    }

}
