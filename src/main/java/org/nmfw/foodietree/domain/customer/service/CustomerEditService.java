package org.nmfw.foodietree.domain.customer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.resp.UpdateDto;
import org.nmfw.foodietree.domain.customer.entity.Customer;
import org.nmfw.foodietree.domain.customer.entity.FavArea;
import org.nmfw.foodietree.domain.customer.entity.FavFood;
import org.nmfw.foodietree.domain.customer.entity.FavStore;
import org.nmfw.foodietree.domain.customer.repository.CustomerEditRepository;
import org.nmfw.foodietree.domain.customer.repository.FavAreaRepository;
import org.nmfw.foodietree.domain.customer.repository.FavFoodRepository;
import org.nmfw.foodietree.domain.customer.repository.FavStoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerEditService {

    private final CustomerEditRepository customerEditRepository;
    private final FavFoodRepository favFoodRepository;
    private final FavAreaRepository favAreaRepository;
    private final FavStoreRepository favStoreRepository;

    public boolean insertPreferredInfo(String customerId, UpdateDto dto) {
        String type = dto.getType();
        String value = dto.getValue();
        if (type.equals("food")) {
            FavFood build = FavFood.builder()
                    .customerId(customerId)
                    .preferredFood(value).build();
            favFoodRepository.save(build);
            return true;
        } else if (type.equals("area")) {
            FavArea build = FavArea.builder()
                    .customerId(customerId)
                    .preferredArea(value)
                    .build();
            favAreaRepository.save(build);
            return true;
        } else if (type.equals("store")) {
            FavStore build = FavStore.builder()
                    .customerId(customerId)
                    .storeId(value)
                    .build();
            favStoreRepository.save(build);
            return true;
        }
        return false;
    }

    public boolean updateProfileInfo(String customerId, UpdateDto dto) {
        String type = dto.getType();
        String value = dto.getValue();
        Customer foundTarget = customerEditRepository.findByCustomerId(customerId);
        if (type.equals("nickname")) {
            foundTarget.setNickname(value);
            customerEditRepository.save(foundTarget);
            return true;
        } else if (type.equals("customer_phone_number")) {
            foundTarget.setCustomerPhoneNumber(value);
            customerEditRepository.save(foundTarget);
            return true;
        }
        return false;
    }

    public boolean deleteProfileInfo(String customerId, UpdateDto dto) {
        String type = dto.getType();
        String value = dto.getValue();
        if (type.equals("food")) {
            favFoodRepository.deleteByCustomerIdAndPreferredFood(customerId, value);
            return true;
        } else if (type.equals("area")) {
            favAreaRepository.deleteByCustomerIdAndPreferredArea(customerId, value);
            return true;
        } else if (type.equals("store")) {
            favStoreRepository.deleteByCustomerIdAndStoreId(customerId, value);
            return true;
        }
        return false;
    }
}
