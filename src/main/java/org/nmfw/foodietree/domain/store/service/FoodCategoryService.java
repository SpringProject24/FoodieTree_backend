package org.nmfw.foodietree.domain.store.service;

import lombok.RequiredArgsConstructor;
import org.nmfw.foodietree.domain.store.entity.FoodCategory;
import org.nmfw.foodietree.domain.store.repository.FoodCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodCategoryService {

    private final FoodCategoryRepository categoryRepository;

    public List<FoodCategory> getAllCategories() {
        return categoryRepository.findAll();
    }
}
