package org.nmfw.foodietree.domain.store.repository;

import org.nmfw.foodietree.domain.store.entity.StoreApproval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreApprovalRepository
        extends JpaRepository<StoreApproval, Long>, StoreApprovalRepositoryCustom {
}
