package org.nmfw.foodietree.domain.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class TokenProvider {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${env.jwt.refreshSecret}")
    private String REFRESH_SECRET_KEY;

    // create access token : short term for access server DB and saved at local storage
    public String createToken(EmailCodeDto emailCodeDto) {

        // customerId와 storeId 중 null이 아닌 값을 선택
        String email = emailCodeDto.getCustomerId() != null ? emailCodeDto.getCustomerId() : emailCodeDto.getStoreId();
        String userType = emailCodeDto.getUserType();

        return Jwts.builder()
                // header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
                .signWith(
                        Keys.hmacShaKeyFor(SECRET_KEY.getBytes())
                        , SignatureAlgorithm.HS512
                )
                .claim("role", userType) // role 클레임에 userType 추가
                // payload에 들어갈 내용
                .setSubject(email) // sub
                .setIssuer("foodie tree") // iss
                .setIssuedAt(new Date()) // iat
                .setExpiration(Date.from(Instant.now().plus(5, ChronoUnit.MINUTES))) // exp
                .compact();
    }

    // refresh token : for long term life cycle and did not need to verify email link
    // save at user's DB
    public String createRefreshToken(String email) {

        return Jwts.builder()
                .signWith(
                        Keys.hmacShaKeyFor(REFRESH_SECRET_KEY.getBytes())
                        , SignatureAlgorithm.HS512
                )
                .setSubject(email)
                .setIssuer("foodie tree token refresher")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(30, ChronoUnit.DAYS))) // 유효기간 30일로 설정
                .compact();
    }

    public Date getExpirationDateFromToken(String token) {
        byte[] keyBytes = SECRET_KEY.getBytes();
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }


   public TokenUserInfo validateAndGetTokenInfo(String token) {

        try {
            //토큰 발급 당시 서명 처리
            Claims claims = Jwts.parserBuilder()
                    // 토큰 발급자의 발급 당시 서명을 넣음
                    .setSigningKey(
                            Keys.hmacShaKeyFor(SECRET_KEY.getBytes())
                    )
                    // 서명위조 검사 진행 : 위조된 경우 Exception이 발생
                    // 위조되지 않은 경우 클레임을 리턴
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            log.info("validateAndGetTokenInfo Claims: {}", claims);

            TokenUserInfo build = TokenUserInfo.builder()
                    .email(claims.get("sub", String.class))
                    .role(claims.get("role", String.class))
                    .build();

            log.info("검증 통과 후 토큰 유저 인포 정보 {},{}", build.email, build.role);

            return build;

        } catch (JwtException e) {
            log.error("Token validation error: {}", e.getMessage());
            throw e; // 또는 적절한 예외 처리
        }
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TokenUserInfo {
        private String role;
        private String email;
    }
}
