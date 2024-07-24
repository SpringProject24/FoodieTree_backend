package org.nmfw.foodietree.domain.customer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.resp.*;
import org.nmfw.foodietree.domain.customer.service.CustomerMyPageService;
import org.nmfw.foodietree.domain.customer.util.LoginUtil;
import org.nmfw.foodietree.domain.product.Util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerMyPageController {
    @Value("${env.upload.path}")
    private String uploadDir;
    private final CustomerMyPageService customerMyPageService;
    
    /**
     * 고객 정보를 가져오는 GET 요청 처리
     * @return 고객 정보 DTO
     */
    @GetMapping("/info")
    public ResponseEntity<CustomerMyPageDto> getUserInfo() {
        String customerId = "test@gmail.com"; // 테스트용 계정 강제 삽입, 추후 토큰에서 customerId 입력하는것으로 변경 예정
        CustomerMyPageDto customerInfo = customerMyPageService.getCustomerInfo(customerId);
        return ResponseEntity.ok(customerInfo);
    }

    /**
     * 고객 통계 정보를 가져오는 GET 요청 처리
     * @return 고객 통계 정보 DTO
     */
    @GetMapping("/stats")
    public ResponseEntity<StatsDto> getStats() {
        String customerId = "test.gmail.com"; // 테스트용 계정 강제 삽입, 추후 토큰에서 customerId 입력하는것으로 변경 예정
        StatsDto stats = customerMyPageService.getStats(customerId);
        return ResponseEntity.ok(stats);
    }

    @PatchMapping("/{customerId}/update")
    public ResponseEntity<?> updateCustomerInfo(@PathVariable String customerId, @RequestBody List<UpdateDto> updates) {

        boolean flag = customerMyPageService.updateCustomerInfo(customerId, updates);
        return flag? ResponseEntity.ok(true): ResponseEntity.status(400).body(false);
    }

    @PatchMapping("/{customerId}/delete")
    public ResponseEntity<?> deleteCustomerInfo(@PathVariable String customerId, @RequestBody List<UpdateDto> dtos) {
        boolean flag = customerMyPageService.deleteCustomerInfo(customerId, dtos);
        return flag? ResponseEntity.ok(true): ResponseEntity.status(400).body(false);
    }

    @PatchMapping("/{customerId}/update/password")
    public ResponseEntity<?> updateCustomerPw(@PathVariable String customerId, @RequestBody Map<String, String> map) {
        String newPassword = map.get("newPassword");
        boolean flag = customerMyPageService.updateCustomerPw(customerId, newPassword);
        return flag? ResponseEntity.ok(true): ResponseEntity.status(400).body(false);
    }

    @PostMapping("/update/img")
    public ResponseEntity<?> updateProfileImage(@RequestParam("storeImg") MultipartFile storeImg, HttpSession session) {
        String customerId = LoginUtil.getLoggedInUser(session);
        UpdateDto dto = new UpdateDto();
        try {
            // 예시로 파일 이름과 크기를 출력하는 코드
            String fileName = storeImg.getOriginalFilename();
            long fileSize = storeImg.getSize();
            System.out.println("Received file: " + fileName + ", Size: " + fileSize + " bytes");
            String imagePath = null;
            if (!storeImg.isEmpty()) {
                // 서버에 업로드 후 업로드 경로 반환
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                imagePath = FileUtil.uploadFile(uploadDir, storeImg);
                dto.setType("profile_image");
                dto.setValue(imagePath);
                List<UpdateDto> dtos = List.of(dto);
                customerMyPageService.updateCustomerInfo(customerId, dtos);
                return ResponseEntity.ok().body(true);
            }
//            FileUtil.uploadFile(rootPath, storeImg);

        } catch (Exception e) {
            // 업로드 실패 시 예외 처리
            return ResponseEntity.badRequest().body(false);
        }
        return ResponseEntity.badRequest().body(false);
    }
}
