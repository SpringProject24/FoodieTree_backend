package org.nmfw.foodietree.domain.store.repository;

import org.nmfw.foodietree.domain.store.dto.resp.StoreMyPageDto;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreMyPageRepository
        extends JpaRepository<Store, Long>, StoreMyPageRepositoryCustom {
}
