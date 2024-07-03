package org.nmfw.foodietree.domain.store.controller;

import org.nmfw.foodietree.domain.store.dto.resp.*;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.store.service.StoreMyPageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/store/mypage")
public class StoreMyPageController {
    String storeId = "aaa@aaa.com";
    private final StoreMyPageService storeMyPageService;

    @GetMapping("/main")
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

    @GetMapping("/main/calendar/{dateString}")
    public ResponseEntity<?> getCalender(@RequestParam String dateString){
        log.info("store my page calendar");
        StoreMyPageDto storeMyPageInfo = storeMyPageService.getStoreMyPageInfo(storeId);
        return ResponseEntity.ok().body(storeMyPageInfo);
    }

    @GetMapping("/main/calendar/modal/{dateString}")
    public ResponseEntity<StoreMyPageCalendarModalDto> getCalenderModalDetail( @PathVariable String dateString){
        log.info("store my page calendar modal");

        StoreMyPageCalendarModalDto dto = storeMyPageService.getStoreMyPageCalendarModalInfo(storeId, dateString);
        log.info(dto.toString());
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/main/calendar/setHoliday")
    public ResponseEntity<?> closeStore(@RequestBody Map<String, String> requestBody){
        String holidayDate = requestBody.get("holidayDate");
        log.info("set holiday");
        boolean flag = storeMyPageService.setHoliday(storeId, holidayDate);
        return flag? ResponseEntity.ok().body(true) : ResponseEntity.badRequest().body(false);
    }

    @GetMapping("/main/calendar/getHoliday")
    public List<StoreHolidayDto> getHolidays(String storeId) {
        log.info("store my page get closed date");
        List<StoreHolidayDto> holidays = storeMyPageService.getHolidays(storeId);
        return holidays;
    }

    // 해당 날짜가 휴무일이면 true 반환
    @GetMapping("/main/calendar/check/holiday")
    public boolean checkHoliday(@RequestParam String date) {
        log.info("check holiday");
        return storeMyPageService.checkHoliday(storeId, date);
    }
}
