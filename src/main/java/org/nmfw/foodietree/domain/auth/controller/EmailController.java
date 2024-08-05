package org.nmfw.foodietree.domain.auth.controller;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeDto;
import org.nmfw.foodietree.domain.auth.entity.EmailVerification;
import org.nmfw.foodietree.domain.auth.mapper.EmailMapper;
import org.nmfw.foodietree.domain.auth.security.TokenProvider;
import org.nmfw.foodietree.domain.auth.security.TokenProvider.TokenUserInfo;
import org.nmfw.foodietree.domain.auth.security.filter.AuthJwtFilter;
import org.nmfw.foodietree.domain.auth.service.EmailService;
import org.nmfw.foodietree.domain.auth.service.UserService;
import org.nmfw.foodietree.domain.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;
    private final UserService userService;
    private final EmailMapper emailMapper;
    private final TokenProvider tokenProvider;

    @GetMapping("/send-reset-email")
    public String sendVerificationCode(@RequestParam String to, String userType) {
        try {
            emailService.sendResetVerificationCode(to, userType, "reset");
            return "Password reset email sent successfully";
        } catch (Exception e) {
            return "Failed to send password reset email: " + e.getMessage();
        }
    }

    /*
    @GetMapping("/verify-reset-code")
    public String verifyResetCode(@RequestParam String email, @RequestParam String code) {
        if (emailService.verifyCode(email, code)) {
            return "Verification successful";
        } else {
            return "Verification failed or code expired";
        }
    }

     */

    // 인증 코드 전송
    @PostMapping("/sendVerificationCode")
    public ResponseEntity<?> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String purpose = request.get("purpose");
        String userType = request.get("userType");
        try {
            emailService.sendResetVerificationCode(email, purpose, userType);
            return ResponseEntity.ok("Verification code sent");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send verification code");
        }
    }

    // 인증 리다이렉션 링크 메일 전송
    @PostMapping("/sendVerificationLink")
    public ResponseEntity<?> sendVerificationLink(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String userType = request.get("userType");

        log.info("usertype :{} ", userType);

        try {
            EmailCodeDto emailCodeDto = EmailCodeDto.builder()
                    .email(email)
                    .userType(userType)
                    .build();

            emailService.sendVerificationEmailLink(email, userType, emailCodeDto);

            return ResponseEntity.ok("Verification Link sent");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send verification link");
        }
    }

    @PostMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmail(@RequestBody Map<String, String> request) {

            log.info("Request Data: {}", request);

            String token = request.get("token");
            String refreshToken = request.get("refreshToken");

            log.info("access token 있는지 확인 {}", token);
            log.info("refresh token 있는지 확인 {}", refreshToken);

            try {
                // access token 유효성 검사
                TokenUserInfo accessTokenUserInfo = tokenProvider.validateAndGetTokenInfo(token);

                log.info("Email extracted from token: {}", accessTokenUserInfo.getEmail());
                log.info("user Role (type) extracted from token: {}", accessTokenUserInfo.getRole());
                log.info("user access expire date : {}", accessTokenUserInfo.getTokenExpireDate().toString());

                String email = accessTokenUserInfo.getEmail();
                String userType = accessTokenUserInfo.getRole();

                // 이메일 dto 정보를 데이터베이스에서 조회
                EmailCodeDto emailCodeDto = emailMapper.findOneByEmail(email);
                log.info("EmailCodeDto retrieved from database: {}", emailCodeDto);

                // 이메일 정보가 인증 테이블에 있을 경우
                if (emailCodeDto != null) {
                    emailCodeDto.setUserType(userType);
                    emailCodeDto.setEmailVerified(true);

                    // 이메일 인증이 완료되지 않은 경우 - 실제 회원가입이 되지 않은 경우
                    if (!emailCodeDto.isEmailVerified()) {
                        // 이메일 재전송 페이지로 리다이렉션
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "Email link is not verified! Please resend verification email."));
                    }

                    //실제 회원가입(테이블에 저장)이 되지 않은 경우 false
                    if (!(userService.findByEmail(emailCodeDto))) {

                        log.info("실제 회원가입(테이블에 저장)이 되지 않은 경우 {}", emailCodeDto);
                        emailMapper.update(emailCodeDto); // 인증정보 true 업데이트
                        return userService.saveUserInfo(emailCodeDto);

                    } else {
                        // 실제 회원가입이 되어있는 경우, 로그인 하는데 access token 기간이 종료된 경우
                        // 만료 기한 access, refresh 업데이트
                        log.info("email이 실제 회원가입 되어있는 경우 dto {}", emailCodeDto);
                        return userService.updateUserInfo(emailCodeDto);
                    }
                }

                // email table에 인증정보가 없을 경우 즉, access token이 만료되었을경우
                // 인증 정보는 상관없이 access token이 만료되었을 경우
            } catch (Exception e) {
                log.info("access token 의 기한이 만료되었거나 위조되었습니다.");
                log.info("JWT parsing error: {}", e.getMessage());

                try {
                    // 리프레시 토큰의 만료일자를 확인 - 서버에도 리프레시 토큰 저장
                    TokenUserInfo refreshTokenUserInfo = tokenProvider.validateAndGetRefreshTokenInfo(refreshToken);

                    log.info("token provider tokenUserInfo로 리프레시토큰의 만료일자 확인하기 위한 값 : {} ", refreshTokenUserInfo);

                    String email = refreshTokenUserInfo.getEmail();
                    String userType = refreshTokenUserInfo.getRole();

                    LocalDateTime refreshTokenExpiryDate = userService.getRefreshTokenExpiryDate(email, userType);

                    log.info("리이이이이이이이이프레에에에에에시토오오오크크ㅡ으응으으응으ㅡㄴ!!! 서버 만료일자 {}", refreshTokenExpiryDate);

                    if (refreshTokenExpiryDate == null || refreshTokenExpiryDate.isBefore(LocalDateTime.now())) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Refresh token expired"));
                    }

                    // 새로운 액세스 토큰 발급
                    EmailCodeDto emailCodeDto = EmailCodeDto.builder()
                            .email(email)
                            .userType(userType)
                            .build();

                    // Call updateUserInfo and capture its response
                    return userService.updateUserInfo(emailCodeDto);


                } catch (Exception ex) {
                    log.error("Refresh token parsing error: {}", ex.getMessage());
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Invalid refresh token"));
                }
            }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "message", "An unexpected error occurred"));
        }
    }


    /*
    @PostMapping("/verifyCode")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> request, @RequestParam(required = false) String purpose) {
        String email = request.get("email");
        String code = request.get("code");
        boolean isValid = false;
        if (purpose != null && purpose.equals("signup")) {
            isValid = emailService.verifyCodeForSignUp(email, code);
        } else {
            isValid = emailService.verifyCode(email, code);
        }
        if (isValid) {
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("failure");
        }
    }

     */

