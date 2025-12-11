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

        http.csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()

                .requestMatchers("/auth/password_reset/**",
                        "/auth/password_reset",
                        "/auth/password_reset_send_result",
                        "/auth/password_reset_send_result.html").permitAll()

                .requestMatchers("/auth/menu_permission/**").hasAnyRole("ADMIN")

                .requestMatchers("/auth/**").permitAll()

                .requestMatchers("/cm/**", "/fi/**", "/hr/**", "/sd/**",
                        "/pm/**", "/mes/**", "/system/**").permitAll()

                .anyRequest().permitAll()
        );

        http.exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler));

        http.formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/doLogin")
                .usernameParameter("userId")
                .passwordParameter("password")
                .failureHandler(failureHandler)
                .successHandler(successHandler)
                .permitAll()
        );

        http.rememberMe(me -> me
                .key("coreone-remember-key")
                .rememberMeParameter("rememberId")
                .tokenValiditySeconds(60 * 60 * 24 * 30)
        );

        //	로그아웃 설정
        http.logout(logout -> logout
                .logoutUrl("/auth/logout")                 // POST /auth/logout
                .logoutSuccessUrl("/auth/login?logout")    // 로그아웃 성공 후 이동
                .invalidateHttpSession(true)               // 세션 삭제
                .deleteCookies("JSESSIONID", "remember-me")
                .permitAll()
        );
        
        //	가장 중요한 부분 — URL 차단 필터 등록
        http.addFilterBefore(menuAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
