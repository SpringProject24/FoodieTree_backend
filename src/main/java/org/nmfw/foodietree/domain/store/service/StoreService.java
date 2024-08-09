package org.nmfw.foodietree.domain.store.service;

import java.time.LocalDateTime;
import javax.servlet.http.Cookie;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeStoreDto;
import org.nmfw.foodietree.domain.customer.dto.request.AutoLoginDto;
import org.nmfw.foodietree.domain.customer.dto.resp.LoginUserInfoDto;
import org.nmfw.foodietree.domain.customer.entity.Customer;
import org.nmfw.foodietree.domain.customer.util.LoginUtil;
import org.nmfw.foodietree.domain.store.dto.request.LoginDto;
import org.nmfw.foodietree.domain.store.dto.request.LoginStoreInfoDto;
import org.nmfw.foodietree.domain.store.dto.request.StoreLoginDto;
import org.nmfw.foodietree.domain.store.dto.request.StoreSignUpDto;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.nmfw.foodietree.domain.store.mapper.StoreMapper;
import org.nmfw.foodietree.domain.store.repository.StoreRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.nmfw.foodietree.domain.customer.util.LoginUtil.AUTO_LOGIN_COOKIE;
import static org.nmfw.foodietree.domain.store.service.StoreLoginResult.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {
    private final StoreRepository storeRepository;

    @Transactional
    public void signUpUpdateStore(EmailCodeStoreDto emailCodeStoreDto) {

        storeRepository.updateRefreshTokenExpireDate(
                emailCodeStoreDto.getRefreshTokenExpireDate(),
                emailCodeStoreDto.getStoreId()
                );
    }

    @Transactional
    public void signUpSaveStore(EmailCodeStoreDto emailCodeStoreDto) {
        Store store = Store.builder()
                .storeId(emailCodeStoreDto.getStoreId())
                .userType(emailCodeStoreDto.getUserType())
                .refreshTokenExpireDate(emailCodeStoreDto.getRefreshTokenExpireDate())
                .build();

        storeRepository.save(store);
    }

    @Transactional(readOnly = true)
    public Store getStoreById(String storeId) {
        return storeRepository.findByStoreId(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + storeId));
    }

    @Transactional(readOnly = true)
    public boolean findOne(String email) {
        return storeRepository.existsByStoreId(email);
    }


    public void updateStore(LocalDateTime date, String email) {
        storeRepository.updateRefreshTokenExpireDate(date, email);
    }
}
