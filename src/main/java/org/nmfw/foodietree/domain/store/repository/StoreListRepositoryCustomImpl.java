package org.nmfw.foodietree.domain.store.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.store.entity.QStore;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.nmfw.foodietree.domain.store.entity.QStore.store;

@Repository
@RequiredArgsConstructor
@Slf4j
public class StoreListRepositoryCustomImpl implements StoreListRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Store> findStoresByCategory(String category) {
        //카테고리 별 분류
        return jpaQueryFactory
                .selectFrom(QStore.store)
                .where(store.category.eq(category))
                .fetch();
    }
}
