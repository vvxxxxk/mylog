package com.golym.mylog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class ErrorController {

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "view/error-page";
    }

    @GetMapping("/unauthorized")
    public String unauthorized() {
        return "view/error-page";
    }
}
