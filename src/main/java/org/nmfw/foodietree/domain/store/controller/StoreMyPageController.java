package org.nmfw.foodietree.domain.store.controller;

import org.nmfw.foodietree.domain.store.dto.resp.StoreMyPageDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreReservationDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreStatsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.store.service.StoreMyPageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/store/mypage")
public class StoreMyPageController {
    String storeId = "aaa@aaa.com";
    private final StoreMyPageService storeMyPageService;

    @RequestMapping("/main")
    public String main(HttpSession session
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response){

        log.info("store my page main");

        StoreMyPageDto storeInfo = storeMyPageService.getStoreMyPageInfo(storeId);
        List<StoreReservationDto> reservations = storeMyPageService.findReservations(storeId);
        StoreStatsDto stats = storeMyPageService.getStats(storeId);

        model.addAttribute("storeInfo", storeInfo);
        model.addAttribute("reservations", reservations);
        model.addAttribute("stats", stats);
        return "store/store-mypage-test";
    }

    @GetMapping("/main/calendar")
    public ResponseEntity<?> getCalender(@RequestParam String storeId, @RequestParam String date){
        log.info("store my page calendar");
        return ResponseEntity.ok().body(storeMyPageService.getStoreMyPageInfo(storeId));
    }
}
