package org.nmfw.foodietree.domain.customer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.security.TokenProvider;
import org.nmfw.foodietree.domain.auth.security.TokenProvider.TokenUserInfo;
import org.nmfw.foodietree.domain.customer.dto.resp.CustomerMyPageDto;
import org.nmfw.foodietree.domain.customer.dto.resp.StatsDto;
import org.nmfw.foodietree.domain.customer.dto.resp.UpdateAreaDto;
import org.nmfw.foodietree.domain.customer.dto.resp.UpdateDto;
import org.nmfw.foodietree.domain.customer.service.CustomerEditService;
import org.nmfw.foodietree.domain.customer.service.CustomerMyPageService;
import org.nmfw.foodietree.domain.customer.service.CustomerService;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationDetailDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMyPageService customerMyPageService;
    private final CustomerEditService customerEditService;

    @GetMapping("/check")
    @CrossOrigin
    @ResponseBody
    public ResponseEntity<?> check(String email) {
        log.info("customer 중복체크 아이디 : {}",  email);
        boolean flag = customerService.findOne(email);
        log.info("customer 중복체크  결과 {}",  flag);
        return ResponseEntity
                .ok()
                .body(flag);
    }

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


    // 테스트용 계정 강제 삽입, 추후 토큰에서 customerId 입력하는것으로 변경 예정
//    String customerId = "test@gmail.com";

    /**
     * 고객 정보를 가져오는 GET 요청 처리
     * @return 고객 정보 DTO
     */
    @GetMapping("/info")
    public ResponseEntity<CustomerMyPageDto> getUserInfo(
            @AuthenticationPrincipal TokenUserInfo userInfo
    ) {
        // 추후 토큰을 통해 고객 ID를 가져옴
         String customerId = userInfo.getUsername();
         log.info("info uri 토큰에서 추출한 유저아이디 {}", customerId);
        CustomerMyPageDto customerInfo = customerMyPageService.getCustomerInfo(customerId);
        return ResponseEntity.ok(customerInfo);
    }

    /**
     * 고객 통계 정보를 가져오는 GET 요청 처리
     * @return 고객 통계 정보 DTO
     */
    @GetMapping("/stats")
    public ResponseEntity<StatsDto> getStats(
            @AuthenticationPrincipal TokenUserInfo userInfo
    ) {
        String customerId = userInfo.getUsername();
        log.info("stats fetch 토큰에서 추출한 유저아이디 {}", customerId);
//        CustomerMyPageDto customerInfo = customerMyPageService.getCustomerInfo(customerId);
        StatsDto stats = customerMyPageService.getStats(customerId);
        return ResponseEntity.ok(stats);
    }

    /**
     * 고객 예약 목록을 가져오는 GET 요청 처리
     * @return 고객 예약 목록 DTO 리스트
     */
    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationDetailDto>> getReservations(
            @AuthenticationPrincipal TokenUserInfo userInfo
    ) {
        // 추후 토큰을 통해 고객 ID를 가져옴
         String customerId = userInfo.getUsername();
        List<ReservationDetailDto> reservations = customerMyPageService.getReservationList(customerId);
        return ResponseEntity.ok(reservations);
    }

    /**
     * 현재 인증된 사용자로부터 고객 ID를 추출하는 메서드
     * 추후 토큰 기반 인증으로 변경 예정
     * @return 고객 ID
     */

    /**
     *
     * @method   imageUpload
     * @param    customerImg
     * @return   ResponseEntity<?> type
     * @author   hoho
     * @date     2024 07 25 15:14
     *
     */
    @PostMapping("/edit/img")
    public ResponseEntity<?> imageUpload(@RequestParam("customerImg") MultipartFile customerImg,
                                         @AuthenticationPrincipal TokenUserInfo userInfo
                                         ) {
        String customerId = userInfo.getUsername();
        boolean flag = customerMyPageService.updateProfileImg(customerId, customerImg);
        if (flag)
            return ResponseEntity.ok().body(true);
        return ResponseEntity.badRequest().body(false);
    }

    /**
     *
     * @method   insertPreferred
     * @return   ResponseEntity<?> type
     * @author   hoho
     * @date     2024 07 24 15:04
     *
     * {
     *  	type: food or area or store
     *  	value: string
     * }
     */
    @PostMapping("/edit")
    public ResponseEntity<?> insertPreferred(@RequestBody UpdateDto dto,
                                             @AuthenticationPrincipal TokenUserInfo userInfo
                                             ) {
        String customerId = userInfo.getUsername();
        boolean flag = customerEditService.insertPreferredInfo(customerId, dto);
        if (flag)
            return ResponseEntity.ok().body(true);
        return ResponseEntity.badRequest().body(false);
    }

    /**
     *
     * @method   deletePreferred
     * @param    dto
     * @return   ResponseEntity<?> type
     * @author   hoho
     * @date     2024 07 24 16:29
     *
     * {
     *     type : food or area or store
     *     value : string
     * }
     */
    @DeleteMapping("/edit")
    public ResponseEntity<?> deletePreferred(@RequestBody UpdateDto dto,
                                             @AuthenticationPrincipal TokenUserInfo userInfo
                                             ) {
        String customerId = userInfo.getUsername();

        boolean flag = customerEditService.deleteProfileInfo(customerId, dto);
        if (flag)
            return ResponseEntity.ok().body(true);
        return ResponseEntity.badRequest().body(false);
    }

    /**
     *
     * @method   editInfo
     * @param    dto
     * @return   ResponseEntity<?> type
     * @author   hoho
     * @date     2024 07 24 15:03
     * {
     *     type: string (nickname or customer_phone_number)
     *     value: string
     * }
     */
    @PatchMapping("/edit")
    public ResponseEntity<?> editInfo(@RequestBody UpdateDto dto,
                                      @AuthenticationPrincipal TokenUserInfo userInfo
    ) {
        String customerId = userInfo.getUsername();
        boolean flag = customerEditService.updateProfileInfo(customerId, dto);
        if (flag)
            return ResponseEntity.ok().body(true);
        return ResponseEntity.badRequest().body(false);
    }

    @PostMapping("/edit/area")
    public ResponseEntity<?> insertArea(@RequestBody UpdateAreaDto dto) {
        String customerId = "test@gmail.com";

        boolean flag = customerMyPageService.updateCustomerAreaInfo(customerId, dto);
        if (flag)
            return ResponseEntity.ok().body(true);
        return ResponseEntity.badRequest().body(false);
    }

    @DeleteMapping("/edit/area")
    public ResponseEntity<?> deleteArea(@RequestBody UpdateAreaDto dto) {
        String customerId = "test@gmail.com";

        boolean flag = customerMyPageService.deleteCustomerAreaInfo(customerId, dto);
        if (flag)
            return ResponseEntity.ok().body(true);
        return ResponseEntity.badRequest().body(false);
    }

    @GetMapping("/info/area")
    public ResponseEntity<?> getFavArea() {
        String customerId = "test@gmail.com";
        try{
            List<UpdateAreaDto> favAreas = customerMyPageService.getFavArea(customerId);
            return ResponseEntity.ok().body(favAreas);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(false);
        }
    }
}
