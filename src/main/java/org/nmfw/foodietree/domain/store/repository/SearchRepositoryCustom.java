package org.nmfw.foodietree.domain.store.repository;

import org.nmfw.foodietree.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchRepositoryCustom {

    Page<Store> findStores(Pageable pageable, String keyword);
}
