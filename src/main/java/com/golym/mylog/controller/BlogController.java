package com.golym.mylog.controller;

import com.golym.mylog.model.dto.common.UserDto;
import com.golym.mylog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BlogController {

    private final UserService userService;

    @GetMapping("/blog")
    public String blogMainForm(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        log.info("blogMainForm()-userId= {}", userId);

        userId = "U-772a6312635540f6abc596a6652014";
//        userId = "U-40af35c0ddba463599d7fd68498efb";

        UserDto user = userService.getUser(userId);
        log.info("user= {}", user);
        model.addAttribute("user", user);


        return "/view/blog/blog";
    }

    @GetMapping("/edit")
    public String editFomr(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        log.info("blogMainForm()-userId= {}", userId);

        userId = "U-772a6312635540f6abc596a6652014";
//        userId = "U-40af35c0ddba463599d7fd68498efb";

        UserDto user = userService.getUser(userId);
        log.info("user= {}", user);
        model.addAttribute("user", user);


        return "/view/blog/edit";
    }
}
