package org.nmfw.foodietree.domain.store.repository;

import org.nmfw.foodietree.domain.store.entity.StoreApproval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreApprovalRepositoryCustom {

    Page<StoreApproval> findStoreApprovals(Pageable pageable, String sort, String storeId);
}
