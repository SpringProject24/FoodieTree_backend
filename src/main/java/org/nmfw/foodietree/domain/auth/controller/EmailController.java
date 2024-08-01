package org.nmfw.foodietree.domain.auth.controller;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeDto;
import org.nmfw.foodietree.domain.auth.entity.EmailVerification;
import org.nmfw.foodietree.domain.auth.mapper.EmailMapper;
import org.nmfw.foodietree.domain.auth.security.TokenProvider;
import org.nmfw.foodietree.domain.auth.security.filter.AuthJwtFilter;
import org.nmfw.foodietree.domain.auth.service.EmailService;
import org.nmfw.foodietree.domain.auth.service.UserService;
import org.nmfw.foodietree.domain.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
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
        String purpose = request.get("purpose");
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

    // 인증 토큰 클라이언트측에서 확인하기
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${env.jwt.refresh}")
    private String REFRESH_SECRET_KEY;


    @PostMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmail(@RequestBody Map<String, String> request) {

        log.info("Request Data: {}", request);
        String token = request.get("token");
        log.info("access token 있는지 확인 {}", token);

        // access token이 아예 없을 경우 bad request 반환 - 로그인 페이지로 리다이렉션
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "Token is missing"));
        }
        try {
            // access token 유효성 검사
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .parseClaimsJws(token);

            String email = claims.getBody().get("sub", String.class);
            String userType = claims.getBody().get("role", String.class);

            log.info("secretKey claim : {}", claims);
            log.info("Email extracted from token: {}", email);
            log.info("user Role (type) extracted from token: {}", userType);

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
                // 이메일 인증이 완료된 경우 (재로그인, 토큰 재부여)
                // 새로운 Access Token 발급
                String newAccessToken = tokenProvider.createToken(emailCodeDto);
                // 새로운 Refresh Token 발급
                String newRefreshToken = tokenProvider.createRefreshToken(email, userType);

                //실제 회원가입(테이블에 저장)이 되지 않은 경우 false
                if (!(userService.findByEmail(emailCodeDto))) {
                    log.info("실제 회원가입(테이블에 저장)이 되지 않은 경우 {}", emailCodeDto);
                    emailMapper.update(emailCodeDto); // 인증정보 true 업데이트
                    userService.saveUserInfo(emailCodeDto);

                } else {
                    // 실제 회원가입이 되어있는 경우, 로그인 하는데 access token 기간이 종료된 경우
                    // 만료 기한 access, refresh 업데이트
                    log.info("email이 실제 회원가입 되어있는 경우 dto {}", emailCodeDto);
                    userService.updateUserInfo(emailCodeDto);
                }
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "accessToken", newAccessToken,
                        "refreshToken", newRefreshToken,
                        "email", email,
                        "role", userType,
                        "message", "Token reissued successfully."
                ));
            }

            // email table에 인증정보가 없을 경우 즉, access token이 만료되었을경우
            // 인증 정보는 상관없이 access token이 만료되었을 경우
        } catch (JwtException e) {
            log.error("access token 의 기한이 만료되었거나 위조되었습니다.");
            log.error("JWT parsing error: {}", e.getMessage());

            try {
                // 리프레시 토큰의 만료일자를 확인
                TokenProvider.TokenUserInfo tokenUserInfo = tokenProvider.validateAndGetRefreshTokenInfo(token);
                String email = tokenUserInfo.getEmail();
                String userType = tokenUserInfo.getRole();

                LocalDateTime refreshTokenExpiryDate = userService.getRefreshTokenExpiryDate(email, userType);

                log.info("리이이이이이이이이프레에에에에에시토오오오크크ㅡ으응으으응으ㅡㄴ!!! {}", refreshTokenExpiryDate);

                if (refreshTokenExpiryDate == null || refreshTokenExpiryDate.isBefore(LocalDateTime.now())) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Refresh token expired"));
                }

                // 새로운 액세스 토큰 발급
                EmailCodeDto emailCodeDto = EmailCodeDto.builder()
                        .email(email)
                        .userType(userType)
                        .build();
                String newAccessToken = tokenProvider.createToken(emailCodeDto);
                String newRefreshToken = tokenProvider.createRefreshToken(email, userType);

                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "accessToken", newAccessToken,
                        "refreshToken", newRefreshToken,
                        "email", email,
                        "role", userType,
                        "message", "Token reissued successfully."
                ));

            } catch (JwtException ex) {
                log.error("Refresh token parsing error: {}", ex.getMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Invalid refresh token"));
            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "message", "An unexpected error occurred"));
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
}
