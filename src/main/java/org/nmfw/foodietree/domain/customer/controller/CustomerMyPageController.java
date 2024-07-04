package org.nmfw.foodietree.domain.customer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.resp.*;
import org.nmfw.foodietree.domain.customer.service.CustomerMyPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerMyPageController {
    String customerId = "test@gmail.com";
    private final CustomerMyPageService customerMyPageService;

    @GetMapping("/mypage")
    public String myPageMain(HttpSession session
                            , Model model
                            , HttpServletRequest request
                            , HttpServletResponse response){
        log.info("/customer/mypage GET");

        // 1. 로그인 되어있는 회원 아이디 가져오기
//        String customerId = "sji4205@naver.com";
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

    @GetMapping("/customer-mypage-edit")
    public String editCustomerInfo(HttpSession session,
                                   Model model,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        log.info("/customer/customer-mypage-edit-test GET");

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
        return flag? ResponseEntity.ok("Update successful"): ResponseEntity.status(400).body("Update fail");
    }

    @PatchMapping("/{customerId}/delete")
    public ResponseEntity<?> deleteCustomerInfo(@PathVariable String customerId, @RequestBody List<UpdateDto> dtos) {
        boolean flag = customerMyPageService.deleteCustomerInfo(customerId, dtos);
        return flag? ResponseEntity.ok("Delete successful"): ResponseEntity.status(400).body("Delete fail");
    }

    @PatchMapping("/{customerId}/update/password")
    public ResponseEntity<?> updateCustomerPw(@PathVariable String customerId, @RequestBody String newPassword) {
        boolean flag = customerMyPageService.updateCustomerPw(customerId, newPassword);
        return flag? ResponseEntity.ok("password reset successful"): ResponseEntity.status(400).body("reset fail");
    }
}
