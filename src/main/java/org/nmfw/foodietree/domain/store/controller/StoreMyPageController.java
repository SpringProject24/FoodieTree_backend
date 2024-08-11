package org.nmfw.foodietree.domain.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.security.TokenProvider;
import org.nmfw.foodietree.domain.auth.security.TokenProvider.TokenUserInfo;
import org.nmfw.foodietree.domain.customer.dto.resp.UpdateDto;
import org.nmfw.foodietree.domain.store.dto.resp.*;
import org.nmfw.foodietree.domain.store.service.StoreMyPageEditService;
import org.nmfw.foodietree.domain.store.service.StoreMyPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreMyPageController {

    private final StoreMyPageService storeMyPageService;
    private final StoreMyPageEditService storeMyPageEditService;

    // 하드코딩된 storeId
    private final String storeId = "thdghtjd115@naver.com";

    /**
     * 현재 인증된 사용자로부터 storeId를 추출하는 메서드
     * 추후 토큰 기반 인증으로 사용 예정
     * @return storeId
     */
    private String getStoreIdFromToken() {
        TokenProvider.TokenUserInfo tokenUserInfo = (TokenProvider.TokenUserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return tokenUserInfo.getEmail();
    }

    /**
     * 가게의 마이페이지 정보를 가져오는 GET 요청 처리
     * @return 가게의 마이페이지 정보 DTO
     */
    @GetMapping("/info")
    public ResponseEntity<StoreMyPageDto> getStoreMyPageInfo() {
        String storeId = getStoreIdFromToken(); // 주석처리된 부분
        log.info("Fetching store my page info for storeId: {}", storeId);
        StoreMyPageDto storeInfo = storeMyPageService.getStoreMyPageInfo(storeId);
        return ResponseEntity.ok().body(storeInfo);
    }

    /**
     * 가게의 통계 정보를 가져오는 GET 요청 처리
     * @return 가게의 통계 정보 DTO
     */
    @GetMapping("/stats")
    public ResponseEntity<StoreStatsDto> getStoreStats() {
        String storeId = getStoreIdFromToken(); // 토큰정보 사용 시 주석 해제
        log.info("Fetching store stats for storeId: {}", storeId);
        StoreStatsDto storeStats = storeMyPageService.getStats(storeId);
        return ResponseEntity.ok().body(storeStats);
    }

    /**
     * 가게의 예약 목록을 가져오는 GET 요청 처리
     * @return 예약 목록 DTO 리스트
     */
    @GetMapping("/reservations")
    public ResponseEntity<List<StoreReservationDto>> getStoreReservations() {
        String storeId = getStoreIdFromToken(); // 주석처리된 부분
        log.info("Fetching reservations for storeId: {}", storeId);
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
        String storeId = getStoreIdFromToken(); // 토큰정보 사용 시 주석 해제
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
        String storeId = getStoreIdFromToken(); // 토큰정보 사용 시 주석 해제
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
        String storeId = getStoreIdFromToken(); // 주석처리된 부분
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
        String storeId = getStoreIdFromToken(); // 주석처리된 부분
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
        String storeId = getStoreIdFromToken(); // 주석처리된 부분
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
        String storeId = getStoreIdFromToken(); // 주석처리된 부분
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
        String storeId = getStoreIdFromToken(); // 주석처리된 부분
        List<StoreHolidayDto> holidays = storeMyPageService.getHolidays(storeId);
        return ResponseEntity.ok().body(holidays);
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
        String storeId = getStoreIdFromToken(); // 주석처리된 부분
        boolean flag = storeMyPageService.setPickupTime(storeId, date, pickupTime);
        return flag ? ResponseEntity.ok().body(true) : ResponseEntity.badRequest().body(false);
    }

    /**
     *
     * @method   updateStoreInfo
     * @param    dto
     * @return   ResponseEntity<?> type
     * @author   hoho
     * @date     2024 07 25 16:26
     *
     * {
     *     type: price / openAt / closedAt / productCnt / business_number
     *     value: String
     * }
     *
     */
    @PatchMapping("/edit")
    public ResponseEntity<?> updateStoreInfo(@RequestBody UpdateDto dto) {
        String storeId = getStoreIdFromToken();
        boolean flag = storeMyPageEditService.updateProfileInfo(storeId, dto);
        if (flag)
            return ResponseEntity.ok().body(true);
        return ResponseEntity.badRequest().body(false);
    }

    /**
     *
     * @method   updateProfileImage
     * @param    storeImg
     * @return   ResponseEntity<?> type
     * @author   hoho
     * @date     2024 07 25 17:14
     *
     */
    @PostMapping("/edit/img")
    public ResponseEntity<?> updateProfileImage(@RequestParam("storeImg") MultipartFile storeImg) {
        String storeId = getStoreIdFromToken();
        boolean flag = storeMyPageEditService.updateProfileImg(storeId, storeImg);
        if (flag)
            return ResponseEntity.ok().body(true);
        return ResponseEntity.badRequest().body(false);
    }
}