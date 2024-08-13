package org.nmfw.foodietree.domain.store.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.customer.repository.FavStoreRepository;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class FavStoreRepositoryCustomImplTest {

	private static final Logger log = LoggerFactory.getLogger(
		FavStoreRepositoryCustomImplTest.class);
	@Autowired
	FavStoreRepository favStoreRepository;

	@Test
	void findFavStoresByCustomerId() {
		// given
		String customerId = "thdghtjd115@gmail.com";
		// when
		List<StoreListDto> favStoresByCustomerId = favStoreRepository.findFavStoresByCustomerId(
			customerId, "favStore");
		List<StoreListDto> orders_3 = favStoreRepository.findFavStoresByCustomerId(customerId, "orders_3");
		favStoresByCustomerId.addAll(orders_3);
		// then
		favStoresByCustomerId.forEach(e -> log.info("{}", e));

	}

	@Test
	void findFavStoresByOrders() {
	}
}