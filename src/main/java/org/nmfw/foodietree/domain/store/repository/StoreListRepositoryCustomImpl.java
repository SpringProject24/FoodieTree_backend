package org.nmfw.foodietree.domain.store.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.resp.UpdateAreaDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreListDto;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
}
