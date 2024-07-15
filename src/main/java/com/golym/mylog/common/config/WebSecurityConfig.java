package com.golym.mylog.common.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final String[] EXCLUDE_PATHS = {
            // resource
            "/css/**", "/images/**", "/js/**",
            // page
            "/",
            "/access-denied",
            "/unauthorized",
            "/signup",
            "/main",
            // ################## TEST #####################
            "/blog/{userId}",
            "/blog/write",
            "/api/file/image",
            "/profile",
            // #############################################
            // View
            "/blog/post/{postId}",
            // 비회원 API
            "/api/auth/login",
            "/api/auth/logout",
            "/api/user/signup",
            "/api/user/send-authcode",
            "/api/user/verify-authcode",
            "/api/user/check-email",
            "/api/user/check-nickname",
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setMaxAge(3600L);
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }));

        // csrf disable
        http
                .csrf(AbstractHttpConfigurer::disable);
        // 경로별 인가 설정
        http
                .authorizeHttpRequests((auth) -> auth
                .requestMatchers(EXCLUDE_PATHS).permitAll()
                .requestMatchers(HttpMethod.GET, "/api/blog/post").permitAll()
//                .requestMatchers("/blog").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated());
        // Form 로그인 방식 사용
        http
                .formLogin(AbstractHttpConfigurer::disable);
        // 세션 설정
        http
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .maximumSessions(1)
                        .expiredUrl("/login?expired=ture"));
        http
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint));

        return http.build();
    }
}
