package com.golym.mylog.controller;

import com.golym.mylog.common.constants.ResponseType;
import com.golym.mylog.model.dto.request.RequestLoginDto;
import com.golym.mylog.model.dto.response.ResponseDto;
import com.golym.mylog.service.UserDetailsServiceImpl;
import com.golym.mylog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody RequestLoginDto params, HttpSession session) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(params.getEmail());

        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(params.getEmail(), params.getPassword());
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
            return new ResponseEntity<>(new ResponseDto(ResponseType.SUCCESS), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new ResponseDto(ResponseType.FAIL), HttpStatus.OK);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>(new ResponseDto(ResponseType.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/check-session")
    public ResponseEntity<?> checkSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName()))
            return new ResponseEntity<>(new ResponseDto(ResponseType.SUCCESS), HttpStatus.OK);
        return new ResponseEntity<>(new ResponseDto(ResponseType.FAIL), HttpStatus.OK);
    }
}
