package org.nmfw.foodietree.domain.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.store.dto.resp.StoreMyPageDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreStatsDto;
import org.nmfw.foodietree.domain.store.service.StoreMyPageEditService;
import org.nmfw.foodietree.domain.store.service.StoreMyPageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/store/mypage/edit")
public class StoreMyPageEditController {
    String storeId = "sji4205@naver.com";

    private final StoreMyPageEditService storeMyPageEditService;

    @GetMapping("/main")
    public String main(
            HttpSession session
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response
    ) {
        log.info("store my page edit main");

        StoreMyPageDto storeInfo = storeMyPageEditService.getStoreMyPageInfo(storeId);
        StoreStatsDto stats = storeMyPageEditService.getStats(storeId);

        model.addAttribute("storeInfo", storeInfo);
        model.addAttribute("stats", stats);

        return "store/store-mypage-edit-test";
    }
}
