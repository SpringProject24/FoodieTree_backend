package org.nmfw.foodietree.domain.customer.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.customer.dto.resp.UpdateAreaDto;
import org.nmfw.foodietree.domain.customer.entity.FavArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class FavAreaServiceTest {

    @Autowired
    private FavAreaService FavAreaService;

    @Test
    @DisplayName("favArea save test")
    void saveAllTest() {
        //given
        List<UpdateAreaDto> areas = List.of(
                UpdateAreaDto.builder()
                        .preferredArea("서울")
                        .alias("우리집")
                        .build(),
                UpdateAreaDto.builder()
                        .preferredArea("부천시")
                        .alias("우리집2")
                        .build()
        );
        String customerId = "test@gmail.com";
        //when
        FavAreaService.saveAllFavAreas(customerId, areas);
        //then
    }
}