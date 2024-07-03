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
//        StoreProductCountDto count = storeMyPageService.getStoreProductCnt(storeId);

        model.addAttribute("storeInfo", storeInfo);
        model.addAttribute("reservations", reservations);
        model.addAttribute("stats", stats);
//        model.addAttribute("count", count);
        return "store/store-mypage-test";
    }

    @GetMapping("/main/getProductCount")
    public ResponseEntity<StoreProductCountDto> getProductCount() {
        log.info("store my page get product count");
        StoreProductCountDto dto = storeMyPageService.getStoreProductCnt(storeId);
        log.debug(dto.toString());
        return ResponseEntity.ok().body(dto);
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

    @DeleteMapping("/main/calendar/undoHoliday")
    public ResponseEntity<?> undoHoliday(@RequestBody Map<String, String> requestBody){
        String holidayDate = requestBody.get("holidayDate");
        log.info("set holiday");
        boolean flag = storeMyPageService.undoHoliday(storeId, holidayDate);
        return flag? ResponseEntity.ok().body(true) : ResponseEntity.badRequest().body(false);
    }

    @GetMapping("/main/calendar/getHoliday")
    public List<StoreHolidayDto> getHolidays(String storeId) {
        log.info("store my page get closed date");
        List<StoreHolidayDto> holidays = storeMyPageService.getHolidays(storeId);
        return holidays;
    }

    // 해당 날짜가 휴무일이면 true 반환
    @PostMapping("/main/calendar/check/holiday")
    public ResponseEntity<?> checkHoliday(@RequestBody Map<String, String> requestBody) {
        String date = requestBody.get("date");
        log.info("check holiday");
        boolean isHoliday = storeMyPageService.checkHoliday(storeId, date);
        log.info("isHoliday = " + isHoliday);
        return ResponseEntity.ok().body(isHoliday);
    }

    @PostMapping("/main/calendar/setPickupTime")
    public ResponseEntity<?> setPickupTime(@RequestBody Map<String, String> requestBody){
        String pickupTime = requestBody.get("pickupTime");
        String date = requestBody.get("date");
        log.info("set pickup time");
        boolean flag = storeMyPageService.setPickupTime(storeId, date, pickupTime);
        return flag? ResponseEntity.ok().body(true) : ResponseEntity.badRequest().body(false);
    }

    @GetMapping("/main/getProductCnt")
    public ResponseEntity<StoreProductCountDto> getStoreProductCnt() {
        log.info("store my page get product count");
        StoreProductCountDto dto = storeMyPageService.getStoreProductCnt(storeId);
        log.debug(dto.toString());
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/main/updateProductCnt")
    public ResponseEntity<?> updateProductCnt(@RequestBody Map<String, Integer> requestBody){
        int productCnt = requestBody.get("newCount");
        log.info("update product count");
        boolean flag = storeMyPageService.updateProductCnt(storeId, productCnt);
        return flag? ResponseEntity.ok().body(true) : ResponseEntity.badRequest().body(false);
    }
}
