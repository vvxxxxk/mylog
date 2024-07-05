package com.golym.mylog.service;

import com.golym.mylog.common.exception.BadRequestException;
import com.golym.mylog.common.utils.JwtTokenUtils;
import com.golym.mylog.model.dto.common.JwtTokenMappingDto;
import com.golym.mylog.model.dto.request.RequestReissueTokenDto;
import com.golym.mylog.model.dto.response.ResponseReissueTokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseReissueTokenDto reissueToken(RequestReissueTokenDto params) {

        String refreshToken = params.getToken();

        if (!redisTemplate.hasKey(refreshToken)) {
            System.out.println("토큰 레디스에 저장 X");
            throw new BadRequestException("Invalid refresh token");
        } else {
            System.out.println("토큰 레디스에 저장되어 있음");
        }

        // 리프레시 토큰이 유효한지 확인
        if (!jwtTokenUtils.validateToken(refreshToken)) {
            throw new BadRequestException("Invalid refresh token");
        }

        // 토큰에서 사용자 정보 추출
        Authentication authentication = jwtTokenUtils.getAuthentication(refreshToken);
        String userId = jwtTokenUtils.getUserId(authentication);

        // 새로운 액세스 토큰 발급
        String newAccessToken = jwtTokenUtils.generateAccessToken(authentication);

        // 새로운 액세스 토큰을 Redis에 저장된 리프레시 토큰에 연결
        redisTemplate.opsForValue().set(refreshToken, newAccessToken, REFRESH_TOKEN_EXPIRATION, TimeUnit.MILLISECONDS);

        // 새로운 액세스 토큰 반환
        return ResponseReissueTokenDto.builder()
                .userId(userId)
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
