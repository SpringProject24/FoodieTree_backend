package org.nmfw.foodietree.domain.store.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.nmfw.foodietree.domain.store.entity.QStore.store;

@Repository
@RequiredArgsConstructor
@Slf4j
public class SearchRepositoryCustomImpl implements SearchRepositoryCustom {
    private final JPAQueryFactory factory;

    @Override
    public Page<Store> findStores(Pageable pageable, String keyword) {
        BooleanExpression expression = store.storeName.contains(keyword).or(store.address.contains(keyword));

        List<Store> result = factory
                .selectFrom(store)
                .where(expression)
                .orderBy(store.storeName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = factory
                .select(store.count())
                .from(store)
                .where(expression)
                .fetchOne();

        if (count == null) count = 0L;
        return new PageImpl<>(result, pageable, count);
    }
}
