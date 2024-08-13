package org.nmfw.foodietree.domain.store.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.resp.UpdateAreaDto;
import org.nmfw.foodietree.domain.product.entity.Product;
import org.nmfw.foodietree.domain.product.entity.QProduct;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListByEndTimeDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListCo2Dto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListDto;
import org.nmfw.foodietree.domain.store.entity.QStore;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.dsl.Expressions.*;
import static org.nmfw.foodietree.domain.product.entity.QProduct.product;
import static org.nmfw.foodietree.domain.reservation.entity.QReservation.reservation;
import static org.nmfw.foodietree.domain.store.entity.QStore.store;

@Repository
@RequiredArgsConstructor
@Slf4j
public class StoreListRepositoryCustomImpl implements StoreListRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
//    private final FavAreaRepository favAreaRepository;

    @Override
    public List<StoreListDto> findStoresByCategory(StoreCategory category) {
        return jpaQueryFactory
                .selectFrom(store)
                .where(store.category.eq(category))
                .fetch()
                .stream()
                .map(StoreListDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<StoreListDto> findAllStoresByFavArea(List<UpdateAreaDto> favouriteAreas) {

        // 선호지역 주소에서 도시 부분만 추출
        List<String> favoriteCities = favouriteAreas.stream()
                .map(UpdateAreaDto::getPreferredArea)
                .map(this::extractCity)  // 도시 부분 추출하는 helper method
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        log.info("Favorite cities: {}", favoriteCities);

        // 선호 도시들을 기반으로 가게를 조회하는 쿼리 작성
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        for (String city : favoriteCities) {
            booleanBuilder.or(store.address.contains(city));
        }

        List<StoreListDto> stores = jpaQueryFactory
                .selectFrom(store)
                .where(booleanBuilder)
                .fetch()
                .stream()
                .map(StoreListDto::fromEntity)
                .collect(Collectors.toList());

        log.info("Stores found: {}", stores);
        return stores;
    }

    @Override
    public List<StoreListDto> findAllProductsStoreId() {

        return jpaQueryFactory
            .select(store, store.count())
            .from(product)
            .leftJoin(reservation).on(product.productId.eq(reservation.productId))
            .leftJoin(store).on(product.storeId.eq(store.storeId))
            .where(product.pickupTime.gt(LocalDateTime.now())
                .and(reservation.reservationTime.isNull()))
            .groupBy(product.storeId)
            .fetch()
            .stream()
            .map(tuple -> StoreListDto.fromEntity(tuple.get(store), tuple.get(store.count()).intValue()))
            .collect(Collectors.toList());
    }

    // 도시 부분을 추출하는 helper method - 현재는 데이터가 부족해 '시'로만 추출
    private String extractCity(String address) {
        String[] parts = address.split(" ");
        for (String part : parts) {
            if (part.endsWith("시")) {
                return part;
            }
        }
        return null;
    }

    @Override
    public List<StoreListCo2Dto> findAllStoresByProductCnt() {
        QProduct product = QProduct.product;
        QStore store = QStore.store;

        // 서브쿼리로 storeId별 유효한 상품 수를 계산
        List<String> storeIdsByProductCount = jpaQueryFactory
                .select(product.storeId)
                .from(product)
                .where(product.cancelByStore.isNull()) // 취소되지 않은 상품만 카운트
                .groupBy(product.storeId) // storeId별로 그룹화
                .orderBy(product.storeId.count().desc()) // 유효한 상품 수로 내림차순 정렬
                .fetch();

        // 위에서 구한 storeId를 기준으로 store 테이블에서 정보 가져오기
        return jpaQueryFactory
                .selectFrom(store)
                .where(store.storeId.in(storeIdsByProductCount)) // storeIdsByProductCount에 있는 storeId만 가져오기
                .fetch()
                .stream()
                .map(s -> {
                    // 유효한 상품 수 계산
                    int productCount = jpaQueryFactory
                            .select(product.count())
                            .from(product)
                            .where(product.storeId.eq(s.getStoreId())
                                    .and(product.cancelByStore.isNull()))
                            .fetchOne().intValue();

                    // CO2 계산 (예: 상품 수 * 0.12)
                    double coTwo = productCount * 0.12;
                    // StoreListDto 빌드 및 반환
                    return StoreListCo2Dto.builder()
                            .storeId(s.getStoreId())
                            .storeName(s.getStoreName())
                            .category(String.valueOf(s.getCategory()))
                            .address(s.getAddress())
                            .price(s.getPrice())
                            .storeImg(s.getStoreImg())
                            .productCnt(productCount) // 해당 storeId의 유효한 상품 수
                            .openAt(s.getOpenAt())
                            .closedAt(s.getClosedAt())
                            .limitTime(s.getLimitTime())
                            .emailVerified(s.getEmailVerified())
                            .productImg(s.getProductImg())
                            .coTwo(coTwo)
                            .build();
                })
                .sorted(Comparator.comparing(StoreListCo2Dto::getCoTwo).reversed()) // coTwo 값으로 내림차순 정렬
                .collect(Collectors.toList());
    }

    //current time - product end time = timeToExpiry
    // time to expiry 가 제일 적은 순으로 리스트 렌더링
    @Override
    public List<StoreListByEndTimeDto> findAllStoresByProductEndTime() {
        // 현재 날짜와 시간을 가져옵니다.
        LocalDate nowDate = LocalDate.now();
        LocalTime nowTime = LocalTime.now();

        // JPA Query Factory를 사용하여 쿼리를 구성합니다.
        QProduct product = QProduct.product;
        QStore store = QStore.store;

        // 1. 오늘 등록된 상품을 조회합니다.
        List<Product> todayProducts = jpaQueryFactory
                .selectFrom(product)
                .where(product.productUploadDate.between(nowDate.atStartOfDay(), nowDate.plusDays(1).atStartOfDay().minusNanos(1))
                        .and(product.cancelByStore.isNull())
                        .and(product.pickupStartTime.loe(nowTime))
                        .and(product.pickupEndTime.goe(nowTime)))
                .fetch();

        // 2. 상품을 남은 시간 기준으로 정렬합니다.
        List<Product> sortedProducts = todayProducts.stream()
                .sorted(Comparator.comparing(p -> calculateRemainingTime(nowTime, p.getPickupEndTime())))
                .collect(Collectors.toList());

        // 3. 상점 정보를 조회하고 DTO 객체를 생성합니다.
        return sortedProducts.stream()
                .collect(Collectors.groupingBy(Product::getStoreId, Collectors.counting())) // 각 상점의 상품 개수를 집계합니다.
                .entrySet().stream()
                .map(entry -> {
                    String storeId = entry.getKey();
                    long productCount = entry.getValue();

                    // 상점 정보를 조회합니다.
                    Store s = jpaQueryFactory
                            .selectFrom(store)
                            .where(store.storeId.eq(storeId))
                            .fetchOne();

                    // 상점의 첫 번째 상품을 가져옵니다.
                    Product firstProduct = sortedProducts.stream()
                            .filter(p -> p.getStoreId().equals(storeId))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Product not found"));

                    // 남은 시간을 계산합니다.
                    LocalTime endTime = LocalTime.from(firstProduct.getPickupEndTime());
                    Duration duration = calculateRemainingTime(nowTime, endTime);

                    // 남은 시간 포맷팅
                    long hours = duration.toHours();
                    long minutes = duration.toMinutes() % 60;
                    long seconds = duration.getSeconds() % 60;
                    String remainingTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);

                    // DTO 객체를 생성하여 반환합니다.
                    return StoreListByEndTimeDto.builder()
                            .storeId(s.getStoreId())
                            .storeName(s.getStoreName())
                            .category(String.valueOf(s.getCategory()))
                            .address(s.getAddress())
                            .price(s.getPrice())
                            .storeImg(s.getStoreImg())
                            .productCnt((int) productCount) // 상품 개수를 설정합니다.
                            .openAt(s.getOpenAt())
                            .closedAt(s.getClosedAt())
                            .limitTime(s.getLimitTime())
                            .emailVerified(s.getEmailVerified())
                            .productImg(s.getProductImg())
                            .remainingTime(remainingTime) // 남은 시간을 HH:mm:ss 형식으로 문자열로 설정
                            .build();
                })
                //리스트를 남은 시간 순으로 정렬
                .sorted(Comparator.comparing(StoreListByEndTimeDto::getRemainingTime))
                .collect(Collectors.toList());
    }


    /**
     * 현재 시간과 종료 시간 사이의 남은 시간을 계산
     */
    private Duration calculateRemainingTime(LocalTime now, LocalTime endTime) {
        // 현재 시간이 종료 시간보다 늦은 경우
        if (now.isAfter(endTime)) {
            return Duration.ZERO;
        }
        return Duration.between(now, endTime);
    }
}
