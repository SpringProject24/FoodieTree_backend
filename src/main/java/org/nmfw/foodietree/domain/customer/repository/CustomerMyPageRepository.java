package org.nmfw.foodietree.domain.customer.repository;

import org.nmfw.foodietree.domain.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CustomerMyPageRepository extends JpaRepository<Customer, Long>, CustomerMyPageRepositoryCustom {
}
