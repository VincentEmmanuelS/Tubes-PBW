package com.tubes.pbw.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;

@Controller
public class PageController {
    
    @GetMapping("/")
    public String homepage() {
        return "homepage";
    }

    @GetMapping("/onboarding")
    public String onboarding() {
        return "onboarding";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

}
