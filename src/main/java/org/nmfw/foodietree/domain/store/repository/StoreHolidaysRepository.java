package org.nmfw.foodietree.domain.store.repository;

import org.nmfw.foodietree.domain.store.entity.StoreHolidays;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

public interface StoreHolidaysRepository
        extends JpaRepository<StoreHolidays, Long> {
    void deleteByStoreIdAndHolidays(String storeId, LocalDate holidays);

//    void deleteByStoreId(String storeId);

    List<StoreHolidays> findByStoreId(String storeId);
}
