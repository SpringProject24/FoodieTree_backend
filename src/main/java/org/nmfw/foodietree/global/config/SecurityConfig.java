package org.nmfw.foodietree.global.config;
import lombok.RequiredArgsConstructor;
import org.nmfw.foodietree.domain.auth.security.filter.AuthJwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthJwtFilter authJwtFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // CSRF 보호 비활성화
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용 안 함
                .and()
                .authorizeRequests()
                .antMatchers("/reservation/**").authenticated() // 특정 경로에만 필터 적용
                .anyRequest().permitAll(); // 나머지 경로는 인증 불필요

        // JwtAuthFilter를 UsernamePasswordAuthenticationFilter 전에 실행하도록 설정
        http.addFilterBefore(authJwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    // password filter
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
