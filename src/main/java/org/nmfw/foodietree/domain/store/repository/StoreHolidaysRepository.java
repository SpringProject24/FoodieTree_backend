package org.nmfw.foodietree.domain.store.repository;

import org.nmfw.foodietree.domain.store.entity.StoreHolidays;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreHolidaysRepository
        extends JpaRepository<StoreHolidays, Long> {
    void deleteByStoreIdAndHolidays(String storeId, String holidays);
}
