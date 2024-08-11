package org.nmfw.foodietree.domain.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.store.dto.request.SearchDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListDto;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.nmfw.foodietree.domain.store.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class SearchService {

    private final StoreRepository storeRepository;

    public Map<String, Object> searchStores(SearchDto dto) {
        Pageable pageable = PageRequest.of(dto.getPageNo() - 1, 8);
        Page<Store> stores = storeRepository.findStores(pageable, dto.getKeyword());
        List<StoreListDto> collect = stores.getContent()
                .stream().map(StoreListDto::fromEntity)
                .collect(Collectors.toList());
        return Map.of(
                "result", collect,
                "totalCnt", stores.getTotalElements()
        );
    }
}
