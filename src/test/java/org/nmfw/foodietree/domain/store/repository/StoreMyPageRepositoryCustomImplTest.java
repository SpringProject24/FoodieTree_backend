package org.nmfw.foodietree.domain.store.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.product.dto.response.ProductInfoDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreCheckDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreMyPageCalendarModalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StoreMyPageRepositoryCustomImplTest {

    @Autowired
    StoreMyPageRepository storeMyPageRepository;

    @Test
    @DisplayName("getProductCntByDate test")
    void test1() {
        //given
        String storeId = "thdghtjd115@naver.com";
        String date = "2024-08-05";
        //when
        List<StoreMyPageCalendarModalDto> list1 = storeMyPageRepository.getStoreMyPageCalendarModalInfo(storeId, date);

        List<ProductInfoDto> productCntByDate = storeMyPageRepository.getProductCntByDate(storeId, date);
        //then

        System.out.println("================================================");
        System.out.println("productCntByDate = " + productCntByDate);
        System.out.println("================================================");
        System.out.println("list1 = " + list1);
    }

    @Test
    @DisplayName("get All store")
    void allStore() {
        //given

        //when
        List<StoreCheckDto> allStore = storeMyPageRepository.getAllStore();
        //then
        System.out.println("================================================");
        System.out.println("allStore = " + allStore);
    }
}