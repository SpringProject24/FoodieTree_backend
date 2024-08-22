package org.nmfw.foodietree.domain.issue.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class issueRepositoryCustomImplTest {

    private issueRepositoryCustomImpl issueRepositoryCustomImpl;

    @Test
    @DisplayName("")
    void updateCategory() {
        //given

        //when
        issueRepositoryCustomImpl.updateCategory(7L, "가게");
        //then
    }
}