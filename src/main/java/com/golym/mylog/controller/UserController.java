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
        userService.sendAuthcode(params);

        return new ResponseEntity<>(new ResponseDto(ResponseType.SUCCESS), HttpStatus.OK);
    }

    /**
     * 인증 메일 검증
     */
    @PostMapping("/verify-authcode")
    public ResponseEntity<?> verifyAuthcode(@Valid @RequestBody RequestVerifyAuthcodeDto params) {
        if (!userService.verifyAuthcode(params.getEmail(), params.getAuthcode()))
            throw new BadRequestException("Invalid email or authcode.");

        return new ResponseEntity<>(new ResponseDto(ResponseType.SUCCESS), HttpStatus.OK);
    }

    /**
     * 이메일 중복 확인
     */
    @PostMapping("/check-email")
    public ResponseEntity<?> checkEmail(@Valid @RequestBody RequestCheckEmailDto params) {
        if (userService.isExistEmail(params.getEmail()))
            throw new ConflictException("The resource already exists. email=" + params.getEmail());

        return new ResponseEntity<>(new ResponseDto(ResponseType.SUCCESS), HttpStatus.OK);
    }

    /**
     * 닉네임 중복 확인
     */
    @PostMapping("/check-nickname")
    public ResponseEntity<?> checkNickname(@Valid @RequestBody RequestCheckNicknameDto params) {
        if (userService.isExistNickname(params.getNickname()))
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

    /**
     * 닉네임 수정
     */
    @PatchMapping("/nickname")
    public ResponseEntity<?> updateNickname(@Valid @RequestBody RequestUpdateNicknameDto params) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        // 닉네임 수정
        userService.updateNickname(userId, params.getNickname());
        return new ResponseEntity<>(new ResponseDto(ResponseType.SUCCESS), HttpStatus.OK);
    }

    /**
     * 비밀번호 수정
     */
    @PatchMapping("/password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody RequestUpdatePasswordDto params) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        // 비밀번호 수정
        userService.updatePassword(userId, params.getPassword(), params.getNewPassword());
        return new ResponseEntity<>(new ResponseDto(ResponseType.SUCCESS), HttpStatus.OK);
    }

}
