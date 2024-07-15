package com.golym.mylog.controller;

import com.golym.mylog.common.constants.ResponseType;
import com.golym.mylog.common.exception.BadRequestException;
import com.golym.mylog.common.exception.ConflictException;
import com.golym.mylog.model.dto.request.*;
import com.golym.mylog.model.dto.response.ResponseDto;
import com.golym.mylog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 인증 메일 발송
     */
    @PostMapping("/send-authcode")
    public ResponseEntity<?> sendAuthcode(@Valid @RequestBody RequestSendAuthCodeDto params) {
        // 인증 메일 발송
        userService.sendAuthcode(params);
        return new ResponseEntity<>(new ResponseDto(ResponseType.SUCCESS), HttpStatus.OK);
    }

    /**
     * 인증 메일 검증
     */
    @PostMapping("/verify-authcode")
    public ResponseEntity<?> verifyAuthcode(@Valid @RequestBody RequestVerifyAuthcodeDto params) {

        log.info("params = {}", params);
        // 인증 코드 검증
        boolean isVerified = userService.verifyAuthcode(params.getEmail(), params.getAuthcode());
        if (!isVerified)
            throw new BadRequestException("Invalid email or authcode.");

        return new ResponseEntity<>(new ResponseDto(ResponseType.SUCCESS), HttpStatus.OK);
    }

    /**
     * 이메일 중복 확인
     */
    @PostMapping("/check-email")
    public ResponseEntity<?> checkEmail(@Valid @RequestBody RequestCheckEmailDto params) {
        // 이메일 중복 확인
        boolean isExisted = userService.isExistEmail(params.getEmail());
        if (isExisted)
            throw new ConflictException("The resource already exists. email=" + params.getEmail());

        return new ResponseEntity<>(new ResponseDto(ResponseType.SUCCESS), HttpStatus.OK);
    }

    /**
     * 닉네임 중복 확인
     */
    @PostMapping("/check-nickname")
    public ResponseEntity<?> checkNickname(@Valid @RequestBody RequestCheckNicknameDto params) {
        // 닉네임 중복 확인
        boolean isExisted = userService.isExistNickname(params.getNickname());
        if (isExisted)
            throw new ConflictException("The resource already exists. nickname=" + params.getNickname());

        return new ResponseEntity<>(new ResponseDto(ResponseType.SUCCESS), HttpStatus.OK);
    }

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody RequestSignupDto params) {
        if (userService.isExistEmail(params.getEmail()) || userService.isExistNickname(params.getNickname()))
            throw new ConflictException("The user already exists. Please check the email and nickname again.");

        userService.signup(params);
        return new ResponseEntity<>(new ResponseDto(ResponseType.SUCCESS), HttpStatus.OK);
    }

    /**
     * 프로필 이미지 수정
     */
    @PatchMapping("/profile-image")
    public ResponseEntity<?> updateProfileImage(@Valid @RequestBody RequestUpdateProfileImageDto params) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        userService.updateProfileImage(userId, params.getProfileImage());
        return new ResponseEntity<>(new ResponseDto(ResponseType.SUCCESS), HttpStatus.OK);
    }

}
