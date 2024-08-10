package org.nmfw.foodietree.domain.store.service.StoreList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class StoreListServiceTest {

    @Autowired
    StoreListService storeListService;

    @Test
    @DisplayName("선호지역에 기반한 가게 출력")
    void getStoreByFavAreas() {
        //given
        String customerId="test@gmail.com";
        //when
        List<StoreListDto> allStores = storeListService.getAllStores(customerId);
        //then
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("allStores = " + allStores);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}