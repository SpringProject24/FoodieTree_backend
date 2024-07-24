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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @GetMapping("/mypage")
    public String myPageMain(HttpSession session
                            , Model model
                            , HttpServletRequest request
                            , HttpServletResponse response){
        log.info("/customer/mypage GET");
//        String customerId = LoginUtil.getLoggedInUser(session);
        String customerId = LoginUtil.getLoggedInUser(request.getSession());
        // 2. 데이터베이스에서 해당 회원 데이터 조회하기
        CustomerMyPageDto customerMyPageDto = customerMyPageService.getCustomerInfo(customerId, request, response);

        List<MyPageReservationDetailDto> myPageReservationDetailDto = customerMyPageService.getReservationInfo(customerId);

        List<CustomerIssueDetailDto> customerIssueDetailDto = customerMyPageService.getCustomerIssues(customerId);

        statsDto stats = customerMyPageService.getStats(customerId);

        // 3. JSP파일에 조회한 데이터 보내기
        model.addAttribute("customerMyPageDto", customerMyPageDto);
        model.addAttribute("reservations", myPageReservationDetailDto);
        model.addAttribute("issues", customerIssueDetailDto);
        model.addAttribute("stats", stats);
        return "customer/mypage";
    }

    @GetMapping("/mypage-edit")
    public String editCustomerInfo(HttpSession session,
                                   Model model,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        log.info("/customer/customer-mypage-edit-test GET");
//        String customerId = LoginUtil.getLoggedInUser(session);
        String customerId = LoginUtil.getLoggedInUser(session);
        CustomerMyPageDto customerMyPageDto = customerMyPageService.getCustomerInfo(customerId, request, response);

        List<MyPageReservationDetailDto> myPageReservationDetailDto = customerMyPageService.getReservationInfo(customerId);

        List<CustomerIssueDetailDto> customerIssueDetailDto = customerMyPageService.getCustomerIssues(customerId);
        // 3. JSP파일에 조회한 데이터 보내기
        model.addAttribute("customerMyPageDto", customerMyPageDto);
        model.addAttribute("reservations", myPageReservationDetailDto);
        model.addAttribute("issues", customerIssueDetailDto);
        return "customer/mypage-edit";
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
