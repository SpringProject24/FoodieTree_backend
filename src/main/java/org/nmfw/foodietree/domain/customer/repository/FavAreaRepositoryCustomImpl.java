package org.nmfw.foodietree.domain.customer.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.resp.UpdateAreaDto;
import org.nmfw.foodietree.domain.customer.entity.FavArea;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
@Repository
@RequiredArgsConstructor
@Slf4j
public class FavAreaRepositoryCustomImpl implements FavAreaRepositoryCustom {

   private final FavAreaRepository favAreaRepository;

    @Override
    public List<UpdateAreaDto> findFavAreaByCustomerId(String customerId) {
        List<FavArea> byCustomerId = favAreaRepository.findByCustomerId(customerId);
        return byCustomerId.stream()
                .map(area -> UpdateAreaDto.builder()
                        .preferredArea(area.getPreferredArea())
                        .alias(area.getAlias())
                        .build())
                .collect(Collectors.toList());
    }

}
