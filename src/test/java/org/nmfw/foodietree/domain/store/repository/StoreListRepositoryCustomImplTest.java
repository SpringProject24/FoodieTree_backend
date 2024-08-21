package org.nmfw.foodietree.domain.store.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class StoreListRepositoryCustomImplTest {

	private static final Logger log = LoggerFactory.getLogger(
		StoreListRepositoryCustomImplTest.class);
	@Autowired
	StoreListRepositoryCustomImpl storeListRepository;

	@Test
	void test() {
		// given

		// when
		List<StoreListDto> allProductsStoreId = storeListRepository.findAllProductsStoreId();
		// then
		assertAll(
			() -> assertNotNull(allProductsStoreId)
		);
		allProductsStoreId.forEach(e -> log.info("{}", e));
	}
}