package org.nmfw.foodietree.domain;

import javax.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.resp.CustomerIssueDetailDto;
import org.nmfw.foodietree.domain.customer.dto.resp.CustomerMyPageDto;
import org.nmfw.foodietree.domain.customer.dto.resp.MyPageReservationDetailDto;
import org.nmfw.foodietree.domain.customer.service.CustomerMyPageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CommonController {

    private final CustomerMyPageService customerMyPageService;

    @Value("${env.kakao.api.key:default}")
    private String kakaoApiKey;

    @GetMapping("/sign-in")
    public String login() {
        return "sign-in";
    }

    @GetMapping("/common/sign-up")
    public String signUp(Model model) {
        model.addAttribute("kakaoApiKey", kakaoApiKey);
        return "common/sign-up";
	}
    @GetMapping("/customer/mypage-test")
    public String myPageMain(
                            Model model
                            , HttpServletRequest request
                            , HttpServletResponse response) {
        log.info("/customer/mypage-main POST");

        // 1. 로그인 되어있는 회원 아이디 가져오기
        String customerId = "test@gmail.com";
        // 2. 데이터베이스에서 해당 회원 데이터 조회하기
        CustomerMyPageDto customerMyPageDto = customerMyPageService.getCustomerInfo(customerId,
            request, response);

        List<MyPageReservationDetailDto> myPageReservationDetailDto = customerMyPageService.getReservationInfo(
            customerId);

        List<CustomerIssueDetailDto> customerIssueDetailDto = customerMyPageService.getCustomerIssues(
            customerId);
        // 3. JSP파일에 조회한 데이터 보내기
        model.addAttribute("customerMyPageDto", customerMyPageDto);
        model.addAttribute("reservations", myPageReservationDetailDto);
        model.addAttribute("issues", customerIssueDetailDto);
        return "customer/mypage";
    }

    @PostMapping("/common/sign-up")
    @CrossOrigin
    public String signUp(SignUpDto signUpDto) {
        log.info("signUpDto: {}", signUpDto);
        return "redirect:/sign-in";
	}


    @GetMapping("/customer/mypage-edit-test")
    public String myPageEdit(
                            Model model
                            , HttpServletRequest request
                            , HttpServletResponse response){
            log.info("/customer/mypage-main POST");

            // 1. 로그인 되어있는 회원 아이디 가져오기
            String customerId = "test@gmail.com";
            // 2. 데이터베이스에서 해당 회원 데이터 조회하기
            CustomerMyPageDto customerMyPageDto = customerMyPageService.getCustomerInfo(customerId, request, response);

            List<MyPageReservationDetailDto> myPageReservationDetailDto = customerMyPageService.getReservationInfo(customerId);

            List<CustomerIssueDetailDto> customerIssueDetailDto = customerMyPageService.getCustomerIssues(customerId);
            // 3. JSP파일에 조회한 데이터 보내기
            model.addAttribute("customerMyPageDto", customerMyPageDto);
            model.addAttribute("reservations", myPageReservationDetailDto);
            model.addAttribute("issues", customerIssueDetailDto);
        return "customer/mypage-edit";
    }

    @Data
    static class SignUpDto {
        private String account;
        private String password;
        private String name;
        private String email;
        private String profileImage;
        private List<String> food;
        private List<String> location;
    }
}
