package org.nmfw.foodietree.domain.store.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nmfw.foodietree.domain.product.entity.Product;
import org.nmfw.foodietree.domain.product.repository.ProductRepository;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListDto;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@DataJpaTest
class StoreListRepositoryCustomImplTest {

//
//    @Autowired
//    private StoreRepository storeRepository; // StoreRepository 인터페이스
//
//    @Autowired
//    private ProductRepository productRepository; // ProductRepository 인터페이스
//
//    @Autowired
//    private JPAQueryFactory jpaQueryFactory;
//
//    @Test
//    public void testFindAllStoresByProductCnt() {
//        // given: 테스트용 데이터 준비
//        Store store1 = new Store("store1", ...); // 필수 필드 추가
//        Store store2 = new Store("store2", ...);
//        storeRepository.save(store1);
//        storeRepository.save(store2);
//
//        Product product1 = new Product("store1", ...); // 필수 필드 추가
//        Product product2 = new Product("store1", ...);
//        Product product3 = new Product("store2", ...);
//        productRepository.save(product1);
//        productRepository.save(product2);
//        productRepository.save(product3);
//
//        // when: 쿼리 메서드를 실행
//        List<StoreListDto> result = storeRepository.findA();
//
//        // then: 결과 검증
//        assertThat(result).isNotEmpty();
//        assertThat(result.get(0).getStoreId()).isEqualTo("store1");
//        assertThat(result.get(0).getProductCnt()).isEqualTo(2); // store1이 2개의 유효한 제품이 있다고 가정
//        assertThat(result.get(1).getStoreId()).isEqualTo("store2");
//        assertThat(result.get(1).getProductCnt()).isEqualTo(1);
//    }

}