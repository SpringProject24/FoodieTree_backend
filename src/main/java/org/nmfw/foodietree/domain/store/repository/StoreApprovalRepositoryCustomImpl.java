package org.nmfw.foodietree.domain.store.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.store.dto.resp.ApprovalInfoDto;
import org.nmfw.foodietree.domain.store.entity.StoreApproval;
import org.nmfw.foodietree.domain.store.entity.value.ApproveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static org.nmfw.foodietree.domain.store.entity.QStoreApproval.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class StoreApprovalRepositoryCustomImpl implements StoreApprovalRepositoryCustom{

    private final JPAQueryFactory factory;

    @Override // 등록 요청 처리 상태에 따라 목록 조회 (가게 + 상품)
    public Page<ApprovalInfoDto> findApprovalsByStatus(
            // Pageable, String sort, ApproveStatus status
            Pageable pageable, ApproveStatus status
    ) {

        // status 따라 요청 목록 조회할 수 있도록 동적 쿼리 사용
        BooleanBuilder booleanBuilder = makeDynamicStoreStatus(status);

        List<ApprovalInfoDto> list = factory
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
                .from(storeApproval)
//                .innerJoin(productApproval).on(productApproval.storeId.eq(storeApproval.storeId))
                .where(booleanBuilder)
//                .orderBy(specifier(sort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 요청 수 조회
        Long count = factory
                .select(storeApproval.count())
                .from(storeApproval)
//                .innerJoin(productApproval).on(productApproval.storeId.eq(storeApproval.storeId))
                .where(storeApproval.status.eq(ApproveStatus.PENDING))
                .fetchOne();

        if(count == null) count = 0L;

        return new PageImpl<>(list, pageable, count);
    }

    @Override // 사업자등록번호 검증하지 않은 요청 목록 조회
    public List<StoreApproval> findApprovalsByLicenseVerification() {

        // 어제 이후 요청
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
//        LocalDateTime startOfToday = LocalDateTime.now().with(LocalDate.MIN);

        return factory
                .selectFrom(storeApproval)
                .where(storeApproval.licenseVerification.eq(ApproveStatus.PENDING)
                    .and(storeApproval.createdAt.after(yesterday)))
                .limit(100)
                .fetch();
    }

    @Override // 해당 가게의 승인 요청 조회
    public ApprovalInfoDto findApprovalsByStoreId(String storeId) {

        BooleanBuilder booleanBuilder = makeDynamicStoreStatus(ApproveStatus.APPROVED);

        ApprovalInfoDto infoDto = factory
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
                .from(storeApproval)
//                .innerJoin(productApproval).on(productApproval.storeId.eq(storeApproval.storeId))
                .where(booleanBuilder.and(storeApproval.storeId.eq(storeId)))
                .fetchOne();

        return infoDto;
    }

    @Override
    public List<ApprovalInfoDto> findAllByDate() {
        return List.of();
    }

    // storeApproval 동적 쿼리 메서드
    private BooleanBuilder makeDynamicStoreStatus(ApproveStatus status) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(status == ApproveStatus.PENDING) {
            booleanBuilder.and(storeApproval.status.eq(ApproveStatus.PENDING));
        }
        if(status == ApproveStatus.APPROVED) {
            booleanBuilder.and(storeApproval.status.eq(ApproveStatus.APPROVED));
        }
        if(status == ApproveStatus.REJECTED) {
            booleanBuilder.and(storeApproval.status.eq(ApproveStatus.REJECTED));
        }

        return booleanBuilder;
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
