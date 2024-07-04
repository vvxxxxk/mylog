package com.golym.mylog.service;

import com.golym.mylog.common.utils.JwtTokenUtils;
import com.golym.mylog.model.dto.common.JwtTokenMappingDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtTokenUtils jwtTokenUtils;

    @Value("${security.jwt.refresh-token-expire}")
    private long REFRESH_TOKEN_EXPIRATION;

    public JwtTokenMappingDto generateToken(Authentication authentication) {

        // generate token
        String accessToken = jwtTokenUtils.generateAccessToken(authentication);
        String refreshToken = jwtTokenUtils.generateRefreshToken(authentication);
        log.info("[accessToken]=[{}]", accessToken);
        log.info("[refreshToken]=[{}]", refreshToken);

        redisTemplate.opsForValue().set(refreshToken, accessToken, REFRESH_TOKEN_EXPIRATION, TimeUnit.MICROSECONDS);

        // Return token
        return JwtTokenMappingDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
