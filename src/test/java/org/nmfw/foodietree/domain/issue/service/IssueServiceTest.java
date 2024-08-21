package org.nmfw.foodietree.domain.issue.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.issue.entity.Issue;
import org.nmfw.foodietree.domain.issue.repository.IssueRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@SpringBootTest
class IssueServiceTest {

    private IssueService issueService;
    private IssueRepository issueRepository;

    @Test
    @DisplayName("update issue category")
    void updateTest() {
        //given
        Long issueId = 7L;
        String category = "가게";
        //when
        issueService.updateCategory(issueId, category);
        Optional<Issue> byId = issueRepository.findById(issueId);
        //then
        System.out.println("byId = " + byId);

    }
}