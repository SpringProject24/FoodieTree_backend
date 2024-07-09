package org.nmfw.foodietree.domain.store.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.util.LoginUtil;
import org.nmfw.foodietree.domain.store.dto.request.StoreLoginDto;
import org.nmfw.foodietree.domain.store.dto.request.StoreSignUpDto;
import org.nmfw.foodietree.domain.store.service.StoreLoginResult;
import org.nmfw.foodietree.domain.store.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.nmfw.foodietree.domain.store.service.StoreLoginResult.SUCCESS;

@Controller
@RequestMapping("/store")
@Slf4j
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/sign-in")
    public String login(HttpSession session
            , @RequestParam(required = false) String redirect) {
        session.setAttribute("redirect", redirect);
        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String login(StoreLoginDto dto,
                        HttpSession session,
                        HttpServletResponse response) {

        StoreLoginResult result = storeService.authenticate(dto, session, response);
        if (result == SUCCESS) {
            String redirect = (String) session.getAttribute("redirect");
            if (redirect != null) {
                session.removeAttribute("redirect");
                return "redirect:" + redirect;
            }
            return "redirect:/";
        }
        return "redirect:/store/sign-in?message=signin-fail";
    }

    @GetMapping("/sign-up")
    public String StoreSignUpForm() {
        return "store/sign-up";
    }

    /**
     * @param dto
     * @return StoreSignUpService에서 성공적으로 회원가입완료시 다음페이지로 이동
     */
    @PostMapping("/sign-up")
    public String storeSignUp(@Validated StoreSignUpDto dto, HttpSession session, BindingResult result) {
        log.info("/store-sign-up POST");
        log.info("parameter:{}", dto);
        if (result.hasErrors()) {
            return "redirect:/store/sign-up?message=signup-fail";
        }

        boolean flag = storeService.signUp(dto, session);
        if (!flag) {
            log.debug("회원가입 실패");
            return "redirect:/store/sign-up?message=signup-fail";
        }
        log.debug("회원가입 성공");
        return "redirect:/store/approval";
    }

    @GetMapping("/sign-out")
    public String signOut(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        if (LoginUtil.isAutoLogin(request)) {
            storeService.autoLoginClear(request, response);
        }
        session.removeAttribute("login");
        session.invalidate();
        return "redirect:/";
    }
}
