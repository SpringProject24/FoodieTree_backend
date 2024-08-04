package org.nmfw.foodietree.domain.customer.repository;

import org.nmfw.foodietree.domain.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerEditRepository extends JpaRepository<Customer, Long> {

	Customer findByCustomerId(String customerId);
}
