package com.golym.mylog.controller;

import com.golym.mylog.model.dto.common.CategoryDto;
import com.golym.mylog.model.dto.common.UserDto;
import com.golym.mylog.service.CategoryService;
import com.golym.mylog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ViewController {

    private final UserService userService;
    private final CategoryService categoryService;

    /** 에러 페이지 **/
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "view/error-page";
    }

    /** 에러 페이지 **/
    @GetMapping("/unauthorized")
    public String unauthorized() {
        return "view/error-page";
    }

    /** 회원가입 페이지 **/
    @GetMapping("/signup")
    public String signupForm() {
        return "/view/user/signup";
    }

    /** 블로그 메인 페이지 **/
    @GetMapping("/blog")
    public String blogForm(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        UserDto user = userService.getUser(userId);
        List<CategoryDto> categoryList = categoryService.getCategoryList(userId);
        model.addAttribute("user", user);
        model.addAttribute("categoryList", categoryList);
        return "/view/blog/blog";
    }

    /** 글작성 페이지 **/
    @GetMapping("/blog/write")
    public String editFomr(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        UserDto user = userService.getUser(userId);
        List<CategoryDto> categoryList = categoryService.getCategoryList(userId);
        model.addAttribute("user", user);
        model.addAttribute("categoryList", categoryList);
        return "/view/blog/write";
    }
}
