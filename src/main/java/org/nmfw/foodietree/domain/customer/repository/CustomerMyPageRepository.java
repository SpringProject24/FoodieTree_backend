package org.nmfw.foodietree.domain.customer.repository;

import org.nmfw.foodietree.domain.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerMyPageRepository extends JpaRepository<Customer, Long>, CustomerMyPageRepositoryCustom {
}
