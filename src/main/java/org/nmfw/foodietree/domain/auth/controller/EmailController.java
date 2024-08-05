package org.nmfw.foodietree.domain.auth.controller;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeDto;
import org.nmfw.foodietree.domain.auth.mapper.EmailMapper;
import org.nmfw.foodietree.domain.auth.service.EmailService;
import org.nmfw.foodietree.domain.auth.service.UserService;
import org.nmfw.foodietree.domain.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.time.Instant;
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

    @Value("${env.jwt.refreshSecret}")
    private String REFRESH_SECRET_KEY;

    @PostMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmail(@RequestBody Map<String, String> request) {
        log.info("Request Data: {}", request);
        String token = request.get("token");
        log.info("access token 있는지 확인 {}", token);

        // access token이 없을 경우 bad request 반환 - 로그인 페이지로 리다이렉션
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "Token is missing"));
        }

        try {
            // access token 유효성 검사
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .parseClaimsJws(token);

            String email = claims.getBody().get("sub", String.class);
            String userRole = claims.getBody().get("role", String.class);

            log.info("secretKey claim : {}", claims);
            log.info("Email extracted from token: {}", email);
            log.info("user Role (type) extracted from token: {}", userRole);

            // 이메일 정보를 데이터베이스에서 조회
            EmailCodeDto emailCodeDto = emailMapper.findByEmail(email);
            log.info("EmailCodeDto retrieved from database: {}", emailCodeDto);

            // 이메일 정보가 있을 경우
            if (emailCodeDto != null) {
                emailCodeDto.setUserType(userRole);
                emailCodeDto.setEmailVerified(true);

                // 이메일 인증이 완료되지 않은 경우 - 실제 회원가입이 되지 않은 경우
                if (!emailCodeDto.isEmailVerified()) {
                    // 이메일 재전송 페이지로 리다이렉션
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "Email link is not verified! Please resend verification email."));
                } else {
                        // 이메일 인증이 완료된 경우 (재로그인, 토큰 재부여)
                        // 새로운 Access Token 발급
                        String newAccessToken = Jwts.builder()
                                .setSubject(email)
                                .claim("role", userRole)
                                .setIssuedAt(new Date())
                                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.MINUTES)))
                                .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes())
                                .compact();

                        // 새로운 Refresh Token 발급
                        String newRefreshToken = Jwts.builder()
                                .setSubject(email)
                                .claim("role", userRole)
                                .setIssuedAt(new Date())
                                .setExpiration(Date.from(Instant.now().plus(30, ChronoUnit.DAYS)))
                                .signWith(SignatureAlgorithm.HS512, REFRESH_SECRET_KEY.getBytes())
                                .compact();

                    //실제 회원가입(테이블에 저장)이 되지 않은 경우 false
                    if(!(userService.findByEmail(emailCodeDto))) {
                        log.info("email이 회원가입저장되지 않은 경우 dto {}", emailCodeDto);
                        emailMapper.save(emailCodeDto);
                        userService.saveUserInfo(emailCodeDto);
                    } else {
                    // 실제 회원가입이 되어있는 경우, 로그인 하는데 access token 기간이 종료된 경우
                        // 만료 기한 access, refresh 업데이트
                        log.info("email이 회원가입저장 되어있는 경우 dto {}", emailCodeDto);
                        emailMapper.update(emailCodeDto);
                        userService.updateUserInfo(emailCodeDto);
                    }

                    return ResponseEntity.ok(Map.of(
                            "success", true,
                            "accessToken", newAccessToken,
                            "refreshToken", newRefreshToken,
                            "email", email,
                            "role", userRole,
                            "message", "Token reissued successfully."
                    ));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "User in verification tbl not found"));
            }
        } catch (JwtException e) {
            log.error("access token 의 기한이 만료되었거나 위조되었습니다.");
            log.error("JWT parsing error: {}", e.getMessage());

            try {
                String refreshToken = request.get("refreshToken");
                log.info("refresh token 값이 있니? {}",refreshToken);
                // refresh token 유효성 검사
                Jws<Claims> refreshClaims = Jwts.parser()
                        .setSigningKey(REFRESH_SECRET_KEY.getBytes())
                        .parseClaimsJws(refreshToken);

                Date expiration = refreshClaims.getBody().getExpiration();
                log.info("refresh 토큰의 기한 {}", expiration);

                // refresh token이 만료된 경우
                if (expiration.before(new Date())) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Refresh token expired"));
                }

                // 아직 refresh token 기한이 남아있는 경우
                String email = refreshClaims.getBody().get("sub", String.class);
                String userRole = refreshClaims.getBody().get("role", String.class);
                log.info("refresh 토큰이 유효합니다 ! 이메일 : {}, role : {}", email, userRole);

                // 새로운 Access Token 발급
                String newAccessToken = Jwts.builder()
                        .setSubject(email)
                        .claim("role", userRole)
                        .setIssuedAt(new Date())
                        .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.MINUTES)))
                        .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes())
                        .compact();

                // 새로운 Refresh Token 발급
                String newRefreshToken = Jwts.builder()
                        .setSubject(email)
                        .claim("role", userRole)
                        .setIssuedAt(new Date())
                        .setExpiration(Date.from(Instant.now().plus(30, ChronoUnit.DAYS)))
                        .signWith(SignatureAlgorithm.HS512, REFRESH_SECRET_KEY.getBytes())
                        .compact();

                // token 값 반환
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "accessToken", newAccessToken,
                        "c", newRefreshToken,
                        "email", email,
                        "role", userRole,
                        "message", "Token reissued successfully."
                ));

            } catch (JwtException ex) {
                log.error("Refresh token parsing error: {}", ex.getMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Invalid refresh token"));
                }
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
