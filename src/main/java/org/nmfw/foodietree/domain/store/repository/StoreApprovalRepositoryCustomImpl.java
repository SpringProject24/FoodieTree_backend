package org.nmfw.foodietree.domain.store.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.store.entity.QStoreApproval;
import org.nmfw.foodietree.domain.store.entity.StoreApproval;
import org.nmfw.foodietree.domain.store.entity.value.ApproveStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.nmfw.foodietree.domain.store.entity.QStoreApproval.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class StoreApprovalRepositoryCustomImpl implements StoreApprovalRepositoryCustom{

    private final JPAQueryFactory factory;

    @Override // 등록 요청 처리 상태에 따라 목록 조회
    public Page<StoreApproval> findStoreApprovals(
            Pageable pageable, String sort, String storeId
    ) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        // status 따라 요청 목록 조회할 수 있도록 booleanBuilder 추가 필요

        List<StoreApproval> storeApprovals = factory
                .selectFrom(storeApproval)
                .where(storeApproval.status.eq(ApproveStatus.PENDING))
                .orderBy(specifier(sort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 요청 수 조회
        Long count = factory
                .select(storeApproval.count())
                .from(storeApproval)
                .where(storeApproval.status.eq(ApproveStatus.PENDING))
                .fetchOne();

        return new PageImpl<>(storeApprovals, pageable, count);
    }

    // 정렬 조건을 처리하는 메서드
    private OrderSpecifier<?> specifier(String sort) {
        switch (sort) {
            case "date":
                return storeApproval.createdAt.desc();
            case "store":
                return storeApproval.name.asc();
            case "category":
                return storeApproval.category.asc();
            default:
                return null;
        }
    }
}
