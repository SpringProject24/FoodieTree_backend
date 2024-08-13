package org.nmfw.foodietree.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.security.TokenProvider.TokenUserInfo;
import org.nmfw.foodietree.domain.auth.service.UserService;
import org.nmfw.foodietree.global.LoggedInUserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // user nickname, user image 등 로그인한 사람의 정보 서버측에서 가져오기
    @GetMapping("/info")
    public ResponseEntity<LoggedInUserInfoDto> getUserInfo(
            @AuthenticationPrincipal TokenUserInfo userInfo) {

        String email = userInfo.getUsername();
        String userType = userInfo.getRole();

        LoggedInUserInfoDto response = userService.getUserInfo(email, userType);

        return ResponseEntity.ok(response);
    }

}


