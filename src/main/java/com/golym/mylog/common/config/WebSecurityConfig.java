package com.golym.mylog.common.config;

import com.golym.mylog.common.filter.JwtVerificationFilter;
import com.golym.mylog.common.utils.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenUtils jwtTokenUtils;

    private final String[] EXCLUDE_PATHS = {
            // main
            "/",
            // resource
            "/css/**", "/images/**", "/js/**",
            // page
            "/signup",
            "/blog",
            // 비회원 API
            "/login",
            "/api/send-authcode",
            "/api/verify-authcode",
            "/api/check-email",
            "/api/check-nickname",
            "/api/reissue-token",
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        // configuration.setAllowedOrigins(Collections.singletonList(WEB_HOST));
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
        // Form 로그인 방식 disable
        http
                .formLogin(AbstractHttpConfigurer::disable);

        // http basic 인증방식 disable
        http
                .httpBasic((AbstractHttpConfigurer::disable));

        // 경로별 인가 설정
        http
                .authorizeHttpRequests((auth) -> auth
                .requestMatchers(EXCLUDE_PATHS).permitAll()
                .requestMatchers("/**").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated());

        // 필터 등록
        http
                .addFilterBefore(new JwtVerificationFilter(jwtTokenUtils), UsernamePasswordAuthenticationFilter.class);

        // 세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
