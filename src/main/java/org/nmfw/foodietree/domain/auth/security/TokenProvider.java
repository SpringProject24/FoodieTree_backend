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

    public String createToken(EmailCodeDto emailCodeDto) {

        byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
        System.out.println("Decoded Key Length in Bytes: " + decodedKey.length);
        System.out.println("Decoded Key Length in Bits: " + (decodedKey.length * 8));

        byte[] keyBytes = SECRET_KEY.getBytes();
        Key key = Keys.hmacShaKeyFor(keyBytes);
        System.out.println("Secret Key Length in Bytes: " + key.getEncoded().length);
        System.out.println("Secret Key Length in Bits: " + (key.getEncoded().length * 8));

        return Jwts.builder()
                // header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
                .signWith(key, SignatureAlgorithm.HS512)
                // payload에 들어갈 내용
                .setSubject(emailCodeDto.getCustomerId()) // sub
                .setIssuer("demo app") // iss
                .setIssuedAt(new Date()) // iat
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS))) // exp
                .compact();
    }

   public TokenUserInfo validateAndGetTokenInfo(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY));

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            log.info("Claims: {}", claims);

            return TokenUserInfo.builder()
                    .userId(claims.getSubject())
                    .email(claims.get("email", String.class))
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
        private String userId;
        private String email;
    }
}
