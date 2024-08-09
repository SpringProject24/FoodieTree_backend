package org.nmfw.foodietree.domain.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.store.service.LoginIdCheckService;
import org.nmfw.foodietree.domain.store.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/store")
@Slf4j
@RequiredArgsConstructor
public class StoreSignInIdCheckController {

    private final LoginIdCheckService loginIdCheckService;
    private final StoreService storeService;

//    // 아이디(이메일) 중복검사 비동기 요청 처리
@GetMapping("/check")
@CrossOrigin
@ResponseBody
public ResponseEntity<?> check(@RequestParam String email) {
    log.info("이메일 중복체크 아이디 : {}", email);
    boolean flag = storeService.existByStoreEmail(email);
    log.info("이메일 중복체크  결과 {}", flag);
    return ResponseEntity
            .ok()
            .body(flag);
}

    @GetMapping("test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok().body(true);
    }

    @GetMapping("/check-form")
    public String check2() {
        System.out.println("LoginIdCheckController.check");
        return "LoginIdCheck";
    }
}
