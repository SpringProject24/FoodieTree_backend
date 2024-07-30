package org.nmfw.foodietree.domain.customer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.entity.FavArea;
import org.nmfw.foodietree.domain.customer.repository.FavAreaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FavAreaService {

    private final FavAreaRepository favAreaRepository;

    public void saveAllFavAreas(String customerId, List<String> favAreas) {
        List<FavArea> areasToSave = favAreas.stream()
                .map(area -> FavArea.builder()
                        .customerId(customerId)
                        .preferredArea(area)
                        .build())
                .collect(Collectors.toList());

        favAreaRepository.saveAll(areasToSave);
    }
}
