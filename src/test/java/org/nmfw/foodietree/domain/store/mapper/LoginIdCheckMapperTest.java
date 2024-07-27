package org.nmfw.foodietree.domain.store.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class LoginIdCheckMapperTest {
    @Autowired
    LoginIdCheckMapper repo;
    @Autowired
    StoreMapper storeMapper;

//    @BeforeEach
//    void before() {
//        storeMapper.save(
//                Store.builder()
//                        .storeId("test")
//                        .password("test")
//                        .category("test")
//                        .address("test")
//                        .approve("test")
//                        .warningCount(0)
//                        .price(0)
//                        .businessNumber("test")
//                        .build()
//        );
//    }
//    @Test
//    void exists() {
//        boolean test = repo.existsById("account", "test3");
//        Assertions.assertThat(test).isFalse();
//        System.out.println("hi");
//    }


}