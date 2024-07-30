package org.nmfw.foodietree.domain.store.service;

import java.time.LocalDateTime;
import javax.servlet.http.Cookie;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
    private final StoreMapper storeMapper;
    private final PasswordEncoder encoder;

    public boolean signUp(StoreSignUpDto dto, HttpSession session) {
        Store store = dto.toEntity();
//        String encodedPassword = encoder.encode(dto.getPassword());
//        store.setPassword(encodedPassword);
        boolean flag = storeMapper.storeSave(store);
        if (!flag) {
            return false;
        }
        StoreService.maintainLoginState(session, store);
        return true;
    }

    public StoreLoginResult authenticate(StoreLoginDto dto, HttpSession session, HttpServletResponse response) {
        String storeId = dto.getStoreId();
        Store foundStore = storeMapper.findOne(storeId);
        if (foundStore == null) {
            log.info("{} - 회원가입이 필요합니다.", storeId);
            return NO_ACC;
        }

        String inputPassword = dto.getPassword();
//        String originPassword = foundStore.getPassword();
//        if (!encoder.matches(inputPassword, originPassword)) {
//            log.info("비밀번호가 일치하지 않습니다");
//            return NO_PW;
//        }

        if (dto.isAutoLogin()) {
            String sessionId = session.getId();
            Cookie autoLoginCookie = new Cookie(LoginUtil.AUTO_LOGIN_COOKIE, sessionId);
            autoLoginCookie.setPath("/"); // 쿠키를 사용할 경로
            autoLoginCookie.setMaxAge(60 * 60 * 24 * 90); // 자동로그인 유지 시간
            response.addCookie(autoLoginCookie);
            storeMapper.updateAutoLogin(
                    AutoLoginDto.builder()
                            .sessionId(sessionId)
                            .limitTime(LocalDateTime.now().plusDays(90))
                            .id(storeId)
                            .build()
            );
        }
        maintainLoginState(session, foundStore);
        return SUCCESS;
    }

    public static void maintainLoginState(HttpSession session, Store found) {
        session.setMaxInactiveInterval(60 * 60);
        session.setAttribute("login", new LoginStoreInfoDto(found));
    }

    public void autoLoginClear(HttpServletRequest request,
                               HttpServletResponse response) {

        Cookie c = WebUtils.getCookie(request, AUTO_LOGIN_COOKIE);
        if (c != null) {
            c.setPath("/");
            c.setMaxAge(0);
            response.addCookie(c);
            storeMapper.updateAutoLogin(
                    AutoLoginDto.builder()
                            .sessionId("none")
                            .limitTime(LocalDateTime.now())
                            .id(LoginUtil.getLoggedInUser(request.getSession()))
                            .build()
            );
        }
    }
}
