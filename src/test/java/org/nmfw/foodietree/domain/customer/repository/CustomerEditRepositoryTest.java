package org.nmfw.foodietree.domain.customer.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.customer.entity.Customer;
import org.nmfw.foodietree.domain.customer.entity.FavArea;
import org.nmfw.foodietree.domain.customer.entity.FavFood;
import org.nmfw.foodietree.domain.customer.entity.FavStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("inmem_test")
class CustomerEditRepositoryTest {
    @Autowired
    private FavFoodRepository favFoodRepository;
    @Autowired
    private FavAreaRepository favAreaRepository;
    @Autowired
    private FavStoreRepository favStoreRepository;
    @Autowired
    private CustomerEditRepository customerEditRepository;

    @Test
    void insertPreferredInfo() {
        // given
        String customerId = "test@gmail.com";
        String storeId = "thdghtjd115@naver.com";
        String food = "양식";
        String area = "서울시 마포구";
        // when
        FavStore save = favStoreRepository.save(FavStore.builder().customerId(customerId).storeId(storeId).build());
        FavFood save1 = favFoodRepository.save(FavFood.builder().customerId(customerId).preferredFood(food).build());
        FavArea save2 = favAreaRepository.save(FavArea.builder().customerId(customerId).preferredArea(area).build());
        // then
        Assertions.assertAll(
                () -> assertNotNull(save),
                () -> assertEquals(save.getStoreId(), storeId),
                () -> assertNotNull(save1),
                () -> assertEquals(save1.getPreferredFood(), food),
                () -> assertNotNull(save2),
                () -> assertEquals(save2.getPreferredArea(), area)
        );
    }

    @BeforeEach
    void before() {
        String customerId = "test@gmail.com";
        String storeId = "thdghtjd115@naver.com";
        String food = "한식";
        String area = "서울시 마포구 대흥동";
        // when
        FavStore save = favStoreRepository.save(FavStore.builder().customerId(customerId).storeId(storeId).build());
        FavFood save1 = favFoodRepository.save(FavFood.builder().customerId(customerId).preferredFood(food).build());
        FavArea save2 = favAreaRepository.save(FavArea.builder().customerId(customerId).preferredArea(area).build());
    }

    @Test
    void deletePreferredInfo() {
        // given
        String customerId = "test@gmail.com";
        String storeId = "thdghtjd115@naver.com";
        String food = "한식";
        String area = "서울시 마포구 대흥동";
        int storeCnt = favStoreRepository.findAll().size();
        int foodCnt = favFoodRepository.findAll().size();
        int areaCnt = favAreaRepository.findAll().size();
        // when
        favStoreRepository.deleteByCustomerIdAndStoreId(customerId, storeId);
        favFoodRepository.deleteByCustomerIdAndPreferredFood(customerId, food);
        favAreaRepository.deleteByCustomerIdAndPreferredArea(customerId, area);
        // then
        Assertions.assertAll(
                () -> assertEquals(storeCnt - 1, favStoreRepository.findAll().size()),
                () -> assertEquals(foodCnt - 1, favFoodRepository.findAll().size()),
                () -> assertEquals(areaCnt - 1, favAreaRepository.findAll().size())
        );
    }

    @Test
    void updateProfile() {
        // given
        customerEditRepository.save(Customer.builder()
                .customerId("test@gmail.com")
                .nickname("hoho")
                .customerPhoneNumber("010")
                .build());
        String customerId = "test@gmail.com";
        String nickname = "hoho12";
        String phone = "01012341234";
        // when
        Customer found = customerEditRepository.findByCustomerId(customerId);
        found.setNickname(nickname);
        found.setCustomerPhoneNumber(phone);
        Customer save = customerEditRepository.save(found);
        // then
        Assertions.assertAll(
                () -> assertEquals(nickname, save.getNickname()),
                () -> assertEquals(phone, save.getCustomerPhoneNumber())
        );
    }
}