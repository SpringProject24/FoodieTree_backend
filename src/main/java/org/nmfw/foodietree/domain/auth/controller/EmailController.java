package org.nmfw.foodietree.domain.auth.controller;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeDto;
import org.nmfw.foodietree.domain.auth.entity.EmailVerification;
import org.nmfw.foodietree.domain.auth.security.TokenProvider;
import org.nmfw.foodietree.domain.auth.security.TokenProvider.TokenUserInfo;
import org.nmfw.foodietree.domain.auth.service.EmailService;
import org.nmfw.foodietree.domain.auth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    @GetMapping("/check")
    @CrossOrigin
    @ResponseBody
    public ResponseEntity<?> check(@RequestParam String email) {
        log.info("이메일 중복체크 아이디 : {}", email);
        boolean flag = emailService.existsByEmailInCustomerOrStore(email);
        log.info("이메일 중복체크  결과 {}", flag);
        return ResponseEntity
                .ok()
                .body(flag);
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

            emailService.sendVerificationEmailLink(email, userType);

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
            log.info("유효성 검증 후  email : {}", email);
            String userType = accessTokenUserInfo.getRole();
            log.info("유효성 검증 후  userType : {}", userType);

            // 이메일 정보를 인증 테이블 데이터베이스 에서 조회
            Optional<EmailVerification> emailVerificationOpt = emailService.findOneByEmail(email);

            if (emailVerificationOpt.isPresent()) {
                EmailVerification emailVerification = emailVerificationOpt.get();
                EmailCodeDto emailCodeDto = EmailCodeDto.builder()
                        .email(emailVerification.getEmail())
                        .expiryDate(emailVerification.getExpiryDate())
                        .emailVerified(true)
                        .userType(userType)
                        .build();

                log.info("EmailCodeDto retrieved from database: {}", emailCodeDto);


                // 이메일 정보가 인증 테이블에 있을 경우
                if (emailCodeDto != null) {

                    log.info("이메일 정보 테이블에 해당 이메일 있음 {}", email);
                    emailCodeDto.setUserType(userType);
                    emailCodeDto.setEmailVerified(true);

                    // 이메일 인증이 완료되지 않은 경우 - 실제 회원가입이 되지 않은 경우
                    if (!emailService.existsByEmailInCustomerOrStore(emailCodeDto.getEmail())) {

                        log.info("( customer, store 테이블에 저장)이 되지 않은 경우 {}", emailCodeDto);

                        emailService.updateEmailVerification(emailCodeDto); // 인증정보 true 업데이트

                        userService.saveUserInfo(emailCodeDto);
                    } else {
                        // 실제 회원가입이 되어있는 경우, 로그인 하는데 access token 기간이 종료된 경우
                        // 만료 기한 access, refresh 업데이트
                        log.info("customer, store에 email이 실제 회원가입 되어있는 경우 dto {}", emailCodeDto);
                        return userService.updateUserInfo(emailCodeDto);
                    }
                }
            }
            // email table에 인증정보가 없을 경우 즉, access token이 만료되었을경우
            // 인증 정보는 상관없이 access token이 만료되었을 경우
        } catch (JwtException e) {
            log.warn("JWT parsing error: {}", e.getMessage(), e);
        }
                // 리프레시 토큰의 만료일자를 확인 - 서버에도 리프레시 토큰 저장
                TokenUserInfo refreshTokenUserInfo = tokenProvider.validateAndGetRefreshTokenInfo(refreshToken);

                log.info("token provider tokenUserInfo로 리프레시토큰의 만료일자 확인하기 위한 값 : {} ", refreshTokenUserInfo);

                String email = refreshTokenUserInfo.getEmail();
                String userType = refreshTokenUserInfo.getRole();

                LocalDateTime refreshTokenExpiryDate = userService.getUserRefreshTokenExpiryDate(email, userType);

                log.info(" 서버에서 리프레시 토큰 만료일자 {}", refreshTokenExpiryDate);

                if (refreshTokenExpiryDate == null || refreshTokenExpiryDate.isBefore(LocalDateTime.now())) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Refresh token expired"));
                }

                // 새로운 액세스 토큰 발급
                EmailCodeDto emailCodeDto = EmailCodeDto.builder()
                        .email(email)
                        .userType(userType)
                        .build();

                return userService.updateUserInfo(emailCodeDto);
        }
    }

 
