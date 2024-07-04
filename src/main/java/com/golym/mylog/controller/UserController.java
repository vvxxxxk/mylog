package com.golym.mylog.controller;

import com.golym.mylog.common.constants.ResponseType;
import com.golym.mylog.common.exception.BadRequestException;
import com.golym.mylog.model.dto.RequestEmailDto;
import com.golym.mylog.model.dto.RequestVerifyAuthcodeDto;
import com.golym.mylog.model.dto.ResponseDto;
import com.golym.mylog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signupForm() {
        return "/user/signup";
    }

    @PostMapping("/signup/authcode")
    public ResponseEntity<?> sendAuthcode(@Valid @RequestBody RequestEmailDto params) {
        // 인증 메일 발송
        userService.sendAuthcode(params);
        return new ResponseEntity<>(new ResponseDto(ResponseType.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/signup/verify-authcode")
    public ResponseEntity<?> verifyAuthcode(@Valid @RequestBody RequestVerifyAuthcodeDto params) {

        log.info("params = {}", params);
        // 인증 코드 검증
        boolean isVerified = userService.verifyAuthcode(params.getEmail(), params.getAuthcode());
        if (!isVerified)
            throw new BadRequestException("Invalid email or authcode.");

        return new ResponseEntity<>(new ResponseDto(ResponseType.SUCCESS), HttpStatus.OK);
    }
}
