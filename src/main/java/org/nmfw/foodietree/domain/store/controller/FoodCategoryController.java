package org.nmfw.foodietree.domain.store.controller;

import lombok.RequiredArgsConstructor;
import org.nmfw.foodietree.domain.store.dto.resp.StoreCategoryDto;
import org.nmfw.foodietree.domain.store.service.FoodCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class FoodCategoryController {

    private final FoodCategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<StoreCategoryDto>> getCategories() {
        List<StoreCategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}
