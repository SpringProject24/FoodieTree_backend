package org.nmfw.foodietree.domain.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.review.controller.ReviewController;
import org.nmfw.foodietree.domain.review.dto.res.ReviewSaveDto;
import org.nmfw.foodietree.domain.review.entity.Hashtag;
import org.nmfw.foodietree.domain.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @WebMvcTest
 * - JPA 기능은 동작하지 않는다.
 * - 여러 스프링 테스트 어노테이션 중, Web(Spring MVC)에만 집중할 수 있는 어노테이션
 * - @Controller, @ControllerAdvice 사용 가능
 * - 단, @Service, @Repository등은 사용할 수 없다.
 * */
//@WebMvcTest(controllers = ReviewController.class)
@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerTest {


    /**
     * 웹 API 테스트할 때 사용
     * 스프링 MVC 테스트의 시작점
     * HTTP GET,POST 등에 대해 API 테스트 가능
     */
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private ReviewService reviewService;

    @Test
    @DisplayName("중복된 리뷰 등록 시 BadRequest 반환")
    void checkDuplicatedReviewId() throws Exception {
        // Given
        ReviewSaveDto reviewSaveDto = ReviewSaveDto.builder()
                .customerId("sinyunjong@gmail.com")
                .reservationId(616L)
                .storeImg("store_img.jpg")
                .reviewScore(5)
                .reviewImg("review_img.jpg")
                .reviewContent("Great experience!")
                .hashtags(Arrays.asList(Hashtag.FAST_SERVICE, Hashtag.PLEASANT_SURPRISE, Hashtag.EASY_TO_EAT))
                .build();

        // 중복된 리뷰가 이미 존재하는 상황을 모킹
        given(reviewService.isReviewExist(616L)).willReturn(true);

        /**
         * test 시 spring.security.enabled=false 설정하여 보안 해제
         */
        // When & Then
        // JSON 요청 본문 생성
        String reviewJson = "{ \"customerId\": \"sinyunjong@gmail.com\", \"reservationId\": 616, \"storeImg\": \"store_img.jpg\", \"reviewScore\": 5, \"reviewImg\": \"review_img.jpg\", \"reviewContent\": \"Great experience!\", \"hashtags\": [\"FAST_SERVICE\", \"PLEASANT_SURPRISE\", \"EASY_TO_EAT\"] }";

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/review/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reviewJson));

        // Then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().string("이미 작성된 리뷰가 있습니다."));

        // 응답 본문 출력
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("**************x*************************");
        System.out.println("응답 본문 = " + responseBody);
        System.out.println("***************************************");
    }
}