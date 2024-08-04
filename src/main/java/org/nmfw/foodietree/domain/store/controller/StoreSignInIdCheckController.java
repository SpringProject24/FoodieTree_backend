package org.nmfw.foodietree.domain.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.service.CustomerService;
import org.nmfw.foodietree.domain.store.service.LoginIdCheckService;
import org.nmfw.foodietree.domain.store.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/store")
@Slf4j
@RequiredArgsConstructor
public class StoreSignInIdCheckController {

    private final StoreService storeService;

    // 아이디(이메일) 중복검사 비동기 요청 처리
    @GetMapping("/check")
    @CrossOrigin
    @ResponseBody
    public ResponseEntity<?> check(String keyword) {
        log.info("{}",  keyword);
        boolean flag = storeService.findOne(keyword);
        return ResponseEntity
                .ok()
                .body(flag);
    }

//    @GetMapping("/check-form")
//    public String check2() {
//        System.out.println("LoginIdCheckController.check");
//        return "LoginIdCheck";
//    }
}
