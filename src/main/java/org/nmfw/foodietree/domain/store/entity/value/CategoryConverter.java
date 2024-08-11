package org.nmfw.foodietree.domain.store.entity.value;

import javax.persistence.AttributeConverter;

public class CategoryConverter implements AttributeConverter<StoreCategory, String> {

	@Override
	public String convertToDatabaseColumn(StoreCategory storeCategory) {
		return storeCategory != null ? storeCategory.getFoodType() : null;
	}

	@Override
	public StoreCategory convertToEntityAttribute(String s) {
		return StoreCategory.fromString(s);
	}
}
