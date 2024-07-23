package org.nmfw.foodietree.domain.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeDto;
import org.nmfw.foodietree.domain.customer.entity.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class TokenProvider {

    // 서명에 사용할 512비트의 랜덤 문자열 비밀키
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    /**
     * JWT를 생성하는 메서드
     * @param emailCodeDto - 토큰에 포함될 로그인한 유저의 정보
     * @return - 생성된 JWT의 암호화된 문자열
     */
    public String createToken(EmailCodeDto emailCodeDto) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", emailCodeDto.getCustomerId());

        return Jwts.builder()
                // token에 들어갈 서명
                .signWith(
                        Keys.hmacShaKeyFor(SECRET_KEY.getBytes())
                        , SignatureAlgorithm.HS512
                )
                // payload 에 들어갈 클레임 설정
                .setClaims(claims) // 추가 클레임은 항상 가장 먼저 설정
                .setIssuer("foodietree") // 발급자 정보
                .setIssuedAt(new Date()) // 발급 시간
                .setExpiration(Date.from(
                        Instant.now().plus(1, ChronoUnit.DAYS)
                )) // 토큰 만료 시간
                .setSubject(emailCodeDto.getCustomerId())
                .compact();
    }

    /**
     * 클라이언트가 전송한 토큰을 디코딩하여 토큰의 서명 위조 여부를 확인
     * 토큰을 JSON으로 파싱하여 안에 들어있는 클레임(토큰 정보)을 리턴
     *
     * @param token - 클라이언트가 보낸 토큰
     * @return - 토큰에 들어있는 인증 정보들을 리턴 - 회원 식별 ID, 이메일, 권한정보
     */
    public TokenUserInfo validateAndGetTokenInfo(String token) {

        Claims claims = Jwts.parserBuilder()
                // 토큰 발급자의 발급 당시 서명 삽입
                .setSigningKey(
                        Keys.hmacShaKeyFor(SECRET_KEY.getBytes())
                )
                // 서명위조 검사 진행 : 위조된 경우 Exception이 발생
                // 위조되지 않은 경우 클레임 리턴
                .build()
                .parseClaimsJws(token)
                .getBody();

        log.info("claims: {}", claims);

        return TokenUserInfo.builder()
                .userId(claims.getSubject())
                .email(claims.get("email", String.class))
                .build();
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
