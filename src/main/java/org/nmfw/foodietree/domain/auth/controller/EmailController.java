package org.nmfw.foodietree.domain.auth.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeDto;
import org.nmfw.foodietree.domain.auth.mapper.EmailMapper;
import org.nmfw.foodietree.domain.auth.service.EmailService;
import org.nmfw.foodietree.domain.customer.dto.request.SignUpDto;
import org.nmfw.foodietree.domain.customer.entity.Customer;
import org.nmfw.foodietree.domain.customer.entity.CustomerIssues;
import org.nmfw.foodietree.domain.customer.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private final EmailMapper emailMapper;

    @GetMapping("/send-reset-email")
    public String sendVerificationCode(@RequestParam String to) {
        try {
            emailService.sendResetVerificationCode(to, "reset");
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
        try {
            emailService.sendResetVerificationCode(email, purpose);
            return ResponseEntity.ok("Verification code sent");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send verification code");
        }
    }

    // 인증 리다이렉션 링크 메일 전송 -> customer
    @PostMapping("/sendVerificationLink")
    public ResponseEntity<?> sendVerificationLink(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String purpose = request.get("purpose");
        try {
           EmailCodeDto emailCodeDto = EmailCodeDto.builder()
                   .customerId(email)
                   .build();

            emailService.sendVerificationEmailLink(email, emailCodeDto);

            return ResponseEntity.ok("Verification Link sent");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send verification link");
        }
    }

    // 인증 토큰 클라이언트측에서 확인하기
    private final String secret = "your_jwt_secret";
    @PostMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmail(@RequestBody Map<String, String> request) {
        String token = request.get("token");

        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secret.getBytes())
                    .parseClaimsJws(token);

            String email = claims.getBody().get("email", String.class);

            // 이메일 인증 업데이트
            //1. 인증테이블에서 인증번호 보낸 아이디 서치
            EmailCodeDto emailCodeDto = emailMapper.findByEmail(email);

            //2. customer에 저장할 데이터 빌드
            Customer customer = Customer.builder()
                    .customerId(email)
                    .build();

            if (emailCodeDto != null) {
                emailCodeDto.setEmailVerified(true);
                customerMapper.save(customer);
                return ResponseEntity.ok(Map.of("success", true));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "User not found"));
            }
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "Invalid or expired token"));
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
