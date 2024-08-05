package org.nmfw.foodietree.domain.customer.repository;

import org.nmfw.foodietree.domain.customer.dto.resp.CustomerFavStoreDto;
import org.nmfw.foodietree.domain.customer.dto.resp.CustomerMyPageDto;
import org.nmfw.foodietree.domain.customer.dto.resp.PreferredFoodDto;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationDetailDto;

import java.util.List;

public interface CustomerMyPageRepositoryCustom {
    CustomerMyPageDto findCustomerDetails(String customerId);
    List<String> findPreferenceAreas(String customerId);
    List<PreferredFoodDto> findPreferenceFoods(String customerId);
    List<CustomerFavStoreDto> findFavStores(String customerId);
    List<ReservationDetailDto> findReservationsByCustomerId(String customerId);
}
