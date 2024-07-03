package org.nmfw.foodietree.domain.customer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapController {
    @GetMapping("/kakao")
    public String kakaoMapApi(HttpSession session) {
        // 세션에서 로그인된 사용자 ID 가져오기
        String storeId = (String) session.getAttribute("storeId");

        return "/customer/kakaoMap";
    }
}
