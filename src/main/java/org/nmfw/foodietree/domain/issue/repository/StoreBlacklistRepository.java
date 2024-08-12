package org.nmfw.foodietree.domain.issue.repository;

import org.nmfw.foodietree.domain.issue.entity.StoreBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreBlacklistRepository extends JpaRepository<StoreBlacklist, Long> {
}
