package com.rootcore.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.rootcore.auth.security.CustomAuthenticationFailureHandler;
import com.rootcore.auth.security.CustomAuthenticationSuccessHandler;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationFailureHandler failureHandler;
    private final CustomAuthenticationSuccessHandler successHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth -> auth

                // 정적 리소스는 항상 허용
                .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()

                // 비밀번호 재설정 관련 전체 허용 (POST 포함)
                .requestMatchers(
                        "/auth/password_reset/**",
                        "/auth/password_reset",
                        "/auth/password_reset_send_result",
                        "/auth/password_reset_send_result.html"
                ).permitAll()

                // 로그인 이전 모든 기능은 /auth/ 로 통일 → 전체 허용
                .requestMatchers("/auth/**").permitAll()

                // 🔹 ERP 내부 경로는 모두 로그인 필요
                .requestMatchers(
                        "/cm/**",
                        "/fi/**",
                        "/hr/**",
                        "/sd/**",
                        "/pm/**",
                        "/mes/**",
                        "/system/**"
                ).permitAll()

                // 🔹 그 외 모든 요청은 인증 필요로 변경하는 것이 일반적
                .anyRequest().permitAll()
        );

        // 🔹 로그인 설정
        http.formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/doLogin")
                .usernameParameter("userId")
                .passwordParameter("password")
                .failureHandler(failureHandler)
                .successHandler(successHandler)
                .permitAll()
        );

        // 🔹 Remember-Me 설정
        http.rememberMe(me -> me
                .key("coreone-remember-key")
                .rememberMeParameter("rememberId")
                .tokenValiditySeconds(60 * 60 * 24 * 30)
        );

        return http.build();
    }
}
