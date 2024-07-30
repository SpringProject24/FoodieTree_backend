package org.nmfw.foodietree.domain.customer.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.customer.entity.FavArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class FavAreaServiceTest {

    @Autowired
    private FavAreaService FavAreaService;

    @Test
    @DisplayName("favArea save test")
    void saveAllTest() {
        //given
        List<String> areas = List.of("테스트 코드", "부천시 원미구");
        String customerId = "test@gmail.com";
        //when
        FavAreaService.saveAllFavAreas(customerId, areas);
        //then
    }
}