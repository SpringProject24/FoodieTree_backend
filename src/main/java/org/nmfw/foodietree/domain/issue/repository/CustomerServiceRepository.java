package org.nmfw.foodietree.domain.issue.repository;

import org.nmfw.foodietree.domain.issue.entity.CustomerService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerServiceRepository extends JpaRepository<CustomerService, Long> {
}
