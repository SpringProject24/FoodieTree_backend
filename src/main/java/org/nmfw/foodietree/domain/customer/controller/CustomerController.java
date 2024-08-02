package org.nmfw.foodietree.domain.customer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.resp.CustomerMyPageDto;
import org.nmfw.foodietree.domain.customer.dto.resp.StatsDto;
import org.nmfw.foodietree.domain.customer.dto.resp.UpdateDto;
import org.nmfw.foodietree.domain.customer.service.CustomerMyPageService;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationDetailDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Controller
@RequestMapping("/customer")
@Slf4j
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerMyPageService customerMyPageService;

    // 테스트용 계정 강제 삽입, 추후 토큰에서 customerId 입력하는것으로 변경 예정
    String customerId = "test@gmail.com";

    /**
     * 고객 정보를 가져오는 GET 요청 처리
     * @return 고객 정보 DTO
     */
    @GetMapping("/info")
    public ResponseEntity<CustomerMyPageDto> getUserInfo() {
        // 추후 토큰을 통해 고객 ID를 가져옴
        // String customerId = getCustomerIdFromToken();
        CustomerMyPageDto customerInfo = customerMyPageService.getCustomerInfo(customerId);
        return ResponseEntity.ok(customerInfo);
    }

    /**
     * 고객 통계 정보를 가져오는 GET 요청 처리
     * @return 고객 통계 정보 DTO
     */
    @GetMapping("/stats")
    public ResponseEntity<StatsDto> getStats() {
        // 추후 토큰을 통해 고객 ID를 가져옴
        // String customerId = getCustomerIdFromToken();
        StatsDto stats = customerMyPageService.getStats(customerId);
        return ResponseEntity.ok(stats);
    }

    /**
     * 고객 예약 목록을 가져오는 GET 요청 처리
     * @return 고객 예약 목록 DTO 리스트
     */
    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationDetailDto>> getReservations() {
        // 추후 토큰을 통해 고객 ID를 가져옴
        // String customerId = getCustomerIdFromToken();
        List<ReservationDetailDto> reservations = customerMyPageService.getReservationList(customerId);
        return ResponseEntity.ok(reservations);
    }

    /**
     * 현재 인증된 사용자로부터 고객 ID를 추출하는 메서드
     * 추후 토큰 기반 인증으로 변경 예정
     * @return 고객 ID
     */
//     private String getCustomerIdFromToken() {
//         TokenProvider.TokenUserInfo tokenUserInfo = (TokenProvider.TokenUserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//         return tokenUserInfo.getUserId();
//     }

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
    public ResponseEntity<?> imageUpload(@RequestParam("customerImg") MultipartFile customerImg) {
        String customerId = "test@gmail.com";
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
    public ResponseEntity<?> insertPreferred(@RequestBody UpdateDto dto) {
        String customerId = "test@gmail.com";
        boolean flag = customerMyPageService.updateCustomerInfo(customerId, List.of(dto));
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
    public ResponseEntity<?> deletePreferred(@RequestBody UpdateDto dto) {
        String customerId = "test@gmail.com";
        boolean flag = customerMyPageService.deleteCustomerInfo(customerId, List.of(dto));
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
     *     type: string (nickname or phone_number)
     *     value: string
     * }
     */
    @PatchMapping("/edit")
    public ResponseEntity<?> editInfo(@RequestBody UpdateDto dto) {
        String customerId = "test@gmail.com";
        boolean flag = customerMyPageService.updateCustomerInfo(customerId, List.of(dto));
        if (flag)
            return ResponseEntity.ok().body(true);
        return ResponseEntity.badRequest().body(false);
    }

}
