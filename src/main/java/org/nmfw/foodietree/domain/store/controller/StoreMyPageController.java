package org.nmfw.foodietree.domain.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.store.dto.resp.*;
import org.nmfw.foodietree.domain.store.service.StoreMyPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreMyPageController {

    private final StoreMyPageService storeMyPageService;

    // 하드코딩된 storeId
    private final String storeId = "thdghtjd115@naver.com";

    /**
     * 현재 인증된 사용자로부터 storeId를 추출하는 메서드
     * 추후 토큰 기반 인증으로 사용 예정
     * @return storeId
     */
    /*
    private String getStoreIdFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("Invalid authentication token");
        }
        return authentication.getName(); // Assuming the storeId is the username in the token
    }
    */

    /**
     * 가게의 마이페이지 정보를 가져오는 GET 요청 처리
     * @return 가게의 마이페이지 정보 DTO
     */
    @GetMapping("/info")
    public ResponseEntity<StoreMyPageDto> getStoreMyPageInfo() {
        log.info("Fetching store my page info for storeId: {}", storeId);
        // String storeId = getStoreIdFromToken(); // 주석처리된 부분
        StoreMyPageDto storeInfo = storeMyPageService.getStoreMyPageInfo(storeId);
        return ResponseEntity.ok().body(storeInfo);
    }

    /**
     * 가게의 통계 정보를 가져오는 GET 요청 처리
     * @return 가게의 통계 정보 DTO
     */
    @GetMapping("/stats")
    public ResponseEntity<StoreStatsDto> getStoreStats() {
        log.info("Fetching store stats for storeId: {}", storeId);
        // String storeId = getStoreIdFromToken(); // 토큰정보 사용 시 주석 해제
        StoreStatsDto storeStats = storeMyPageService.getStats(storeId);
        return ResponseEntity.ok().body(storeStats);
    }

    /**
     * 가게의 예약 목록을 가져오는 GET 요청 처리
     * @return 예약 목록 DTO 리스트
     */
    @GetMapping("/reservations")
    public ResponseEntity<List<StoreReservationDto>> getStoreReservations() {
        log.info("Fetching reservations for storeId: {}", storeId);
        // String storeId = getStoreIdFromToken(); // 주석처리된 부분
        List<StoreReservationDto> reservations = storeMyPageService.findReservations(storeId);
        return ResponseEntity.ok().body(reservations);
    }

    /**
     * 가게의 오늘의 랜덤박스 개수 정보를 가져오는 GET 요청 처리
     * @return 오늘의 랜덤박스 개수 정보 DTO
     */
    @GetMapping("/getProductCount")
    public ResponseEntity<StoreProductCountDto> getProductCount() {
        log.info("store my page get product count");
        // String storeId = getStoreIdFromToken(); // 토큰정보 사용 시 주석 해제
        StoreProductCountDto dto = storeMyPageService.getStoreProductCnt(storeId);
        log.debug(dto.toString());
        return ResponseEntity.ok().body(dto);
    }

    /**
     * 오늘의 랜덤박스 개수를 업데이트하는 POST 요청 처리
     * @param requestBody 업데이트할 랜덤박스 개수를 포함한 요청 본문
     * @return 성공 여부에 따라 HTTP 상태 코드 반환
     */
    @PostMapping("/updateProductCnt")
    public ResponseEntity<?> updateProductCnt(@RequestBody Map<String, Integer> requestBody) {
        int productCnt = requestBody.get("newCount");
        log.info("update product count");
        // String storeId = getStoreIdFromToken(); // 토큰정보 사용 시 주석 해제
        boolean flag = storeMyPageService.updateProductCnt(storeId, productCnt);
        return flag ? ResponseEntity.ok().body(true) : ResponseEntity.badRequest().body(false);
    }

    /**
     * 가게의 특정 날짜에 대한 캘린더 정보를 가져오는 GET 요청 처리
     * @param dateString 요청한 날짜 문자열
     * @return 캘린더 정보 DTO
     */
    @GetMapping("/calendar/{dateString}")
    public ResponseEntity<StoreMyPageDto> getCalendar(@PathVariable String dateString) {
        log.info("store my page calendar");
        // String storeId = getStoreIdFromToken(); // 주석처리된 부분
        StoreMyPageDto storeMyPageInfo = storeMyPageService.getStoreMyPageInfo(storeId);
        return ResponseEntity.ok().body(storeMyPageInfo);
    }

    /**
     * 가게의 특정 날짜에 대한 캘린더 모달 상세 정보를 가져오는 GET 요청 처리
     * @param dateString 요청한 날짜 문자열
     * @return 캘린더 모달 상세 정보 DTO
     */
    @GetMapping("/calendar/modal/{dateString}")
    public ResponseEntity<StoreMyPageCalendarModalDto> getCalendarModalDetail(@PathVariable String dateString) {
        log.info("store my page calendar modal");
        // String storeId = getStoreIdFromToken(); // 주석처리된 부분
        StoreMyPageCalendarModalDto dto = storeMyPageService.getStoreMyPageCalendarModalInfo(storeId, dateString);
        log.info(dto.toString());
        return ResponseEntity.ok().body(dto);
    }

    /**
     * 특정 날짜를 휴무일로 설정하는 POST 요청 처리
     * @param requestBody 휴무일로 설정할 날짜를 포함한 요청 본문
     * @return 성공 여부에 따라 HTTP 상태 코드 반환
     */
    @PostMapping("/calendar/setHoliday")
    public ResponseEntity<?> closeStore(@RequestBody Map<String, String> requestBody) {
        String holidayDate = requestBody.get("holidayDate");
        log.info("set holiday");
        // String storeId = getStoreIdFromToken(); // 주석처리된 부분
        boolean flag = storeMyPageService.setHoliday(storeId, holidayDate);
        return flag ? ResponseEntity.ok().body(true) : ResponseEntity.badRequest().body(false);
    }

    /**
     * 특정 날짜의 휴무일 설정을 취소하는 DELETE 요청 처리
     * @param requestBody 휴무일 설정을 취소할 날짜를 포함한 요청 본문
     * @return 성공 여부에 따라 HTTP 상태 코드 반환
     */
    @DeleteMapping("/calendar/undoHoliday")
    public ResponseEntity<?> undoHoliday(@RequestBody Map<String, String> requestBody) {
        String holidayDate = requestBody.get("holidayDate");
        log.info("set holiday");
        // String storeId = getStoreIdFromToken(); // 주석처리된 부분
        boolean flag = storeMyPageService.undoHoliday(storeId, holidayDate);
        return flag ? ResponseEntity.ok().body(true) : ResponseEntity.badRequest().body(false);
    }

    /**
     * 가게의 휴무일 목록을 가져오는 GET 요청 처리
     * @return 휴무일 목록 DTO 리스트
     */
    @GetMapping("/calendar/getHoliday")
    public ResponseEntity<List<StoreHolidayDto>> getHolidays() {
        log.info("store my page get closed date");
        // String storeId = getStoreIdFromToken(); // 주석처리된 부분
        List<StoreHolidayDto> holidays = storeMyPageService.getHolidays(storeId);
        return ResponseEntity.ok().body(holidays);
    }

    /**
     * 특정 날짜가 휴무일인지 확인하는 POST 요청 처리
     * @param requestBody 확인할 날짜를 포함한 요청 본문
     * @return 해당 날짜가 휴무일인지 여부를 나타내는 boolean 값
     */
    @PostMapping("/calendar/check/holiday")
    public ResponseEntity<?> checkHoliday(@RequestBody Map<String, String> requestBody) {
        String date = requestBody.get("date");
        log.info("check holiday");
        // String storeId = getStoreIdFromToken(); // 주석처리된 부분
        boolean isHoliday = storeMyPageService.checkHoliday(storeId, date);
        log.info("isHoliday = " + isHoliday);
        return ResponseEntity.ok().body(isHoliday);
    }

    /**
     * 특정 날짜의 픽업 시간을 설정하는 POST 요청 처리
     * @param requestBody 설정할 픽업 시간을 포함한 요청 본문
     * @return 성공 여부에 따라 HTTP 상태 코드 반환
     */
    @PostMapping("/calendar/setPickupTime")
    public ResponseEntity<?> setPickupTime(@RequestBody Map<String, String> requestBody) {
        String pickupTime = requestBody.get("pickupTime");
        String date = requestBody.get("date");
        log.info("set pickup time");
        // String storeId = getStoreIdFromToken(); // 주석처리된 부분
        boolean flag = storeMyPageService.setPickupTime(storeId, date, pickupTime);
        return flag ? ResponseEntity.ok().body(true) : ResponseEntity.badRequest().body(false);
    }

    /**
     * 특정 일자의 픽업 완료된 랜덤박스 개수를 가져오는 GET 요청 처리
     * @param dateString 특정 일자
     * @return 픽업 완료된 랜덤박스 개수
     */
    @GetMapping("/countPickedUpProducts/{dateString}")
    public ResponseEntity<Integer> countPickedUpProductsByDate(@PathVariable String dateString) {
        log.info("Counting picked up products for date: {}", dateString);
        // String storeId = getStoreIdFromToken(); // 토큰정보 사용 시 주석 해제
        int count = storeMyPageService.countPickedUpProductsByDate(storeId, dateString);
        return ResponseEntity.ok().body(count);
    }
}