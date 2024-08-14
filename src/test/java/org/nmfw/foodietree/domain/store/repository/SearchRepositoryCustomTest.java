package org.nmfw.foodietree.domain.store.repository;

import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("inmem_test")
@SpringBootTest
@Transactional
@Rollback(value = true)
class SearchRepositoryCustomTest {
    private static final Logger log = LoggerFactory.getLogger(SearchRepositoryCustomTest.class);
    @Autowired
    StoreRepository storeRepository;

    @BeforeEach
    void before() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        List<Store> storeList = objectMapper.readValue(
                new File("src/test/resources/dummy.json"),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Store.class));
        storeRepository.saveAll(storeList);
    }

    @Test
    void 조회() {
        // given
        Pageable pageable = PageRequest.of(0, 3);
        String keyword = "강남";
        // when
        Page<Store> result = storeRepository.findStores(pageable, keyword);
        // then
        Assertions.assertAll(
                () -> assertNotNull(result.getContent()),
                () -> assertEquals(17, result.getTotalElements())
        );
        result.getContent().forEach(e -> log.info("{} {} {}", e, e.getStoreName(), e.getAddress()));
    }
}