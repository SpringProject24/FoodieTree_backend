package org.nmfw.foodietree.domain.store.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.admin.dto.res.StoreApproveDto;
import org.nmfw.foodietree.domain.store.dto.resp.ApprovalInfoDto;
import org.nmfw.foodietree.domain.store.entity.StoreApproval;
import org.nmfw.foodietree.domain.store.entity.value.ApproveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.nmfw.foodietree.domain.store.entity.QStore.store;
import static org.nmfw.foodietree.domain.store.entity.QStoreApproval.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class StoreApprovalRepositoryCustomImpl implements StoreApprovalRepositoryCustom{

    private final JPAQueryFactory factory;
    private final EntityManager em;

    @Override // 등록 요청 처리 상태에 따라 목록 조회 (가게 + 상품)
    public Page<ApprovalInfoDto> findApprovalsByStatus(
            // Pageable, String sort, ApproveStatus status
            Pageable pageable, ApproveStatus status
    ) {

        // status 따라 요청 목록 조회할 수 있도록 동적 쿼리 사용
        BooleanBuilder booleanBuilder = makeDynamicCondition(storeApproval.status, status);

        List<ApprovalInfoDto> list =
                selectToDto()
                .where(booleanBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 요청 수 조회
        Long count = factory
                .select(storeApproval.count())
                .from(storeApproval)
                .where(storeApproval.status.eq(ApproveStatus.PENDING))
                .fetchOne();

        if(count == null) count = 0L;

        return new PageImpl<>(list, pageable, count);
    }

    @Override // 사업자등록번호 검증하지 않은 요청 목록 조회
    public List<StoreApproval> findApprovalsByLicenseVerification() {

        // 어제 0시 0분
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0);

        return factory
            .selectFrom(storeApproval)
            .where(storeApproval.licenseVerification.eq(ApproveStatus.PENDING)
                .and(storeApproval.createdAt.after(yesterday)))
            .limit(100)
            .fetch();
    }

    // 해당 가게의 모든 요청 조회
    public List<ApprovalInfoDto> findDtoListByStoreId(String storeId) {
        return selectToDto()
            .from(storeApproval)
            .where(storeApproval.storeId.eq(storeId))
            .orderBy(storeApproval.id.desc())
            .fetch();
    }

    @Override // 기간 기준 조회
    public List<ApprovalInfoDto> findAllByDate(LocalDateTime startDate, LocalDateTime endDate) {

        List<ApprovalInfoDto> list = selectToDto()
                .where(storeApproval.createdAt
                        .between(startDate, endDate))
                .fetch();
        return list;
    }

    // select 결과를 dto로 담는 쿼리 분리
    private JPAQuery<ApprovalInfoDto> selectToDto() {
        return  factory
                .select(Projections.bean(
                        ApprovalInfoDto.class,
                        storeApproval.id.as("storeApprovalId"),
                        storeApproval.storeId.as("storeId"),
                        storeApproval.name.as("name"),
                        storeApproval.contact.as("contact"),
                        storeApproval.address.as("address"),
                        storeApproval.status.as("status"),
                        storeApproval.category.as("category"),
                        storeApproval.license.as("license"),
                        storeApproval.licenseVerification.as("licenseVerification"),
                        storeApproval.createdAt.as("createdAt"),
                        storeApproval.productCnt.as("productCnt"),
                        storeApproval.price.as("price"),
                        storeApproval.proImage.as("proImage")
                ))
                .from(storeApproval);
    }


    @Override // status(APPROVED, REJECTED)로 bulk update
    public Long updateApprovalStatus(ApproveStatus status, List<Long> ids) {
        Long result = factory
                .update(storeApproval)
                .set(storeApproval.status, status)
                .where(storeApproval.id.in(ids))
                .execute();
        em.flush();
        em.clear();
        return result;
    }

    public List<StoreApproval> findAllByIdInIds(List<Long> ids) {
        return factory
                .selectFrom(storeApproval)
                .where(storeApproval.id.in(ids))
                .fetch();
    }

    @Override
    public Long updateStoreInfo(List<StoreApproveDto> approvals) {
        int batchSize = 100; // Batch size
        int count = 0;
        long resultCnt = 0;

        for (StoreApproveDto sa : approvals) {
            long result = factory.update(store)
                    .set(store.category, sa.getCategory())
                    .set(store.address, sa.getAddress())
                    .set(store.approve, sa.getStatus())
                    .set(store.storeContact, sa.getContact())
                    .set(store.storeName, sa.getName())
                    .set(store.storeLicenseNumber, sa.getLicense())
                    .set(store.productCnt, sa.getProductCnt())
                    .set(store.price, sa.getPrice())
                    .set(store.productImg, sa.getProductImage()) // store 필드에 따라 변경 필요
                    .where(store.storeId.eq(sa.getStoreId()))
                    .execute();
            if(result > 0) resultCnt++;
            if (++count % batchSize == 0) {
                em.flush();
                em.clear();
            }
        }

        em.flush();
        em.clear();

        return resultCnt;
    }
    @Override
    public StoreApproval findRecentOneByStoreId(String storeId) {
        return factory.selectFrom(storeApproval)
                .where(storeApproval.storeId.eq(storeId))
                .orderBy(storeApproval.id.desc())
                .limit(1)
                .fetchOne();
    }


    // 동적 쿼리 eq 메서드
    private <T> BooleanBuilder makeDynamicCondition(PathBuilder<T> field, T value) {
        return new BooleanBuilder().and(field.eq(value));
    }

    private BooleanBuilder makeDynamicCondition(EnumPath<ApproveStatus> field, ApproveStatus value) {
        return new BooleanBuilder().and(field.eq(value)); // Enum에 사용
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
