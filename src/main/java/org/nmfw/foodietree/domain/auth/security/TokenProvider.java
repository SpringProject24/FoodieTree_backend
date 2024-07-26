package org.nmfw.foodietree.domain.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import nonapi.io.github.classgraph.json.JSONUtils;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeDto;
import org.nmfw.foodietree.domain.customer.entity.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class TokenProvider {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // create access token : short term for access server DB and saved at local storage
    public String createToken(EmailCodeDto emailCodeDto) {

        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        System.out.println("Decoded Key Length in Bytes: " + decodedKey.length);
        System.out.println("Decoded Key Length in Bits: " + (decodedKey.length * 8));

        byte[] keyBytes = SECRET_KEY.getBytes();
        Key key = Keys.hmacShaKeyFor(keyBytes);
        System.out.println("Secret Key Length in Bytes: " + key.getEncoded().length);
        System.out.println("Secret Key Length in Bits: " + (key.getEncoded().length * 8));

        // customerId와 storeId 중 null이 아닌 값을 선택
        String email = emailCodeDto.getCustomerId() != null ? emailCodeDto.getCustomerId() : emailCodeDto.getStoreId();
        String userType = emailCodeDto.getUserType();

        return Jwts.builder()
                .claim("role", userType) // role 클레임에 userType 추가
                // header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
                .signWith(key, SignatureAlgorithm.HS512)
                // payload에 들어갈 내용
                .setSubject(email) // sub
                .setIssuer("foodie tree") // iss
                .setIssuedAt(new Date()) // iat
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.MINUTES))) // exp
                .compact();
    }

    // refresh token : for long term life cycle and did not need to verify email link
    // save at user's DB
    public String createRefreshToken(String email) {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        byte[] keyBytes = SECRET_KEY.getBytes();
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512)
                .setSubject(email)
                .setIssuer("foodie tree")
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
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY));

        try {
            //토큰 발급 당시 서명 처리
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            log.info("Claims: {}", claims);

            return TokenUserInfo.builder()
                    .userId(claims.getSubject()) // 이거 왜 추가하는거지?
                    .email(claims.get("sub", String.class))
                    .role(claims.get("role", String.class))
                    .build();

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
        private String userId; // 얘는 왜 있는건지 아직 파악 못함
        private String role;
        private String email;
    }
}
