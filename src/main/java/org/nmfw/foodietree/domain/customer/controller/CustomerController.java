package org.nmfw.foodietree.domain.customer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.security.TokenProvider;
import org.nmfw.foodietree.domain.customer.dto.request.CustomerLoginDto;
import org.nmfw.foodietree.domain.customer.dto.request.SignUpDto;
import org.nmfw.foodietree.domain.customer.service.CustomerService;
import org.nmfw.foodietree.domain.customer.service.LoginResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.nmfw.foodietree.domain.customer.util.LoginUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/customer")
@Slf4j
@RequiredArgsConstructor
public class CustomerController {

    @Value("${env.kakao.api.key:default}")
    private String kakaoApiKey;
    private final CustomerService customerService;

    @PostMapping("/myFavMap")
    public ResponseEntity<Map<String, String>> getMyLocation(@RequestBody Map<String, String> payload) {
        String location = payload.get("location");
        if (location != null) {
            log.info("My location test: {}", location);
            return ResponseEntity.ok(Map.of("location", location));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Location is required"));
        }
    }



    //회원가입 양식 열기
//    @GetMapping("/sign-up")
//    public String signUp(Model model) {
//        log.info("customer/sign-up GET : forwarding to sign-up.jsp");
//        model.addAttribute("kakaoApiKey", kakaoApiKey);
//        return "/customer/sign-up";
//    }

    // 회원가입 요청 처리
    @PostMapping("/sign-up")
    public String signUp(@Validated SignUpDto dto, BindingResult result) {
        if (result.hasErrors()) {
            log.info("{}", result);
            return "redirect:/customer/sign-up";
        }

        boolean flag = customerService.join(dto);
        return flag ? "redirect:/customer/sign-in" : "redirect:/customer/sign-up";
    }

    // 아이디 중복검사
    @GetMapping("/check")
    @ResponseBody
    public ResponseEntity<?> check(String keyword) {
        log.info("keyword : {}", keyword);
        boolean flag = customerService.checkIdentifier(keyword);
        log.debug("중복체크 결과? {}", flag);
        return ResponseEntity.ok().body(flag);
    }

    // 로그인 양식 열기
    @GetMapping("/sign-in")
    public String signIn(HttpSession session) {

       if(LoginUtil.isLoggedIn(session)) {
           return"redirect:/";
       }

        log.info("/customer/sign-in GET : forwarding to sign-in.jsp");
        return "/sign-in";
    }

    //로그인 요청 처리
    @PostMapping("/sign-in")
    public String signIn(CustomerLoginDto dto,
                         RedirectAttributes ra,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        log.info("/customer/sign-in POST");
        log.debug("parameter: {}", dto);


        // 세션 얻기
        HttpSession session = request.getSession();
        System.out.println("\nsession = " + session);

        LoginResult result = customerService.authenticate(dto, session, response);
        System.out.println("\nresult = " + result);


        ra.addFlashAttribute("result", result);

        if (result == LoginResult.SUCCESS) { //참
            // 혹시 세션에 리다이렉트 URL이 있다면
            String redirect = (String) session.getAttribute("redirect");
            System.out.println("\nredirect = " + redirect);

            if (redirect != null) {
                session.removeAttribute("redirect");
                return "redirect:" + redirect;
            }
            return "redirect:/"; // 로그인 성공시
        }
        return "redirect:/customer/sign-in?message=signin-fail";
    }

    @GetMapping("/sign-out")
    public String signOut(HttpServletRequest request,
                          HttpServletResponse response) {
        // 세션 구하기
       HttpSession session = request.getSession();

        // 자동로그인 상태인지 확인
        if(LoginUtil.isAutoLogin(request)) {
            // 쿠키를 제거하고, DB에도 자동로그인 관련데이터를 원래대로 해놓음
            customerService.autoLoginClear(request, response);

        }

        // 세션에서 로그인 기록 삭제
        session.removeAttribute("login");

        // 세션을 초기화 (reset)
        session.invalidate();

        // 홈으로 보내기
        return "redirect:/";
    }

}
