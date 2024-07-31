package org.nmfw.foodietree.domain.store.entity.value;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum StoreCategory {
    //    한식, 양식, 카페, 일식, 디저트, 중식, 기타
    한식("한식"),
    양식("양식"),
    카페("카페"),
    일식("일식"),
    디저트("디저트"),
    중식("중식"),
    기타("기타");

    private final String foodType;

    public static StoreCategory fromString(String category) {
        try {
            return StoreCategory.valueOf(category);
        } catch (IllegalArgumentException e) {
            return StoreCategory.기타; // 기본값
        }
    }
}
