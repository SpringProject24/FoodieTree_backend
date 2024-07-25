package org.nmfw.foodietree.domain.auth.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeDto;
import org.nmfw.foodietree.domain.auth.mapper.EmailMapper;
import org.nmfw.foodietree.domain.auth.service.EmailService;
import org.nmfw.foodietree.domain.auth.service.UserService;
import org.nmfw.foodietree.domain.customer.dto.request.SignUpDto;
import org.nmfw.foodietree.domain.customer.entity.Customer;
import org.nmfw.foodietree.domain.customer.entity.CustomerIssues;
import org.nmfw.foodietree.domain.customer.mapper.CustomerMapper;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;
    private final UserService userService;
    private final EmailMapper emailMapper;

    @GetMapping("/send-reset-email")
    public String sendVerificationCode(@RequestParam String to, String userType) {
        try {
            emailService.sendResetVerificationCode(to, userType, "reset");
            return "Password reset email sent successfully";
        } catch (Exception e) {
            return "Failed to send password reset email: " + e.getMessage();
        }
    }

    @GetMapping("/verify-reset-code")
    public String verifyResetCode(@RequestParam String email, @RequestParam String code) {
        if (emailService.verifyCode(email, code)) {
            return "Verification successful";
        } else {
            return "Verification failed or code expired";
        }
    }

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
                   .customerId(email)
                   .userType(userType)
                   .build();

            emailService.sendVerificationEmailLink(email, userType, emailCodeDto);

            return ResponseEntity.ok("Verification Link sent");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send verification link");
        }
    }

    // 인증 토큰 클라이언트측에서 확인하기
    @Value("${env.jwt.secret}")
    private String SECRET_KEY;

    @PostMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmail(@RequestBody Map<String, String> request) {
        // 서버 측에서 받은 요청 데이터를 로그로 출력합니다.
        log.info("Request Data: {}", request);
        String token = request.get("token");
        // 토큰이 없을 때
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "Token is missing"));
        }

        // usertype이 store 인지 customer 인지 구분해서 저장 -> 서비스레이어
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .parseClaimsJws(token);

            String email = claims.getBody().get("sub", String.class);
            String userRole = claims.getBody().get("role", String.class);

            log.info("secretKey claim : {}", claims);

            log.info("Email extracted from token: {}", email);

            log.info("user Role (type) extracted from token: {}", userRole);

            EmailCodeDto emailCodeDto = emailMapper.findByEmail(email);

            log.info("EmailCodeDto retrieved from database: {}", emailCodeDto);

            if (emailCodeDto != null) {
                emailCodeDto.setUserType(userRole);
                emailCodeDto.setEmailVerified(true);

                emailMapper.save(emailCodeDto); // save가 아니라, update

                userService.saveUserInfo(emailCodeDto); // 실제 usertype 에 맞게 저장

                return ResponseEntity.ok(Map.of("success", true));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "User not found"));
            }
        } catch (JwtException e) {
            log.error("JWT parsing error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "Invalid or expired token"));
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "message", "An unexpected error occurred"));
        }
    }



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
}
