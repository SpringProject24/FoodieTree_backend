package org.nmfw.foodietree.domain.customer.service;

import lombok.RequiredArgsConstructor;
import org.nmfw.foodietree.domain.customer.repository.CustomerEditRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerEditService {

	private final CustomerEditRepository customerEditRepository;


}
