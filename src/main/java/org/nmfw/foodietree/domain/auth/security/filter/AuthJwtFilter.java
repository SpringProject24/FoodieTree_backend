package org.nmfw.foodietree.domain.auth.security.filter;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.dto.EmailCodeDto;
import org.nmfw.foodietree.domain.auth.security.TokenProvider;
import org.nmfw.foodietree.domain.auth.security.TokenProvider.TokenUserInfo;
import org.nmfw.foodietree.domain.auth.service.UserService;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthJwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = parseBearerToken(request);
            String refreshToken = request.getHeader("refreshToken");

            log.info("Token Forgery Verification Filter Operation!");
            if (token != null) {
                try {
                    TokenUserInfo tokenInfo = tokenProvider.validateAndGetTokenInfo(token);
                    setAuthenticationContext(request, tokenInfo);
                    log.info("필터 통과 ✅");
                } catch (JwtException e) {
                    log.warn("Access token is not valid or expired. Attempting to verify refresh token.");
                    log.info("access token 만료되어도 괜찮아 ✅");

                    if (refreshToken != null) {
                        try {
                            TokenUserInfo refreshTokenInfo = tokenProvider.validateAndGetRefreshTokenInfo(refreshToken);
                            handleRefreshToken(request, response, refreshTokenInfo);
                            log.info("refresh token 기간 안 ✅");
                        } catch (JwtException ex) {
                            log.error("Refresh token parsing error: {}", ex.getMessage());
                            log.info("refresh token 기간 지나거나 위조됨 ❌");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                            response.getWriter().write("Invalid refresh token");
                            return;
                        }
                    } else {
                        log.info("refresh token 삭제됨 ❌");
                        log.warn("No refresh token provided.");
                    }
                }
            }

        } catch (Exception e) {
            log.info("refresh token, access token 유효성 검증 통과 둘다 못함 ❌");

            log.warn("Token validation error");
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }

    private void handleRefreshToken(HttpServletRequest request, HttpServletResponse response, TokenUserInfo refreshTokenInfo) throws IOException {
        String email = refreshTokenInfo.getUsername();
        String userType = refreshTokenInfo.getRole();

        // DB 에 저장된 토큰 만료 일자
        LocalDateTime refreshTokenExpiryDate = userService.getUserRefreshTokenExpiryDate(email, userType);
        log.info("Refresh token expiry date from server: {}", refreshTokenExpiryDate);

        if (refreshTokenExpiryDate.isAfter(LocalDateTime.now())) {

            // 로그인 함과 동시에 토큰, 리프레시 토큰 재발급
            String token = tokenProvider.createToken(email, userType);
            String refreshToken = tokenProvider.createRefreshToken(email, userType);
            userService.setUserRefreshTokenExpiryDate(refreshToken ,email, userType);

            log.info("리프레시 토큰 및 액세스 토큰 재발급 ✅");

            // 응답 상태 코드 설정 (200 OK)
            response.setStatus(HttpServletResponse.SC_OK);

            // Set authentication context
            TokenUserInfo newAccessTokenInfo = tokenProvider.validateAndGetTokenInfo(token);
            setAuthenticationContext(request, newAccessTokenInfo);

            // Set new tokens in response headers
            response.setHeader("token", token);
            response.setHeader("refreshToken", refreshToken);
            return;
        }

        log.warn("리프레시 토큰이 만료되었거나 잘못되었습니다.");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Invalid refresh token");

    }

    private void setAuthenticationContext(HttpServletRequest request, TokenUserInfo tokenInfo) {
        AbstractAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                tokenInfo, null, List.of(new SimpleGrantedAuthority(tokenInfo.getRole()))
        );

        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private String parseBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}