package org.nmfw.foodietree.domain.auth.security.filter;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.auth.security.TokenProvider;
import org.nmfw.foodietree.domain.auth.security.TokenProvider.TokenUserInfo;
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
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthJwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = parseBearerToken(request);

            log.info("Token Forgery Verification Filter Operation!");
            if (token != null) {
                TokenUserInfo tokenInfo = tokenProvider.validateAndGetTokenInfo(token);

                AbstractAuthenticationToken auth
                        = new UsernamePasswordAuthenticationToken(
                        tokenInfo,
                        null // password
                );
                // when token verification operator is done
                auth.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (Exception e) {
            log.warn("token is not certificated");
            e.printStackTrace();
        }

        // filter chain
        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        /*
             -- request header

             {
                'Authorization' : 'Bearer dhfdlksfd67fdskjfhskfhkssadfasf',
                'Content-type' : 'application/json'
             }
         */

        String bearerToken = request.getHeader("Authorization");

        // remove string "Bearer"
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}