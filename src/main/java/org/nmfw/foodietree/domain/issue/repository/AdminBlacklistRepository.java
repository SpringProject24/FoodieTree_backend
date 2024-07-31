package org.nmfw.foodietree.domain.issue.repository;

import org.nmfw.foodietree.domain.issue.entiry.AdminBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminBlacklistRepository extends JpaRepository<AdminBlacklist, Long> {
}
