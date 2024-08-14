package org.nmfw.foodietree.domain.store.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreCategoryDto {
    private String foodType;    // 한글 카테고리명
    private String englishName; // 영어 카테고리명
}
