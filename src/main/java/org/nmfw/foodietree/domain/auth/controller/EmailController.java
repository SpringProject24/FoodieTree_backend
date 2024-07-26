package org.nmfw.foodietree.domain.auth.controller;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeDto;
import org.nmfw.foodietree.domain.auth.mapper.EmailMapper;
import org.nmfw.foodietree.domain.auth.service.EmailService;
import org.nmfw.foodietree.domain.auth.service.UserService;
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
        String refreshToken = request.get("refreshToken");

        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "Token is missing"));
        }

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

                emailMapper.save(emailCodeDto);
                userService.saveUserInfo(emailCodeDto);

                //프론트엔드에 결과값 반환하기
                return ResponseEntity.ok(Map.of("success", true, "email", email, "role", userRole));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "User not found"));
            }
        } catch (JwtException e) {
            log.error("JWT parsing error: {}", e.getMessage());

            if (refreshToken == null || refreshToken.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "Refresh token is missing"));
            }

            try {
                Jws<Claims> refreshClaims = Jwts.parser()
                        .setSigningKey(REFRESH_SECRET_KEY.getBytes())
                        .parseClaimsJws(refreshToken);

                Date expiration = refreshClaims.getBody().getExpiration();
                if (expiration.before(new Date())) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Refresh token expired"));
                }

                String email = refreshClaims.getBody().get("sub", String.class);
                String userRole = refreshClaims.getBody().get("role", String.class);

                // 새로운 Access Token 발급
                String newAccessToken = Jwts.builder()
                        .setSubject(email)
                        .claim("role", userRole)
                        .setIssuedAt(new Date())
                        .setExpiration(Date.from(Instant.now().plus(10, ChronoUnit.MINUTES)))
                        .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes())
                        .compact();

                // 새로운 Refresh Token 발급
                String newRefreshToken = Jwts.builder()
                        .setSubject(email)
                        .claim("role", userRole)
                        .setIssuedAt(new Date())
                        .setExpiration(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                        .signWith(SignatureAlgorithm.HS512, REFRESH_SECRET_KEY.getBytes())
                        .compact();

                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "accessToken", newAccessToken,
                        "refreshToken", newRefreshToken
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


//    @PostMapping("/verifyEmail")
//    public ResponseEntity<?> verifyEmail(@RequestBody Map<String, String> request) {
//        // 서버 측에서 받은 요청 데이터를 로그로 출력합니다.
//        log.info("Request Data: {}", request);
//        String token = request.get("token");
//        // 토큰이 없을 때
//        if (token == null || token.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "Token is missing"));
//        }
//
//
//        // 초기 발급한 access 토큰이 유효하다면 테이블에 회원 추가, refresh TOKEN 추가
//        // (로그인 창 = 상세페이지) access 토큰이 만료되었거나 유효하지 않다면 -> refresh token 확인 후 재발급하여 재로그인 방지(10분)
//        // 로그아웃 할 경우 localstorage에 있는 userData 삭제
//
//        // usertype이 store 인지 customer 인지 구분해서 저장 -> 서비스레이어
//        try {
//            Jws<Claims> claims = Jwts.parser()
//                    .setSigningKey(SECRET_KEY.getBytes())
//                    .parseClaimsJws(token);
//
//            String email = claims.getBody().get("sub", String.class);
//            String userRole = claims.getBody().get("role", String.class);
//
//            log.info("secretKey claim : {}", claims);
//
//            log.info("Email extracted from token: {}", email);
//
//            log.info("user Role (type) extracted from token: {}", userRole);
//
//
//            EmailCodeDto emailCodeDto = emailMapper.findByEmail(email);
//
//            log.info("EmailCodeDto retrieved from database: {}", emailCodeDto);
//
//            if (emailCodeDto != null) {
//                emailCodeDto.setUserType(userRole);
//                emailCodeDto.setEmailVerified(true);
//
//                emailMapper.save(emailCodeDto); // save가 아니라, update
//
//                userService.saveUserInfo(emailCodeDto); // 실제 usertype 에 맞게 저장
//
//                return ResponseEntity.ok(Map.of("success", true));
//            } else {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "User not found"));
//            }
//        } catch (JwtException e) {
//            log.error("JWT parsing error: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "Invalid or expired token"));
//        } catch (Exception e) {
//            log.error("An unexpected error occurred: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "message", "An unexpected error occurred"));
//        }
//    }



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
