package org.nmfw.foodietree.domain.store.controller;

import org.nmfw.foodietree.domain.store.dto.resp.StoreMyPageDto;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.store.service.StoreMyPageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/store/mypage")
public class StoreMyPageController {
    String storeId = "sji4205@naver.com";
    private final StoreMyPageService storeMyPageService;

    @RequestMapping("/main")
    public String main(HttpSession session
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response){

        log.info("store my page main");

        StoreMyPageDto storeInfo = storeMyPageService.getStoreMypageInfo(storeId);

        model.addAttribute("storeInfo", storeInfo);
        return "store/store-mypage-test";
    }



}
