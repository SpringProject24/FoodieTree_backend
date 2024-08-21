package org.nmfw.foodietree.domain.store.repository;

import org.nmfw.foodietree.domain.store.dto.resp.SearchedStoreListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchRepositoryCustom {

    Page<SearchedStoreListDto> findStores(Pageable pageable, String keyword);
}
