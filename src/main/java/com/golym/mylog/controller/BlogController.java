package com.golym.mylog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BlogController {

    @GetMapping("/blog")
    public String blogMainForm() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        System.out.println("BlogController.blogMainForm");
        log.info("blogMainForm()-userId= {}", userId);

        return "/view/blog/blog";
    }

    @GetMapping("/api/post")
    public ResponseEntity<?> getPosts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        System.out.println("BlogController.getPosts");
        log.info("getPosts()-userId= {}", userId);

        return null;
    }
}
