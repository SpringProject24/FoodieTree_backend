package org.nmfw.foodietree.domain.reservation.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.product.entity.Product;
import org.nmfw.foodietree.domain.product.repository.ProductRepository;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationDetailDto;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationFoundStoreIdDto;
import org.nmfw.foodietree.domain.reservation.entity.Reservation;
import org.nmfw.foodietree.domain.reservation.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("inmem_test")
@SpringBootTest
@Transactional
class ReservationRepositoryCustomImplTest {

	private static final Logger log = LoggerFactory.getLogger(
		ReservationRepositoryCustomImplTest.class);
	@Autowired
	ReservationRepository reservationRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ReservationService reservationService;

	@BeforeEach
	void before() {
		String storeId = "thdghtjd115@naver.com";
		for (int i = 0; i < 3; i++) {
			productRepository.save(
				Product.builder()
					.storeId(storeId)
					.pickupTime(LocalDateTime.now().plusMinutes(60))
					.build());
		}
	}

	@Test
	void findByStoreIdLimit() {
		// given
		String storeId = "thdghtjd115@naver.com";
		int cnt = 3;
		// when
		List<ReservationFoundStoreIdDto> byStoreIdLimit = reservationRepository.findByStoreIdLimit(
			storeId, cnt);
		// then
		byStoreIdLimit.forEach(e -> log.info("{}", e));
		Assertions.assertAll(
			() -> assertEquals(3, byStoreIdLimit.size()),
			() -> assertEquals(storeId, byStoreIdLimit.get(0).getStoreId())
		);
	}

	@Test
	void createReservationTest() {
		// given
		String customerId = "thdghtjd115@gmail.com";
		String storeId = "thdghtjd115@naver.com";
		Map<String, String> data = Map.of(
			"storeId", storeId,
			"cnt", "3"
		);
		// when
		boolean flag = reservationService.createReservation(customerId, data);
		// then
		List<Reservation> byCustomerId = reservationRepository.findByCustomerId(customerId);
		byCustomerId.forEach(e -> log.info("{}", e));
		Assertions.assertAll(
			() -> assertTrue(flag),
			() -> assertEquals(3, byCustomerId.size()),
			() -> assertEquals(customerId, byCustomerId.get(0).getCustomerId())
		);
	}
}