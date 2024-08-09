package org.nmfw.foodietree.domain.store.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.customer.dto.resp.UpdateDto;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.nmfw.foodietree.domain.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Transactional
@ActiveProfiles("inmem_test")
class StoreMyPageEditServiceTest {
	@Autowired
	private StoreMyPageEditService storeMyPageEditService;
	@Autowired
	private StoreRepository repository;
	@Autowired
	private StoreRepository storeRepository;

	@BeforeEach
	void before() {
		String storeId = "thdghtjd115@naver.com";
		repository.save(
			Store.builder()
				.storeId(storeId)
				.build()
		);
	}

	@Test
	void updateProfileInfo() {
		// given
		String storeId = "thdghtjd115@naver.com";
		String price = "3900";
		String open = "10:42:00";
		String close = "22:42:00";
		String productCnt = "6";
		String phone = "01022221234";
		// when
		storeMyPageEditService.updateProfileInfo(storeId, UpdateDto.builder().type("price").value(price).build());
		storeMyPageEditService.updateProfileInfo(storeId, UpdateDto.builder().type("openAt").value(open).build());
		storeMyPageEditService.updateProfileInfo(storeId, UpdateDto.builder().type("closedAt").value(close).build());
		storeMyPageEditService.updateProfileInfo(storeId, UpdateDto.builder().type("productCnt").value(productCnt).build());
		storeMyPageEditService.updateProfileInfo(storeId, UpdateDto.builder().type("store_contact").value(phone).build());
		// then
		Store found = storeRepository.findByStoreId(storeId).orElse(null);
		assert found != null;
		Assertions.assertAll(
			() -> assertEquals(Integer.parseInt(price), found.getPrice()),
			() -> assertEquals(parseTime(open), found.getOpenAt()),
			() -> assertEquals(parseTime(close), found.getClosedAt()),
			() -> assertEquals(Integer.parseInt(productCnt), found.getProductCnt()),
			() -> assertEquals(phone, found.getStoreContact())
		);
	}

	private LocalTime parseTime(String time) {
		time = time.replaceAll("\"", "");
		return LocalTime.parse(time);
	}
}