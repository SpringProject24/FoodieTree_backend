package org.nmfw.foodietree.domain.store.repository;

import org.nmfw.foodietree.domain.store.dto.resp.StoreMyPageDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreMyPageRepository
        extends JpaRepository<StoreMyPageDto, Long>, StoreMyPageRepositoryCustom {
}
