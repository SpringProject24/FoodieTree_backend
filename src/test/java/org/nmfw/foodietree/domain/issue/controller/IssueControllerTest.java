package org.nmfw.foodietree.domain.issue.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@Transactional
@SpringBootTest
//@RequiredConstructor
class IssueControllerTest {

    private static final Logger log = LoggerFactory.getLogger(IssueControllerTest.class);
    @Autowired
    private IssueController issueController;

    @Test
    @DisplayName("이슈 저장 테스트")
    void saveTest() {
        //given
        Map<String, String> issue = Map.of("customerId", "test@gmail.com", "reservationId", "38");
        //when
        ResponseEntity<?> issue1 = issueController.issue(issue);
        //then
        System.out.println("issue1 = " + issue1);
    }

    @Test
    @DisplayName("이슈 카테고리 수정 테스트")
    void updateCategoryTest() {
        //given

        //when
        issueController.updateIssueCategory(Map.of("issueId", "7", "issueCategory", "가게"));
        //then
    }
}