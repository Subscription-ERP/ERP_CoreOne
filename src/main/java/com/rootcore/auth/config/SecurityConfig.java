package com.rootcore.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.rootcore.auth.security.CustomAccessDeniedHandler;
import com.rootcore.auth.security.CustomAuthenticationFailureHandler;
import com.rootcore.auth.security.CustomAuthenticationSuccessHandler;
import com.rootcore.auth.security.MenuAuthFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationFailureHandler failureHandler;
    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final MenuAuthFilter menuAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CSRF / frame 옵션
        http.csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        // =============================
        //  URL 권한 설정
        // =============================
        http.authorizeHttpRequests(auth -> auth
                // 정적 리소스
                .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()

                // 🔥 로그아웃은 인증된 사용자만 (조금 더 안전하게)
                .requestMatchers("/auth/logout").authenticated()

                // 비밀번호 재설정 관련
                .requestMatchers("/auth/password_reset/**",
                        "/auth/password_reset",
                        "/auth/password_reset_send_result",
                        "/auth/password_reset_send_result.html").permitAll()

                // ROLE ADMIN 전용
                .requestMatchers("/auth/menu_permission/**").hasAnyRole("ADMIN")

                // 그 외 인증 관련 경로 (/auth/login 등)
                .requestMatchers("/auth/**").permitAll()

                // 업무 모듈들 (현재는 모두 permitAll, 메뉴필터로 세부권한 체크)
                .requestMatchers("/cm/**", "/fi/**", "/hr/**", "/sd/**",
                        "/pm/**", "/mes/**", "/system/**").permitAll()

                // 나머지
                .anyRequest().permitAll()
        );

        // 접근 거부 핸들러
        http.exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler));

        // =============================
        //  로그인 설정
        // =============================
        http.formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/doLogin")
                .usernameParameter("userId")
                .passwordParameter("password")
                .failureHandler(failureHandler)
                .successHandler(successHandler)
                .permitAll()
        );

        // =============================
        //  Remember-Me 설정
        // =============================
        http.rememberMe(me -> me
                .key("coreone-remember-key")
                .rememberMeParameter("rememberId")
                .tokenValiditySeconds(60 * 60 * 24 * 30)
        );

        // =============================
        //  로그아웃 설정
        // =============================
        http.logout(logout -> logout
                .logoutUrl("/auth/logout")                 // POST /auth/logout
                .logoutSuccessUrl("/auth/login?logout")    // 로그아웃 성공 후 이동
                .invalidateHttpSession(true)               // 세션 삭제
                .deleteCookies("JSESSIONID", "remember-me")
                .permitAll()
        );

        // =============================
        //  메뉴 권한 필터 등록
        //  (UsernamePasswordAuthenticationFilter 이전에 실행)
        // =============================
        http.addFilterBefore(menuAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
