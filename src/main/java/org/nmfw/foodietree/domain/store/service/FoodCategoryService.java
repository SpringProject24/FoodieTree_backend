package org.nmfw.foodietree.domain.store.service;

import lombok.RequiredArgsConstructor;
import org.nmfw.foodietree.domain.store.dto.resp.StoreCategoryDto;
import org.nmfw.foodietree.domain.store.entity.FoodCategory;
import org.nmfw.foodietree.domain.store.repository.FoodCategoryRepository;
import org.nmfw.foodietree.domain.store.entity.value.StoreCategory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodCategoryService {

    private final FoodCategoryRepository categoryRepository;

    public List<StoreCategoryDto> getAllCategories() {
        List<FoodCategory> categories = categoryRepository.findAll();

        // FoodCategory를 StoreCategoryDto로 변환
        return categories.stream()
                .map(category -> new StoreCategoryDto(
                        category.getFoodName(),
                        StoreCategory.fromString(category.getFoodName()).name()
                ))
                .collect(Collectors.toList());
    }
}