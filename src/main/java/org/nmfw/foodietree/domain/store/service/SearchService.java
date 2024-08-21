package org.nmfw.foodietree.domain.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.store.dto.request.SearchDto;
import org.nmfw.foodietree.domain.store.dto.resp.SearchedStoreListDto;
import org.nmfw.foodietree.domain.store.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class SearchService {

    private final StoreRepository storeRepository;

    public Map<String, Object> searchStores(SearchDto dto) {
        Pageable pageable = PageRequest.of(dto.getPageNo() - 1, 20);
        Page<SearchedStoreListDto> stores = storeRepository.findStores(pageable, dto.getKeyword());
        return Map.of(
                "result", stores.getContent(),
                "totalCnt", stores.getTotalElements()
        );
    }
}
