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

        http.csrf(csrf -> csrf.disable())
            .headers(headers ->
                headers.frameOptions(frame -> frame.sameOrigin())
            );

        http.authorizeHttpRequests(auth -> auth

        	    .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()

        	    .requestMatchers(
        	        "/auth/password_reset/**",
        	        "/auth/password_reset",
        	        "/auth/password_reset_send_result",
        	        "/auth/password_reset_send_result.html"
        	    ).permitAll()

        	    // ADMIN만 접근 가능
        	    .requestMatchers("/auth/menu_permission/**").hasAnyRole("ADMIN")


        	    .requestMatchers("/auth/**").permitAll()

        	    .requestMatchers(
        	        "/cm/**",
        	        "/fi/**",
        	        "/hr/**",
        	        "/sd/**",
        	        "/pm/**",
        	        "/mes/**",
        	        "/system/**"
        	    ).permitAll()

        	    .anyRequest().permitAll()
        	);


        // 로그인 설정
        http.formLogin(form -> form
            .loginPage("/auth/login")
            .loginProcessingUrl("/auth/doLogin")
            .usernameParameter("userId")
            .passwordParameter("password")
            .failureHandler(failureHandler)
            .successHandler(successHandler)
            .permitAll()
        );

        // Remember-Me
        http.rememberMe(me -> me
            .key("coreone-remember-key")
            .rememberMeParameter("rememberId")
            .tokenValiditySeconds(60 * 60 * 24 * 30)
        );

        return http.build();
    }

}
