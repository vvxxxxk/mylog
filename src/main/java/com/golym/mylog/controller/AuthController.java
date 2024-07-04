package com.golym.mylog.controller;

import com.golym.mylog.common.utils.JwtTokenUtils;
import com.golym.mylog.model.dto.common.ErrorMessageDto;
import com.golym.mylog.model.dto.common.JwtTokenMappingDto;
import com.golym.mylog.model.dto.request.RequestLoginDto;
import com.golym.mylog.model.dto.response.ResponseLoginDto;
import com.golym.mylog.service.JwtTokenService;
import com.golym.mylog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final JwtTokenUtils jwtTokenUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody RequestLoginDto params) {

        Authentication authentication = userService.login(params);

        if (authentication == null) {
            return new ResponseEntity<>(ErrorMessageDto.builder()
                    .code(401)
                    .status("UNAUTHORIZED")
                    .message("Invalid email or password")
                    .build(),
                    HttpStatus.UNAUTHORIZED);
        }

        // Generate token and Save token to Redis
        JwtTokenMappingDto jwtTokenMappingDto = jwtTokenService.generateToken(authentication);

        // Return token to client
        return new ResponseEntity<>(ResponseLoginDto.builder()
                .userId(jwtTokenUtils.getUserId(authentication)) // test code
                .accessToken(jwtTokenMappingDto.getAccessToken())
                .refreshToken(jwtTokenMappingDto.getRefreshToken())
                .build(), HttpStatus.OK);
    }
}
